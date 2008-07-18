package org.makumba.parade.view.beans;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Row;

/**
 * Bean that provides data for the file browser view
 * 
 * @author Manuel Gay
 * 
 */
public class FileBrowserBean {

    private Row row;

    private String path;

    public void setContext(String context) {

        Session s = null;
        try {
            s = InitServlet.getSessionFactory().openSession();
            Transaction tx = s.beginTransaction();
            row = (Row) s.createQuery("from Row r where r.rowname = :context").setString("context", context)
                    .uniqueResult();
            if (row == null) {
                throw new RuntimeException("Could not find row " + context);
            }

        } finally {
            if (s != null)
                s.close();
        }
    }

    public void setPath(String path) {
        // if this is the root of the row
        if (path == null || path.equals("null"))
            path = "/";

        this.path = path.replace(java.io.File.separatorChar, '/');
    }
    
    public String getPath() {
        return this.path;
    }
    
    public String getPathEncoded() {
        String pathEncoded = "";

        try {
            pathEncoded = URLEncoder.encode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return pathEncoded;
    }

    public List<HashMap<String, String>> getParentDirs() {

        if (path == null)
            path = "/";

        List<HashMap<String, String>> parentDirs = new LinkedList<HashMap<String, String>>();
        String currentPath = "";

        StringTokenizer st = new StringTokenizer(path, "/");
        while (st.hasMoreTokens()) {
            HashMap<String, String> parentDir = new HashMap<String, String>();

            String thisDir = st.nextToken();
            currentPath += thisDir;
            if (st.hasMoreElements())
                currentPath += "/";
            parentDir.put("path", currentPath);
            parentDir.put("directoryName", thisDir);
            parentDirs.add(parentDir);
        }
        return parentDirs;
    }

    public String getPathOnDisk() {
        return row.getRowpath()
                + (path.length() > 1 ? java.io.File.separator + path.replace('/', java.io.File.separatorChar) : "");
    }

}
