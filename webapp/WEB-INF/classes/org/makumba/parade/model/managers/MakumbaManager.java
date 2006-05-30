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
import java.util.HashSet;
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

            System.out.println("DEBUG INFO: Extracting fields: field name " + fd.getName() + " of type " + fd.getType());

            if (isSet(fd) || isInternalSet(fd) || isPtr(fd))  
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

            out.write(header + "\n");
            out.write(beforeForm + "\n");

            if (type == NEWFORM) {
                out.write("\n<!-- Makumba Generator - START OF NEW PAGE FOR OBJECT " + object + " -->\n");
                out.write("<mak:newForm type=\"" + object + "\" action=\"" + action + "\">\n");
            }
            if (type == ADDFORM) {
                out.write("\n<!-- Makumba Generator - START OF ADD PAGE FOR OBJECT " + object + " -->\n");
                out.write("<mak:object from=\"" + object + " o\" where=\"o=$pointer\">\n");
            }
            if (type == LIST) {
                out.write("\n<!-- Makumba Generator - START OF LIST PAGE FOR OBJECT " + object + " -->\n");
                out.write("<mak:list from=\"" + object + " o\">\n");
            }
            if (type == EDITFORM) {
                out.write("\n<!-- Makumba Generator - START OF EDIT PAGE FOR OBJECT " + " -->\n");
                out.write("<mak:object from=\"" + object + " o\" where=\"o=$pointer\">\n");
                out.write("<mak:editForm object=\"o\" action=\"" + action + "\" method=\"post\">\n");
            }

            if (type != ADDFORM) {
                out.write("\n<!-- Makumba Generator - START OF NORMAL FIELDS -->\n");
                // iterating over the normal fields
                // we start at 3 because before there is the field itself, TS_CREATE and TS_MODIFY
                for (int i = 3; i < fields.size(); i++) {
                    FieldDefinition fd = (FieldDefinition) fields.get(i);
                    out.write(beforeField + "\n");

                    // name
                    out.write(beforeFieldElement);
                    out.write(fd.getName());
                    out.write(afterFieldElement + "\n");

                    // tag
                    out.write(beforeFieldElement);

                    if (type == NEWFORM) {
                        out.write("<mak:input field=\"");
                        out.write(fd.getName());
                        out.write("\"/>");
                    }
                    if (type == LIST) {
                        out.write("<mak:value expr=\"");
                        out.write("o." + fd.getName());
                        out.write("\"/>");
                    }
                    if (type == EDITFORM) {
                        out.write("<mak:input field=\"");
                        out.write("o." + fd.getName());
                        out.write("\"/>");
                    }

                    out.write(afterFieldElement + "\n");
                    out.write(afterField + "\n");
                }
                if (type == EDITFORM) {
                    out.write("\n" + beforeField + "\n");
                    out.write(beforeFieldElement);
                    out.write("<input type=\"submit\" name=\"Submit\">");
                    out.write(afterFieldElement + "\n");
                    out.write(afterField + "\n");
                    out.write("</mak:editForm>\n");
                }
                out.write("<!-- Makumba Generator - END OF NORMAL FIELDS -->\n\n");
            }

            // if newForm, we also generate an addForm page with all the possible sets which will have to be added
            if (type == NEWFORM) {
                File f = new File(getFileNameFromObject(object, ADDFORM));

                generateFile(f, ADDFORM, object, action, processData, header, footer, beforeForm, afterForm,
                        beforeField, afterField, beforeFieldElement, afterFieldElement);
            } else {
                // iterating over the sets
                out.write("\n<!-- Makumba Generator - START OF SETS -->\n");
                System.out.println("DEBUG INFO: Number of sets of MDD " + object + " is " + sets.size());
                for (int i = 0; i < sets.size(); i++) {
                    FieldDefinition fd = (FieldDefinition) sets.get(i);
                    System.out.println("DEBUG INFO: Currently processing set with fieldname " + fd.getName()
                            + " and type " + fd.getType());

                    DataDefinition dd = null;
                    Vector[] extractInnerFields = null;

                    dd = getDataDefinitionFromType(fd, dd);

                    // sorting out only the normal fields, we don't generate internal sets.
                    extractInnerFields = extractInnerFields(dd);
                    Vector innerFields = extractInnerFields[0];
                    System.out.println("DEBUG INFO: Number of inner fields of MDD " + object + ", subset "
                            + dd.getName() + " is " + innerFields.size());

                    if (type == ADDFORM) {
                        out.write("\n<!-- Makumba Generator - START ADDFORM FOR FIELD " + fd.getName() + " -->\n");
                        out.write("<c:set var=\"pointer\" value=\"o\"/>\n");
                        out.write("<mak:addForm object=\"o\" field=\"" + fd.getName() + "\" action=\""
                                + file.getName() + "?pointer=${pointer}\" method=\"post\">\n");
                    }
                    if (type == EDITFORM) {
                        out.write("\n<!-- Makumba Generator - START EDITFORM FOR FIELD " + fd.getName() + " -->\n");
                        out.write("<mak:editForm object=\"o." + fd.getName() + "\" action=\"" + file.getName()
                                + "?pointer=${pointer}\" method=\"post\">\n");
                    }
                    if (type == LIST) {
                        out.write("<mak:list from=\"o." + fd.getName() + " o1\">\n");
                    }

                    // launching recursive generation of inner fields
                    // requires to have a Vector of fields and sets which will be processed
                    Vector fieldsAndSets = new Vector();
                    fieldsAndSets.addAll(extractInnerFields[0]);
                    fieldsAndSets.addAll(extractInnerFields[1]);
                    
                    String fieldPath = "o.";
                    HashSet processedDds = new HashSet();
                    generateInnerFields(type, beforeField, afterField, beforeFieldElement, afterFieldElement, out, fd,
                            dd, fieldsAndSets, fieldPath, processedDds);

                    // closing forms
                    if (type == ADDFORM) {
                        out.write("\n" + beforeField + "\n");
                        out.write(beforeFieldElement);
                        out.write("<input type=\"submit\" name=\"Add\">");
                        out.write(afterFieldElement + "\n");
                        out.write(afterField + "\n");
                        out.write("</mak:addForm>\n");
                        out.write("\n<!-- Makumba Generator - END ADDFORM FOR FIELD " + fd.getName() + " -->\n");
                    }
                    if (type == EDITFORM) {
                        out.write("\n" + beforeField + "\n");
                        out.write(beforeFieldElement);
                        out.write("<input type=\"submit\" name=\"Submit\">");
                        out.write(afterFieldElement + "\n");
                        out.write(afterField + "\n");
                        out.write("</mak:editForm>\n");
                        out.write("\n<!-- Makumba Generator - END EDITFORM FOR FIELD " + fd.getName() + " -->\n");
                    }
                    if (type == LIST) {
                        out.write("</mak:list>\n");
                    }

                } // end iterating over the sets
                out.write("<!-- Makumba Generator - END OF SETS -->\n\n");
            }

            // closing forms
            if (type == NEWFORM) {
                out.write("\n" + beforeFieldElement);
                out.write("<input type=\"submit\" name=\"Submit\">");
                out.write(afterFieldElement + "\n");
                out.write("</mak:newForm>\n");
                out.write("<!-- Makumba Generator - END OF NEW PAGE -->\n");
            }
            if (type == ADDFORM) {
                out.write("</mak:object>\n");
                out.write("<!-- Makumba Generator - END OF ADD PAGE -->\n");
            }
            if (type == LIST) {
                out.write("</mak:list>\n");
                out.write("<!-- Makumba Generator - END OF LIST PAGE -->\n");
            }
            if (type == EDITFORM) {
                out.write("<input type=\"submit\" name=\"Submit\">\n");
                out.write("</mak:editForm>\n");
                out.write("</mak:object>\n");
                out.write("<!-- Makumba Generator - END OF EDIT PAGE -->\n");
            }

            out.write(afterForm + "\n");
            out.write(footer + "\n");

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

    // generating the inner fields
    private static void generateInnerFields(int type, String beforeField, String afterField, String beforeFieldElement,
            String afterFieldElement, BufferedWriter out, FieldDefinition fd, DataDefinition dd, Vector innerFields, 
            String fieldPath, HashSet processedDds) throws IOException {

        // we must make sure that the field was not already processed before to avoid cyclic redundency
        // for this we store the combination (dataDefinition, fieldDefinition)
        
        //Object[] processedElement = { dd.getName() };

        if (processedDds.contains(fd))
            return;
        else
            processedDds.add(fd);

        if(type == EDITFORM) {
            fieldPath += fd.getName() + ".";
        }
        
        // fields
        // we start at 3 because before there is the field itself, TS_CREATE and TS_MODIFY
        for (int j = 3; j < innerFields.size(); j++) {
            FieldDefinition innerFd = dd.getFieldDefinition(j);
            String innerFieldPath = fieldPath + "." + innerFd.getName() + ".";

            System.out.println("DEBUG INFO: Inner set: generating field " + innerFd.getName() + " of type "
                    + innerFd.getType());

            if (type == ADDFORM) {
                out.write(beforeField + "\n");

                // name
                out.write(beforeFieldElement);
                out.write(innerFd.getName());
                out.write(afterFieldElement + "\n");

                //tag
                out.write(beforeFieldElement);
                out.write("<mak:input field=\"");
                //out.write(innerFd.getName()+" ");
                out.write(innerFieldPath);
                out.write("\"/>");
                out.write(afterFieldElement + "\n");
            }

            if (type == EDITFORM) {
                
                // generating inner sets if needed
                if ((isSet(innerFd) || isInternalSet(innerFd) || isPtr(innerFd))) {
                    System.out.println("DEBUG INFO: Inner set: launching recursive generation for field "+innerFd.getName() + " of type "+innerFd.getType());
                    DataDefinition innerDd = null;
                    Vector[] extractInnerFields = null;

                    innerDd = getDataDefinitionFromType(innerFd, dd);

                    extractInnerFields = extractInnerFields(innerDd);
                    Vector internalInnerFields = extractInnerFields[0];
                    
                    //fieldPath += innerFd.getName() + ".";

                    out.write("<!-- Makumba generator - GENERATED FIELDS OUT OF FIELD "+innerFd.getName()+" -->\n");
                    generateInnerFields(type, beforeField, afterField, beforeFieldElement, afterFieldElement, out,
                            innerFd, innerDd, internalInnerFields, fieldPath, processedDds);
                    out.write("<!-- Makumba generator - END OF GENERATED FIELDS OUT OF FIELD "+innerFd.getName()+" -->\n");
                    

                } else {
                    out.write(beforeField + "\n");

                    // name
                    out.write(beforeFieldElement);
                    out.write(innerFd.getName());
                    out.write(afterFieldElement + "\n");

                    // tag
                    out.write(beforeFieldElement);
                    out.write("<mak:input field=\"");
                    out.write(fieldPath + innerFd.getName());
                    out.write("\"/>");
                    out.write(afterFieldElement + "\n");
                }

            }
            // TODO add innerField lookup as for EDITFIELD
            if (type == LIST) {
                out.write(beforeField + "\n");

                // name
                out.write(beforeFieldElement);
                out.write(innerFd.getName());
                out.write(afterFieldElement + "\n");

                // tag
                out.write(beforeFieldElement);
                out.write("<mak:value expr=\"");
                out.write("o1." + innerFd.getName());
                out.write("\"/>");
                out.write(afterFieldElement + "\n");
            }

            out.write(afterField + "\n");
        }
        
        // sets
    }

    private static Vector[] extractInnerFields(DataDefinition dd) {
//      we build a fake extractInnerFields structure
        Vector[] extractInnerFields = new Vector[2];
        
        try {
            
            // we need to gather all kind of fields, may the be normal sets or fields, in the same vector
            // therefore we build a fake Vector[] structure
            
            Vector innerFields = new Vector();
        
            for (int i = 0; i < dd.getFieldNames().size(); i++) {
                innerFields.add(dd.getFieldDefinition(i));
            }
            
            extractInnerFields[0] = innerFields;
            extractInnerFields[1] = new Vector();
            
    
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return extractInnerFields;
    }

    private static boolean isSet(FieldDefinition fd) {
        return fd.getType().equals("set");
    }

    private static boolean isInternalSet(FieldDefinition fd) {
        return fd.getType().equals("setComplex") || fd.getType().equals("setintEnum")
                || fd.getType().equals("setcharEnum");
    }

    private static boolean isPtr(FieldDefinition fd) {
        return fd.getType().equals("ptr");
    }

    private static DataDefinition getDataDefinitionFromType(FieldDefinition fd, DataDefinition dd) {
        if (isInternalSet(fd))
            dd = fd.getSubtable();
        if (isSet(fd))
            dd = fd.getDataDefinition();
        if (isPtr(fd))
            dd = fd.getForeignTable();
        return dd;
    }

    public static void main(String[] args) {
        //String object = "general.Person";
        //String object = "best.internal.prteam.CityGuide";
        String object = "best.bcc.Offer";
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
        File fn = new File(getFileNameFromObject(object, NEWFORM));
        generateFile(fn, NEWFORM, object, getFileNameFromObject(object, NEWFORM), extractedFields, header, footer,
                beforeForm, afterForm, beforeField, afterField, beforeFieldElement, afterFieldElement);

        // editForm
        File fe = new File(getFileNameFromObject(object, EDITFORM));
        generateFile(fe, EDITFORM, object, getFileNameFromObject(object, EDITFORM), extractedFields, header, footer,
                beforeForm, afterForm, beforeField, afterField, beforeFieldElement, afterFieldElement);

        // list
        File fl = new File(getFileNameFromObject(object, LIST));
        generateFile(fl, LIST, object, getFileNameFromObject(object, LIST), extractedFields, header, footer,
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
