package org.makumba.parade.managers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.makumba.parade.ifc.RowRefresher;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowCVS;
import org.makumba.parade.model.RowMakumba;
import org.makumba.parade.model.RowWebapp;

public class MakumbaManager implements RowRefresher {
	
	static {
        // force a class load
        Class c = org.makumba.MakumbaSystem.class;
    }

	public void rowRefresh(Row row) {
		RowMakumba makumbadata = new RowMakumba();
		makumbadata.setDataType("makumba");
		
        String root = row.getRowpath() + File.separator + "public_html";
        makumbadata.setVersion(getMakumbaVersion(root));
        		
		RowStoreManager rowMgr = new RowStoreManager();
		rowMgr.addManagerData(makumbadata,row);
	}
	
	public String getMakumbaVersion(String p) {
        final String path = p;
        String version = "unknown";

        try {
        	java.io.File fl = new java.io.File((path + "/WEB-INF/lib/makumba.jar").replace('/', java.io.File.separatorChar));
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
                public Class findClass(String name)
                        throws ClassNotFoundException {
                    try {
                        File fl = new File((path + "/WEB-INF/lib/makumba.jar").replace('/', File.separatorChar));
                        if (!fl.exists()) throw new ClassNotFoundException("Jar file " + fl + " not found");
                        JarFile f = new JarFile(fl);
                        String nm = name.replace('.', '/') + ".class";
                        JarEntry j = f.getJarEntry(nm);
                        InputStream i = f.getInputStream(j);
                        ByteArrayOutputStream bo = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int n;
                        while ((n = i.read(buffer)) != -1) bo.write(buffer, 0, n);
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
    
}
