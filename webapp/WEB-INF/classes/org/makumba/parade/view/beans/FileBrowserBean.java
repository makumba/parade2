package org.makumba.parade.view.beans;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.init.ParadeProperties;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowCVS;
import org.makumba.parade.model.RowWebapp;
import org.makumba.parade.model.managers.CVSManager;
import org.makumba.parade.model.managers.ServletContainer;
import org.makumba.parade.tools.CVSRevisionComparator;

/**
 * Bean that provides data for the file browser view
 * 
 * @author Manuel Gay
 * 
 */
public class FileBrowserBean {

    private Row row;
    
    private RowCVS cvsdata;

    private String path;
    
    private String absolutePath;
    
    private RowWebapp webappdata;
    
    private CVSRevisionComparator c = new CVSRevisionComparator();

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
                cvsdata = (RowCVS) row.getRowdata().get("cvs");
            }
            tx.commit();

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

    /**
     * the path of the currently browsed directory
     */
    public String getPath() {
        return this.path;
    }
    
    /**
     * the path of a file/directory within the currently browsed directory
     */
    public String getPath(String fileName) {
        if(path.equals("/")) {
            return fileName;
        } else {
            return (path.endsWith("/") ? path : path + "/") + fileName;
        }
    }

    /**
     * UTF-8 encoded path of the currently browsed directory
     */
    public String getPathEncoded() {
        String pathEncoded = "";

        try {
            pathEncoded = URLEncoder.encode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return pathEncoded;
    }

    /**
     * absolue path on disk of the currently browsed directory
     */
    public String getAbsolutePath() {
        if(absolutePath == null) {
            if (path.equals("/"))
                absolutePath = row.getRowpath();
            else
                absolutePath = row.getRowpath() + java.io.File.separator + path.replace('/', java.io.File.separatorChar);
            if (absolutePath.endsWith(java.io.File.separator))
                absolutePath = absolutePath.substring(0, absolutePath.length() - 1);

            absolutePath = absolutePath.replace(File.separator, "/");
        }
        return absolutePath;
    }

    /**
     * absolute path of the browsed context
     */
    public String getAbsoluteRowPath() {
        return (new java.io.File(row.getRowpath()).getAbsolutePath()).replace(File.separator, "/");
    }

    /**
     * list of parent directories, containing name and relative path
     */
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
    
    /**
     * icons
     */
    public String getFileIcon(String fl) {
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
    
    /**
     * computes correct link to a file
     */
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
    
    /**
     * encodes a string 
     */
    public String encode(String string) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(string, "UTF-8");
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoded;
    }
    

    /**
     * CVS repository link 
     */
    public String getCVSWebLink(String filePath) {
        String cvsWebLink="";
        String cvsweb = ParadeProperties.getParadeProperty("cvs.site");
        String webPath = filePath.substring(row.getRowpath().length() + 1).replace(java.io.File.separatorChar, '/');
        cvsWebLink = cvsweb + cvsdata.getModule() + "/" + webPath;
        return cvsWebLink;
        
    }
    
    public boolean isCVSConflict(String fileName) {
        return fileName.startsWith(".#");
    }
    
    public boolean getCvsNewerExists(String fileName, String cvsRevision) {
        // let's see if there's a newer version of this on the repository
 
        boolean newerExists = false;
        String repositoryRevision = "", rowRevision = "";

        if (row.getApplication() != null) {
            String fileCvsPath = row.getApplication().getName() + getPath(fileName).substring(row.getRowpath().length());
            Hibernate.initialize(row.getApplication().getCvsfiles());
            repositoryRevision = row.getApplication().getCvsfiles().get(fileCvsPath);
            rowRevision = cvsRevision;

            if (repositoryRevision != null && rowRevision != null) {

                if (repositoryRevision.equals("1.1.1.1")) {
                    repositoryRevision = "1.1";
                }
                if (rowRevision.equals("1.1.1.1")) {
                    rowRevision = "1.1";
                }

                newerExists = c.compare(repositoryRevision, rowRevision) == 1;

            } else {
                return false;
            }
        }

        return newerExists;
        
    }
    
    public boolean isCvsConflictOnUpdate(String fileName) {
        return CVSManager.cvsConflictOnUpdate(fileName, getAbsolutePath());
    }
    
    public String getCvsNewRevision(String fileName) {
        String fileCvsPath = row.getApplication().getName() + getPath(fileName).substring(row.getRowpath().length());
        return row.getApplication().getCvsfiles().get(fileCvsPath);
    }
    
}
