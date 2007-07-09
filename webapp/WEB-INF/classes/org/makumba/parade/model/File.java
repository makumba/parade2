package org.makumba.parade.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /* Calls the refresh() directoryRefresh() on the directory managers */
    public void refresh() {
        FileManager fileMgr = new FileManager();
        CVSManager CVSMgr = new CVSManager();
        TrackerManager trackerMgr = new TrackerManager();

        fileMgr.directoryRefresh(row, this.getPath(), false);
        CVSMgr.directoryRefresh(row, this.getPath(), false);
        trackerMgr.directoryRefresh(row, this.getPath(), false);
    }

    public void setPath(String path) {
        this.path = path;
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
        return path;
    }

    public boolean getOnDisk() {
        return onDisk;
    }

    public void setOnDisk(boolean onDisk) {
        this.onDisk = onDisk;
    }

    /* returns a List of the keys of the subdirs of a given path */
    public List getSubdirs() {
        String keyPath = this.getPath();

        String absoulteRowPath = (new java.io.File(this.getRow().getRowpath()).getAbsolutePath());
        if (keyPath == null || keyPath == "")
            keyPath = absoulteRowPath;
        keyPath = keyPath.replace('/', java.io.File.separatorChar);

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

        return children;

    }

    /* returns a List of the direct children (files, dirs) of a given Path */
    public List getChildren() {
        String keyPath = this.getPath();

        String absoulteRowPath = (new java.io.File(this.getRow().getRowpath()).getAbsolutePath());
        if (keyPath == null || keyPath == "")
            keyPath = absoulteRowPath;
        keyPath = keyPath.replace('/', java.io.File.separatorChar);

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

        return children;
    }
    
    public String getRelativePath() {
        return (path.substring(row.getRowpath().length() + 1).replace(java.io.File.separator, "/"));
    }

}
