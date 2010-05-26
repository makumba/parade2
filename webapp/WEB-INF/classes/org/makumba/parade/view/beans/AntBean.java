package org.makumba.parade.view.beans;

import java.util.Iterator;

import org.makumba.parade.init.ParadeProperties;

/**
 * Bean that provides data for the file browser view
 * 
 * @author Koen Speelmeijer
 * 
 */

public class AntBean {
    
    protected String allowedOperations = new String();

    public String getAllowedAntOperations() {
        
        if(allowedOperations.length() == 0) {
            
            for (Iterator<String> iterator = ParadeProperties.getElements("ant.displayedOps").iterator(); iterator.hasNext();) {
                String allowed = iterator.next();
                if(allowed != null && allowed != "null" && allowed.length() > 0) {
                    if(allowed.startsWith("#")) {
                        allowed = allowed.substring(1);
                    }
                    allowedOperations += "'"+allowed+"'";
                    
                    if(iterator.hasNext()) allowedOperations +=",";
                }
            }
        }
        return allowedOperations;

    
    }
}
