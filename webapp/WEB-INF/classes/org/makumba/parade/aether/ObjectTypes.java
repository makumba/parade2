package org.makumba.parade.aether;

import java.util.HashSet;
import java.util.Set;

public enum ObjectTypes {
    
    FILE("file://"), DIR("dir://"), ROW("row://"), USER("user://"), CVSFILE("cvs://"), PARADE("parade://");
    
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
     
     public static String fileFromRow(String fileURL) {
        return "row://"+fileURL.substring("file://".length(), fileURL.substring("file://".length()).indexOf("/"));
     }
     
     public static String typeFromURL(String URL) {
         for(ObjectTypes o : values()) {
             if(URL.startsWith(o.prefix)) {
                 return o.toString();
             }
         }
         return "UNKNOWN";
     }
     
     public static String fileToCVSURL(String fileURL) {
         if(fileURL.startsWith(FILE.prefix())) {
             return CVSFILE.prefix() + fileURL.substring(FILE.prefix().length());
         } else {
             return fileURL;
         }
     }

}
