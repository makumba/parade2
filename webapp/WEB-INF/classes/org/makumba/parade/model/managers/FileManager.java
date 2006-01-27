package org.makumba.parade.model.managers;

import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.File;
import org.makumba.parade.model.FileCVS;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.interfaces.CacheRefresher;
import org.makumba.parade.model.interfaces.ParadeManager;
import org.makumba.parade.model.interfaces.RowRefresher;
import org.makumba.parade.tools.SimpleFileFilter;

public class FileManager implements RowRefresher, CacheRefresher, ParadeManager {

    static Logger logger = Logger.getLogger(FileManager.class.getName());

    private FileFilter filter = new SimpleFileFilter();

    /*
     * Creates a first File for the row which is its root dir and invokes its refresh() method
     */
    public void rowRefresh(Row row) {

        File root = new File();

        try {
            java.io.File rootPath = new java.io.File(row.getRowpath());
            root.setName("_root_");
            root.setPath(rootPath.getCanonicalPath());
            root.setRow(row);
            root.setDate(new Long(new java.util.Date().getTime()));
            root.setFiledata(new HashMap());
            root.setSize(new Long(0));
            root.setOnDisk(false);
            row.getFiles().clear();
            root.setIsDir(true);
            row.getFiles().put(root.getPath(), root);

        } catch (Throwable t) {
            logger.error("Couldn't access row path of row " + row.getRowname(), t);
        }

        root.refresh();

    }

    public void directoryRefresh(Row row, String path, boolean local) {
        java.io.File currDir = new java.io.File(path);

        if (currDir.isDirectory()) {

            java.io.File[] dir = currDir.listFiles();
            for (int i = 0; i < dir.length; i++) {
                if (filter.accept(dir[i]) && !(dir[i].getName() == null)) {

                    java.io.File file = dir[i];

                    cacheFile(row, file, local);
                }
            }
        }
    }

    private void cacheFile(Row row, java.io.File file, boolean local) {
        if (file.isDirectory()) {
            File dirData = setFileData(row, file, true);
            addFile(row, dirData);

            if(!local)
                dirData.refresh();

        } else if (file.isFile()) {
            File fileData = setFileData(row, file, false);
            addFile(row, fileData);
        }
    }

    /* adding file to Row files */
    private void addFile(Row row, File fileData) {

        row.getFiles().put(fileData.getPath(), fileData);

        // logger.warn("Added file: "+fileData.getName());
    }

    /* setting File informations */
    private File setFileData(Row row, java.io.File file, boolean isDir) {
        File fileData = new File();
        fileData.setIsDir(isDir);
        fileData.setRow(row);
        try {
            fileData.setPath(file.getCanonicalPath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fileData.setName(file.getName());
        fileData.setDate(new Long(file.lastModified()));
        fileData.setSize(new Long(file.length()));
        fileData.setOnDisk(true);
        return fileData;
    }

    public static File setVirtualFileData(Row r, File path, String name, boolean dir) {
        File cvsfile = new File();
        cvsfile.setName(name);
        cvsfile.setPath(path.getPath() + java.io.File.separator + name);
        cvsfile.setOnDisk(false);
        cvsfile.setIsDir(dir);
        cvsfile.setRow(r);
        cvsfile.setDate(new Long((new Date()).getTime()));
        cvsfile.setSize(new Long(0));
        return cvsfile;
    }

    public void newRow(String name, Row r, Map m) {
        // TODO Auto-generated method stub

    }

    public String newFile(Row r, String path, String entry) {
        java.io.File f = new java.io.File((path + "/" + entry).replace('/', java.io.File.separatorChar));
        if (f.exists() && f.isFile())
            return "This file already exists";
        boolean success = false;
        try {
            success = f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return ("Error while trying to create file " + entry);
        }
        if (success) {
            File newFile = setFileData(r, f, false);
            try {
                r.getFiles().put(f.getCanonicalPath(), newFile);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return "OK#" + f.getName();
        }
        return "Error while trying to create file " + entry;
    }

    public String newDir(Row r, String path, String entry) {
        java.io.File f = new java.io.File((path + "/" + entry).replace('/', java.io.File.separatorChar));
        if (f.exists() && f.isDirectory())
            return "This directory already exists";

        boolean success = f.mkdir();

        if (success) {
            File newFile = setFileData(r, f, true);
            try {
                r.getFiles().put(f.getCanonicalPath(), newFile);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return "OK#" + f.getName();
        }

        return "Error while trying to create directory " + entry;

    }

    public String deleteFile(Row r, String path) {

        String decodedParams = "";
        try {
            decodedParams = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
        }
        java.io.File f = new java.io.File((decodedParams).replace('/', java.io.File.separatorChar));
        boolean success = f.delete();
        if (success) {
            File cacheFile = (File) r.getFiles().get(decodedParams);
            FileCVS cvsCache = (FileCVS) cacheFile.getFiledata().get("cvs");
            
            // if there is CVS data for this file
            // TODO do this check for Tracker as well once it will be done
            if(cvsCache.getDate() != null) {
                cacheFile.setOnDisk(false);
                cvsCache.setStatus(CVSManager.NEEDS_CHECKOUT);
            }
            else
                r.getFiles().remove(decodedParams);
            
            return "OK#" + f.getName();
        }
        return "Error while trying to delete file " + f.getName();
    }

    public String uploadFile(Parade p, String path, String context) {
        Row r = (Row) p.getRows().get(context);
        File f = new File();
        java.io.File file = new java.io.File(path);
        f = setFileData(r, file, false);
        addFile(r, f);

        return path;

    }
    /* removes a file from the cache */
    public static void deleteSimpleFileCache(String context, String path) {
        Session s = InitServlet.getSessionFactory().openSession();
        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Row r = Row.getRow(p, context);
        Transaction tx = s.beginTransaction();
        
        r.getFiles().remove(path);
        
        tx.commit();
        s.close();
    }
    
    
    /* updates file cache of a single file */
    public static void updateSimpleFileCache(String context, String path) {
        FileManager fileMgr = new FileManager();
        Session s = InitServlet.getSessionFactory().openSession();
        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Row r = Row.getRow(p, context);
        Transaction tx = s.beginTransaction();
        
        java.io.File f = new java.io.File(path);
        if(!f.exists())
            return;
        
        fileMgr.cacheFile(r, f, false);
        
        tx.commit();
        s.close();
    }
    
    /* updates the File cache of a directory */
    public static void updateFileCache(String context, String path, boolean local) {
        FileManager fileMgr = new FileManager();
        Session s = InitServlet.getSessionFactory().openSession();
        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Row r = Row.getRow(p, context);
        Transaction tx = s.beginTransaction();
        
        fileMgr.directoryRefresh(r, path, local);
        
        tx.commit();
        s.close();
    }

    public void fileRefresh(Row row, String path) {
        java.io.File f = new java.io.File(path);
        if(!f.exists())
            return;
        cacheFile(row, f, false);
        
    }

}
