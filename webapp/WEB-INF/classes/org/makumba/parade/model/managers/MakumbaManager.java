package org.makumba.parade.model.managers;

import java.io.File;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowMakumba;
import org.makumba.parade.model.interfaces.ParadeManager;
import org.makumba.parade.model.interfaces.RowRefresher;


public class MakumbaManager implements RowRefresher, ParadeManager {

    final static int NEWFORM = 0;

    final static int ADDFORM = 10;

    final static int LIST = 20;

    final static int EDITFORM = 30;

    public void rowRefresh(Row row) {
        RowMakumba makumbadata = new RowMakumba();
        makumbadata.setDataType("makumba");
        
        // if this is the ParaDe row, there's no Makumba
        if(row.getRowname().equals("(root)")) {
            makumbadata.setVersion("No makumba.jar");
        } else {
            String root = row.getRowpath() + File.separator + "public_html";
            makumbadata.setVersion(getMakumbaVersion(root));
        }

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
            // may be true for old Makumba versions, but they aren't used anymore
            e.printStackTrace();
        }
        return "Error detecting Makumba version";
    }

    public void newRow(String name, Row r, Map m) {
        // TODO Auto-generated method stub

    }

}
