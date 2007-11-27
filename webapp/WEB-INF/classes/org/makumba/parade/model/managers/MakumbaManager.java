package org.makumba.parade.model.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowMakumba;
import org.makumba.parade.model.RowWebapp;
import org.makumba.parade.model.interfaces.ParadeManager;
import org.makumba.parade.model.interfaces.RowRefresher;


public class MakumbaManager implements RowRefresher, ParadeManager {
    
    private static Logger logger = Logger.getLogger(MakumbaManager.class.getName());

    public void rowRefresh(Row row) {
        logger.debug("Refreshing row information for row "+row.getRowname());

        RowMakumba makumbadata = new RowMakumba();
        makumbadata.setDataType("makumba");
        
        // if this is the ParaDe row, there's no Makumba
        if(row.getRowname().equals("(root)")) {
            makumbadata.setVersion("No makumba.jar");
            makumbadata.setDb("No MakumbaDatabase.properties");
        } else {
            String root = row.getRowpath() + File.separator + ((RowWebapp) row.getRowdata().get("webapp")).getWebappPath();
            makumbadata.setVersion(getMakumbaVersion(root));
            makumbadata.setDb(getMakumbaDatabase(root));
        }

        row.addManagerData(makumbadata);
    }

    public String getMakumbaVersion(String p) {
        final String path = p;
        String version = "unknown";

        try {
            java.io.File fl = new java.io.File((path + "/WEB-INF/lib/makumba.jar").replace('/',
                    java.io.File.separatorChar));
            
            if(!fl.exists()) return "No makumba.jar";
            
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
    
    public String getMakumbaDatabase(String root) {
        
        root = (root + "/WEB-INF/classes/").replace('/', File.separatorChar);
        File f = new File(root + "MakumbaDatabase.properties");
        if (!f.exists())
            return "No MakumbaDatabase.properties found";
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(f));
        } catch (IOException e) {
            return "Invalid MakumbaDatabase.properties";
        }
        return "Default database: "+(String)p.get("default");
    }
        
     

    public void newRow(String name, Row r, Map m) {
        // TODO Auto-generated method stub

    }

}
