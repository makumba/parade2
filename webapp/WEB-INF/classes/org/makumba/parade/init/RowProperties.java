package org.makumba.parade.init;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class RowProperties {

    private Properties state;

    public Map rowDefinitions = new HashMap();

    public RowProperties() {
        
        /* for development purposes

        this.addRowDefinition("(root)", ".", "webapp", "ParaDe webapp");
        this.addRowDefinition("test2-k", "../parade", "", "the old parade row");
        this.addRowDefinition("manu-k", "E:\\bundle\\sources\\karamba", "public_html", "manu messing it all up again");
        */
        readRowDefinitions();
    }

    
    /* Get Row definitions */
    public Map getRowDefinitions() {
        return this.rowDefinitions;

    }

    /* Add a row definition */
    public void addRowDefinition(String name, String path, String webapp, String description) {
        Map row = new HashMap();
        row.put("name", name);
        row.put("path", path);
        row.put("webapp", webapp);
        row.put("desc", description);

        rowDefinitions.put(name, row);

    }

    /* Delete row definition */
    public void delRowDefinition() {

    }

    public void setRowDefinitions(Map rowStoreProperties) {
        this.rowDefinitions = rowStoreProperties;
    }

    /* reads row definition from properties file */
    public void readRowDefinitions() {
        java.io.File f = new java.io.File(ParadeProperties.getParadeBase() + java.io.File.separator + "rows.properties");

        state = new Properties();
        try {
            if (f.exists())
                state.load(new FileInputStream(f));
            else {
                state.setProperty("", ParadeProperties.getParadeBase());
                state.store(new FileOutputStream(f), 
                        "rows\n"
                        + "# example:\n"
                        + "# <name_appl>=<path, e.g. ..\\iplabWeb>\n"
                        + "# rowdata.<name_appl>.obs=<space for notes>\n"
                        + "# rowdata.<name_appl>.webapp=<relative path to the context, e.g. 'public_html'>\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        
        for(Enumeration e= state.keys(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            if (!key.startsWith("rowdata.")) {
                extractRowDefinitions(key);
                
            }    
        }
    }

    private void extractRowDefinitions(String name) {
        
        String propName = "rowdata." + name + ".";
        String obs="", webapp="";
        for (Enumeration e = state.keys(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            
            if(key.startsWith(propName + "obs"))
                obs = state.getProperty(propName + "obs");
            if(key.startsWith(propName + "webapp"))
                webapp = state.getProperty(propName + "webapp");
        }
        
        if(name.equals("")) {
            this.addRowDefinition("(root)", state.getProperty(name), ParadeProperties.getProperty("webapp.path"), "ParaDe webapp");
        } else {
            this.addRowDefinition(name, state.getProperty(name), webapp, obs);
        }
        
    }

}
