package org.makumba.parade.model.managers;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.makumba.DataDefinition;
import org.makumba.FieldDefinition;
import org.makumba.MakumbaSystem;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowMakumba;
import org.makumba.parade.model.interfaces.ParadeManager;
import org.makumba.parade.model.interfaces.RowRefresher;

public class MakumbaManager implements RowRefresher, ParadeManager {

    final static int NEWFORM = 0;

    final static int ADDFORM = 10;

    final static int LIST = 20;

    final static int EDITFORM = 30;

    static {
        // force a class load
        Class c = org.makumba.MakumbaSystem.class;
    }

    public void rowRefresh(Row row) {
        RowMakumba makumbadata = new RowMakumba();
        makumbadata.setDataType("makumba");

        String root = row.getRowpath() + File.separator + "public_html";
        makumbadata.setVersion(getMakumbaVersion(root));

        row.addManagerData(makumbadata);
    }

    public String getMakumbaVersion(String p) {
        final String path = p;
        String version = "unknown";

        try {
            java.io.File fl = new java.io.File((path + "/WEB-INF/lib/makumba.jar").replace('/',
                    java.io.File.separatorChar));
            JarFile jar = new JarFile(fl);
            Manifest mf = jar.getManifest();
            Attributes att = mf.getAttributes("Makumba");
            version = att.getValue("Version");
            jar.close();
            return version;
        } catch (Exception e) {
            // when no version info is inside JAR's META-INF/manifest.mf file
            return getMakumbaVersionOld(p);
        }
    }

    private String getMakumbaVersionOld(String p) {
        final String path = p;
        Class c = null;

        try {
            c = new ClassLoader() {
                public Class findClass(String name) throws ClassNotFoundException {
                    try {
                        File fl = new File((path + "/WEB-INF/lib/makumba.jar").replace('/', File.separatorChar));
                        if (!fl.exists())
                            throw new ClassNotFoundException("Jar file " + fl + " not found");
                        JarFile f = new JarFile(fl);
                        String nm = name.replace('.', '/') + ".class";
                        JarEntry j = f.getJarEntry(nm);
                        InputStream i = f.getInputStream(j);
                        ByteArrayOutputStream bo = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int n;
                        while ((n = i.read(buffer)) != -1)
                            bo.write(buffer, 0, n);
                        i.close();
                        f.close();
                        byte b[] = bo.toByteArray();
                        return defineClass(name, b, 0, b.length);
                    } catch (IOException e) {
                        throw new ClassNotFoundException(name);
                    }
                }
            }.findClass("org.makumba.MakumbaSystem");
        } catch (ClassNotFoundException e) {
            if (e.getMessage().startsWith("Jar file"))
                return "no makumba.jar";
            return "invalid jar";
        }
        Method m = null;
        try {
            m = c.getMethod("getVersion", new Class[0]);
        } catch (NoSuchMethodException nsme) {
            return "< 0.5.0.10";
        }

        String ret = null;
        try {
            ret = (String) m.invoke(null, new Object[0]);
        } catch (Throwable t) {
            throw new RuntimeException("did not expect " + t);
        }

        if (ret.startsWith("makumba-") || ret.startsWith("makumba_"))
            return ret.substring("makumba-".length()).replace('_', '.');
        if (ret.length() == 0)
            return "development version";
        return ret;
    }

    public void newRow(String name, Row r, Map m) {
        // TODO Auto-generated method stub

    }

    private static Vector[] extractFields(String object) throws Exception {
        Vector[] result = new Vector[2];
        Vector fields = new Vector();
        Vector sets = new Vector();

        // getting the data definition

        DataDefinition dd;

        try {
            // FIXME - this should get the datadefinition of the MDDs in a given context
            dd = MakumbaSystem.getDataDefinition(object);

        } catch (Throwable t) {
            throw new Exception("Could not find such a data defintion", t);
        }

        // iterating over the DataDefinition, extracting normal fields and sets
        Vector allFields = dd.getFieldNames();

        for (int i = 0; i < allFields.size(); i++) {
            FieldDefinition fd = dd.getFieldDefinition(i);

            if (fd.getType().equals("set") || fd.getType().equals("setComplex"))
                sets.add(fd);
            else
                fields.add(fd);
        }

        result[0] = fields;
        result[1] = sets;

        return result;
    }

    private static void generateFile(File file, int type, String object, String action, Vector[] processData,
            String header, String footer, String beforeForm, String afterForm, String beforeField, String afterField,
            String beforeFieldElement, String afterFieldElement) {

        System.out.println("Start generation of file " + file.getAbsolutePath() + "...");

        Vector fields = processData[0];
        Vector sets = processData[1];

        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fw);

            out.append(header + "\n");
            out.append(beforeForm + "\n");

            if (type == NEWFORM) {
                out.append("<mak:newForm type=\"" + object + "\" action=\"" + action + "\">\n");
            }
            if (type == ADDFORM) {
                out.append("<mak:object from=\"" + object + " o\" where=\"o=$pointer\">\n");
            }
            if (type == LIST) {
                out.append("<mak:list from=\"" + object + " o\">\n");
            }
            if (type == EDITFORM) {
                out.append("<mak:object from=\"" + object + " o\" where=\"o=$pointer\">\n");
                out.append("<mak:editForm object=\"o\" action=\"" + action + "\" method=\"post\">\n");
            }

            // iterating over the normal fields
            for (int i = 0; i < fields.size(); i++) {
                FieldDefinition fd = (FieldDefinition) fields.get(i);
                out.append(beforeField + "\n");

                // name
                out.append(beforeFieldElement);
                out.append(fd.getName());
                out.append(afterFieldElement + "\n");

                // tag
                out.append(beforeFieldElement);

                if (type == NEWFORM) {
                    out.append("<mak:input field=\"");
                    out.append(fd.getName());
                    out.append("\"/>");
                }
                if (type == LIST) {
                    out.append("<mak:value expr=\"");
                    out.append("o." + fd.getName());
                    out.append("\"/>");
                }
                if (type == EDITFORM) {
                    out.append("<mak:input field=\"");
                    out.append("o." + fd.getName());
                    out.append("\"/>");
                }
                out.append(afterFieldElement + "\n");
                out.append(afterField + "\n");
            }

            // if newForm, we have to generate an addForm page
            if (type == NEWFORM) {
                File f = new File(getFileNameFromObject(object, ADDFORM));

                generateFile(f, ADDFORM, object, action, processData, header, footer, beforeForm, afterForm,
                        beforeField, afterField, beforeFieldElement, afterFieldElement);
            } else {
                // iterating over the sets
                // TODO editForm
                for (int i = 0; i < sets.size(); i++) {
                    FieldDefinition fd = (FieldDefinition) sets.get(i);
                    DataDefinition dd = fd.getDataDefinition();

                    // sorting out only the normal fields, we don't generate internal set generation.
                    Vector[] extractInnerFields = null;
                    try {
                        extractInnerFields = extractFields(dd.getName());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Vector innerFields = extractInnerFields[0];

                    if (type == ADDFORM) {
                        out.append("<mak:addForm object=\"o\" field=\"" + fd.getName() + "\" action=\""
                                + file.getName() + "?pointer=<mak:value expr=\"o\"/>+" + "\" method=\"post\">\n");
                    }
                    if (type == LIST) {
                        out.append("<mak:list from=\"o." + fd.getName() + " o1\">\n");
                    }

                    // generating the inner fields
                    for (int j = 0; j < innerFields.size(); j++) {
                        FieldDefinition innerFd = dd.getFieldDefinition(j);

                        out.append(beforeField + "\n");

                        // name
                        out.append(beforeFieldElement);
                        out.append(innerFd.getName());
                        out.append(afterFieldElement + "\n");

                        // tag
                        if (type == ADDFORM) {
                            out.append(beforeFieldElement);
                            out.append("<mak:input field=\"");
                            out.append(innerFd.getName());
                            out.append("\"/>");
                            out.append(afterFieldElement + "\n");
                        }
                        if (type == LIST) {
                            out.append(beforeFieldElement);
                            out.append("<mak:value expr=\"");
                            out.append("o1." + innerFd.getName());
                            out.append("\"/>");
                            out.append(afterFieldElement + "\n");
                        }
                    }
                    if (type == ADDFORM) {
                        out.append("<input type=\"submit\" name=\"Add\">\n");
                        out.append("</mak:addForm>\n");
                    }
                    if (type == LIST) {
                        out.append("</mak:list>\n");
                    }

                    out.append(afterField + "\n");

                } // end iterating over the sets
            }

            // closing forms
            if (type == NEWFORM) {
                out.append("</mak:newForm>\n");
            }
            if (type == ADDFORM) {
                out.append("</mak:object>\n");
            }
            if (type == LIST) {
                out.append("</mak:list>\n");
            }
            if (type == EDITFORM) {
                out.append("</mak:editForm>\n");
                out.append("</mak:object>\n");
            }

            out.append(afterForm + "\n");
            out.append(footer + "\n");

            out.flush();
            out.close();
            fw.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Finished generation.");

    }

    public static void main(String[] args) {
        String object = "general.Person";

        String path = "";

        String header = "<%@ taglib uri=\"http://www.makumba.org/presentation\" prefix=\"mak\" %>\n"
                + "<%@ taglib uri=\"http://java.sun.com/jstl/core_rt\" prefix=\"c\" %>";
        String footer = "";
        String beforeForm = "<table>";
        String afterForm = "</table>";
        String beforeField = "<tr>";
        String afterField = "</tr>";
        String beforeFieldElement = "<td>";
        String afterFieldElement = "</td>";

        Vector[] extractedFields = null;
        try {
            extractedFields = extractFields(object);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // newForm
        File f = new File(getFileNameFromObject(object, NEWFORM));
        generateFile(f, NEWFORM, object, getFileNameFromObject(object, NEWFORM), extractedFields, header, footer,
                beforeForm, afterForm, beforeField, afterField, beforeFieldElement, afterFieldElement);

    }

    private static String getFileNameFromObject(String object, int type) {
        String suffix = object.replace('.', '_');
        String typeName = "";
        if (type == NEWFORM)
            typeName = "new";
        if (type == ADDFORM)
            typeName = "add";
        if (type == LIST)
            typeName = "list";
        if (type == EDITFORM)
            typeName = "edit";
        return (typeName + suffix + ".jsp");
    }

}
