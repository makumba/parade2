package org.makumba.parade.view.beans;

import java.io.File;
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
import org.makumba.parade.model.RowWebapp;
import org.makumba.parade.model.managers.ServletContainer;

/**
 * Bean that provides data for the file browser view
 * 
 * @author Manuel Gay
 * 
 */
public class FileBrowserBean {

    private Row row;

    private String path;
    
    private RowWebapp webappdata;


    public void setContext(String context) {

        Session s = null;
        try {
            s = InitServlet.getSessionFactory().openSession();
            Transaction tx = s.beginTransaction();
            row = (Row) s.createQuery("from Row r where r.rowname = :context").setString("context", context)
                    .uniqueResult();
            if (row == null) {
                throw new RuntimeException("Could not find row " + context);
            } else {
                webappdata = (RowWebapp) row.getRowdata().get("webapp");
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

    public String getAbsolutePath() {
        String absolutePath = "";
        if (path.equals("/"))
            absolutePath = row.getRowpath();
        else
            absolutePath = row.getRowpath() + java.io.File.separator + path.replace('/', java.io.File.separatorChar);
        if (absolutePath.endsWith(java.io.File.separator))
            absolutePath = absolutePath.substring(0, absolutePath.length() - 1);

        return absolutePath.replace(File.separator, "/");
    }

    public String getAbsoluteRowPath() {
        return (new java.io.File(row.getRowpath()).getAbsolutePath()).replace(File.separator, "/");
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
    
    public String getFileIcon(String fl) {
        // icons
        fl = fl.toLowerCase();
        String image = "unknown";

        if (fl.endsWith(".java"))
            image = "java";
        if (fl.endsWith(".mdd") || fl.endsWith(".idd"))
            image = "text";
        if (fl.endsWith(".jsp") || fl.endsWith(".properties") || fl.endsWith(".xml") || fl.endsWith(".txt")
                || fl.endsWith(".conf"))
            image = "text";
        if (fl.endsWith(".doc") || fl.endsWith(".jsp") || fl.endsWith(".html") || fl.endsWith(".htm")
                || fl.endsWith(".rtf"))
            image = "layout";
        if (fl.endsWith(".gif") || fl.endsWith(".png") || fl.endsWith(".jpg") || fl.endsWith(".jpeg"))
            image = "image";
        if (fl.endsWith(".zip") || fl.endsWith(".gz") || fl.endsWith(".tgz") || fl.endsWith(".jar"))
            image = "zip";
        if (fl.endsWith(".avi") || fl.endsWith(".mpg") || fl.endsWith(".mpeg") || fl.endsWith(".mov"))
            image = "movie";
        if (fl.endsWith(".au") || fl.endsWith(".mid") || fl.endsWith(".vaw") || fl.endsWith(".mp3"))
            image = "sound";

        return image;
    }
    
    public String getFileLinkAddress(String fileName) {
        // name
        String addr = "";
        String webappPath = webappdata.getWebappPath();
        String fileNameNormal = fileName;
        fileName = fileName.toLowerCase();

        if (webappdata.getStatus().intValue() == ServletContainer.RUNNING && path.startsWith(webappPath)) {

            String pathURI = path.substring(path.indexOf(webappPath) + webappPath.length()).replace(
                    java.io.File.separatorChar, '/')
                    + "/";

            if (fileName.endsWith(".java")) {
                String dd = pathURI + fileNameNormal;
                dd = dd.substring(dd.indexOf("classes") + 8, dd.lastIndexOf(".")).replace('/', '.');
                addr = "/" + row.getRowname() + "/classes/" + dd;
            }
            if (fileName.endsWith(".mdd") || fileName.endsWith(".idd")) {
                String dd = pathURI + fileNameNormal;
                dd = dd.substring(dd.indexOf("dataDefinitions") + 16, dd.lastIndexOf(".")).replace('/', '.');
                addr = "/" + row.getRowname() + "/dataDefinitions/" + dd;
            }
            if (fileName.endsWith(".jsp") || fileName.endsWith(".html") || fileName.endsWith(".htm") || fileName.endsWith(".txt")
                    || fileName.endsWith(".gif") || fileName.endsWith(".png") || fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")
                    || fileName.endsWith(".css") || fileName.startsWith("readme"))
                addr = "/" + row.getRowname() + pathURI + fileNameNormal;

            if (fileName.endsWith(".jsp"))
                addr += "x";
        }
        
        return addr;

    }
    
    public String getEncodedPath(String filePath, String fileName) {
        String pathEncoded = "";
        try {
            // we encode the path twice, because of the javascript that uses it
            pathEncoded = URLEncoder.encode(URLEncoder.encode(filePath.substring(row.getRowpath().length() + 1), "UTF-8"), "UTF-8");
//            nameEncoded = URLEncoder.encode(f.getName(), "UTF-8");
//            fileURIEncoded = URLEncoder.encode(f.getFileURI(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return pathEncoded;
    }
    
    public String encode(String string) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(string, "UTF-8");
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoded;
    }
}
