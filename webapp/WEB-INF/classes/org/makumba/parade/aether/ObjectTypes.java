package org.makumba.parade.aether;

import java.util.HashSet;
import java.util.Set;

public enum ObjectTypes {

    FILE("file://", "file"), DIR("dir://", "directory"), ROW("row://", "row"), USER("user://", "user"), CVSFILE("cvs://", "cvs file"), PARADE("parade://", "parade");

    public static Set<String> getObjectTypes() {
        ObjectTypes[] v = values();
        Set<String> res = new HashSet<String>();
        for (int i = 0; i < v.length; i++) {
            res.add(v[i].toString());
        }
        return res;
    }

    private String prefix;
    
    private String readableType;

    ObjectTypes(String prefix, String readableType) {
        this.prefix = prefix;
        this.readableType = readableType;
    }

    public String prefix() {
        return this.prefix;
    }
    
    public String readableType() {
        return this.readableType;
    }

    /**
     * file://rudi-k/lbg/member.jsp -> rudi-k
     * 
     * very ugly code. but I'm too tired
     */
    public static String rowNameFromURL(String URL) {
        int n = URL.indexOf(":");
        if (n == -1) {
            return "";
        }
        if (!(URL.charAt(++n) == '/')) {
            return "";
        }
        if (!(URL.charAt(++n) == '/')) {
            return "";
        }
        n++;
        URL = URL.substring(n);
        return (URL.indexOf("/") > -1 ? URL.substring(0, URL.indexOf("/")) : URL);
    }

    /**
     * ???
     */
    public static String fileFromRow(String fileURL) {
        return "row://" + fileURL.substring("file://".length(), fileURL.substring("file://".length()).indexOf("/"));
    }

    /**
     * file://rudi-k/lbg/member.jsp -> member.jsp
     * dir://rudi-k/ --> /
     */
    public static String objectNameFromURL(String objectURL) {
        String name = objectURL.substring(objectURL.lastIndexOf("/") + 1);
        return name.length() > 0 ? name : "/";
    }

    /**
     * file://rudi-k/lbg/member.jsp -> lbg/
     */
    public static String pathFromFileOrDirURL(String fileOrDirURL) {
        String path = fileOrDirURL.substring( (fileOrDirURL.startsWith(ObjectTypes.FILE.prefix()) ? ObjectTypes.FILE.prefix().length() : ObjectTypes.DIR.prefix().length()));
        path = path.substring(path.indexOf("/") + 1);
        if(path.indexOf("/") > -1)
            path = path.substring(0, path.lastIndexOf("/"));
        return path;
    }

    /**
     * file://rudi-k/lbg/member.jsp -> lbg/member.jsp
     */
    public static String fileOrDirPathFromFileOrDirURL(String fileOrDirURL) {
        String path = fileOrDirURL.substring( (fileOrDirURL.startsWith(ObjectTypes.FILE.prefix()) ? ObjectTypes.FILE.prefix().length() : ObjectTypes.DIR.prefix().length()));
        path = path.substring(path.indexOf("/") + 1);
        return path;
    }

    /**
     * file://rudi-k/lbg/member.jsp -> FILE
     */
    public static String typeFromURL(String URL) {
        for (ObjectTypes o : values()) {
            if (URL.startsWith(o.prefix)) {
                return o.toString();
            }
        }
        return "UNKNOWN";
    }
    
    public static ObjectTypes getObjectType(String objectURL) {
        for(ObjectTypes objectType : values()) {
            if(objectURL.startsWith(objectType.prefix())) {
                return objectType;
            }
        }
        return null;
    }

}
