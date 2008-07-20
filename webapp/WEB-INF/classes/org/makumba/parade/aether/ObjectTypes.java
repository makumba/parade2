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
        return URL.substring(0, URL.indexOf("/"));
    }

    /**
     * ???
     */
    public static String fileFromRow(String fileURL) {
        return "row://" + fileURL.substring("file://".length(), fileURL.substring("file://".length()).indexOf("/"));
    }

    /**
     * file://rudi-k/lbg/member.jsp -> member.jsp
     */
    public static String objectNameFromURL(String objectURL) {
        return objectURL.substring(objectURL.lastIndexOf("/") + 1);
    }

    /**
     * file://rudi-k/lbg/member.jsp -> lbg/
     */
    public static String pathFromFileURL(String fileURL) {
        String path = fileURL.substring("file://".length());
        path = path.substring(path.indexOf("/") + 1);
        if(path.indexOf("/") > -1)
            path = path.substring(0, path.lastIndexOf("/"));
        return path;
    }

    /**
     * file://rudi-k/lbg/member.jsp -> lbg/member.jsp
     */
    public static String filePathFromFileURL(String fileURL) {
        String path = fileURL.substring("file://".length());
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

}
