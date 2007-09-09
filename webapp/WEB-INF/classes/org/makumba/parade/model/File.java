package org.makumba.parade.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.managers.CVSManager;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.model.managers.TrackerManager;

public class File {

    private Long id;

    private boolean isDir;

    private boolean onDisk;

    private String name;

    private Long date;

    private Long size;

    private Map filedata = new HashMap();

    private Row row;

    private String path;
    
    private String parentPath;


    /* Calls the refresh() directoryRefresh() on the directory managers */
    public void refresh() {
        FileManager fileMgr = new FileManager();
        CVSManager CVSMgr = new CVSManager();
        TrackerManager trackerMgr = new TrackerManager();

        fileMgr.directoryRefresh(row, this.getPath(), false);
        CVSMgr.directoryRefresh(row, this.getPath(), false);
        trackerMgr.directoryRefresh(row, this.getPath(), false);
    }
    
    /* Calls the refresh() directoryRefresh() on the directory managers locally */
    public void localRefresh() {
        FileManager fileMgr = new FileManager();
        CVSManager CVSMgr = new CVSManager();
        TrackerManager trackerMgr = new TrackerManager();

        fileMgr.directoryRefresh(row, this.getPath(), true);
        CVSMgr.directoryRefresh(row, this.getPath(), true);
        trackerMgr.directoryRefresh(row, this.getPath(), true);
    }
    
    public void setPath(String p) {
        this.path = p;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAge() {
        return new Long(new Date().getTime() - this.date.longValue());
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Map getFiledata() {
        return filedata;
    }

    public void setFiledata(Map filedata) {
        this.filedata = filedata;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsDir() {
        return isDir;
    }

    public void setIsDir(boolean isDir) {
        this.isDir = isDir;
    }

    public String getPath() {
        return this.path;
    }
    
    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }
    
    public boolean getOnDisk() {
        return onDisk;
    }

    public void setOnDisk(boolean onDisk) {
        this.onDisk = onDisk;
    }

    /* returns a List of the keys of the subdirs of a given path */
    public List getSubdirs(Session s) {
        
        String keyPath = path.replace(java.io.File.separatorChar, '/');
        
        String absoulteRowPath = (new java.io.File(row.getRowpath()).getAbsolutePath());
        if (keyPath == null || keyPath == "")
            keyPath = absoulteRowPath.replace(java.io.File.separatorChar, '/');
        
        List children = null;
        
        Query q = s.createQuery("from File f where f.parentPath = :keyPath and f.isDir = true and f.row.rowname = :rowname");
        q.setCacheable(true);
        q.setString("keyPath", keyPath);
        q.setString("rowname", row.getRowname());
        
        children = q.list();
        
        /*

        Set keySet = this.getRow().getFiles().keySet();

        List children = new LinkedList();

        for (Iterator i = keySet.iterator(); i.hasNext();) {
            String currentKey = (String) i.next();
            boolean isChild = currentKey.startsWith(keyPath);
            if (isChild) {
                boolean isNotRoot = currentKey.length() - keyPath.length() > 0;
                if (isNotRoot) {
                    String childKey = currentKey.substring(keyPath.length() + 1, currentKey.length());
                    boolean isDirectChild = childKey.indexOf(java.io.File.separator) == -1;
                    if (isDirectChild) {
                        File f = (File) this.getRow().getFiles().get(currentKey);
                        if (f.getIsDir()) {
                            children.add(f);
                        }
                    }
                }
            }
        }
        
        */

        return children;

    }

    /* returns a List of the direct children (files, dirs) of a given Path */
    public List getChildren() {
        String keyPath = this.getPath().replace(java.io.File.separatorChar, '/');
        
        String absoulteRowPath = (new java.io.File(row.getRowpath()).getAbsolutePath());
        if (keyPath == null || keyPath == "")
            keyPath = absoulteRowPath.replace(java.io.File.separatorChar, '/');
        
        List children = null;
        
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        
        Query q = s.createQuery("from File f where f.parentPath = :keyPath and f.row.rowname = :rowname");
        q.setCacheable(true);
        q.setString("keyPath", keyPath);
        q.setString("rowname", row.getRowname());
        
        children = q.list();
        
        tx.commit();
        s.close();
        
        /*
        

        Set keySet = this.getRow().getFiles().keySet();

        List children = new LinkedList();

        for (Iterator i = keySet.iterator(); i.hasNext();) {
            String currentKey = (String) i.next();
            boolean isChild = currentKey.startsWith(keyPath);
            if (isChild) {
                boolean isNotRoot = currentKey.length() - keyPath.length() > 0;
                if (isNotRoot) {
                    String childKey = currentKey.substring(keyPath.length() + 1, currentKey.length());
                    boolean isDirectChild = childKey.indexOf(java.io.File.separator) == -1;
                    if (isDirectChild) {
                        children.add(this.getRow().getFiles().get(currentKey));
                    }
                }
            }
        }
        */

        //return children;
       return children;
    }
    
    public String getRelativePath() {
        return path.substring(getRow().getRowpath().length() + 1);
    }

}
