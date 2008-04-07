package org.makumba.parade.init;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ApplicationProperties {

    private Properties state;
    
    private static String DEFAULT_ROWSFILE = "/applications.properties";

    public Map<String, Map<String, String>> applicationDefinitions = new HashMap<String, Map<String, String>>();
    
    static Logger logger = Logger.getLogger(ApplicationProperties.class.getName());

    public ApplicationProperties() {
        
        readApplicationDefinitions();
    }

    
    /* Get Row definitions */
    public Map<String, Map<String, String>> getApplicationDefinitions() {
        return this.applicationDefinitions;

    }

    /* Add a row definition */
    public void addApplicationDefinition(String name, String description, String repository, String module) {
        Map<String, String> row = new HashMap<String, String>();
        row.put("name", name);
        row.put("desc", description);
        row.put("repository", repository);
        row.put("module", module);

        applicationDefinitions.put(name, row);

    }

    public void setApplicationDefinitions(Map<String, Map<String, String>> applicationDefinitions) {
        this.applicationDefinitions = applicationDefinitions;
    }

    /* reads application definition from properties file */
    public void readApplicationDefinitions() {
        
        state = new Properties();
        try {
            state.load(ApplicationProperties.class.getResourceAsStream(DEFAULT_ROWSFILE));
            
        } catch (Exception e) {
            // if there's no row definition file, we create one
            logger.warn("No application.properties file found, attempting to generate one");
            state.setProperty("", ":extssh:manuel_gay@parade.cvs.sf.net:/cvsroot/parade");
            try {
                state.store(new FileOutputStream(new java.io.File(ParadeProperties.getClassesPath() + java.io.File.separator + "applications.properties")), 
                        "applications\n"
                        + "# example:\n"
                        + "# <name_appl>=<repository, e.g. pserver:parade@cvs.best.eu.org:/usr/local/cvsroot>\n"
                        + "# appdata.<name_appl>.obs=<space for notes>\n"
                        + "# appdata.<name_appl>.moduke=<CVS module, e.g. karamba>\n"
                        );
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
    }
        
        for(Enumeration e= state.keys(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            if (!key.startsWith("appdata.")) {
                extractRowDefinitions(key);
                
            }    
        }
    }

    /* Extracts the definition of one row */
    private void extractRowDefinitions(String name) {
        
        String propName = "appdata." + name + ".";
        String obs="", module="";
        for (Enumeration e = state.keys(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            
            if(key.startsWith(propName + "obs"))
                obs = state.getProperty(propName + "obs");
            
            if(key.startsWith(propName + "module"))
                module = state.getProperty(propName + "module");
        }
        
        // if we don't find anything, add as default the ParaDe application
        if(name.equals("")) {
            this.addApplicationDefinition("parade", "ParaDe itself", "extssh:manuel_gay@parade.cvs.sf.net:/cvsroot/parade", "parade");
        } else {
            this.addApplicationDefinition(name, obs, state.getProperty(name), module);
        }
        
    }

}
