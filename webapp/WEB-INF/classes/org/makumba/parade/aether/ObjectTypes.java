package org.makumba.parade.aether;

import java.util.HashSet;
import java.util.Set;

public enum ObjectTypes {
    
    FILE("file:/"), DIR("dir:/"), ROW("row:/"), USER("user:/"), CVSFILE("cvs:/");
    
    public static Set<String> getObjectTypes() {
        ObjectTypes[] v = values();
        Set<String> res = new HashSet<String>();
        for (int i = 0; i < v.length; i++) {
            res.add(v[i].toString());
        }
        return res;
    }

    private String prefix;
    
     ObjectTypes(String prefix) {
        this.prefix = prefix;
    }
     
     public String prefix() {
         return this.prefix;
     }

}
