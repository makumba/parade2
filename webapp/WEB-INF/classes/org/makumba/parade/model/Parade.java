package org.makumba.parade.model;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

import org.apache.log4j.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.init.ParadeProperties;
import org.makumba.parade.init.RowProperties;
import org.makumba.parade.model.managers.AntManager;
import org.makumba.parade.model.managers.CVSManager;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.model.managers.MakumbaManager;
import org.makumba.parade.model.managers.WebappManager;
import org.makumba.parade.tools.SimpleFileFilter;

public class Parade {

    private Long id;

    private String baseDir = new String();

    private Map<String, Row> rows = new HashMap<String, Row>();

    private RowProperties rowproperties = new RowProperties();
    
    private static Logger logger = Logger.getLogger(Parade.class.getName());

    // ParaDe managers
    // TODO these should be injected using Spring

    public FileManager fileMgr = new FileManager();

    public CVSManager CVSMgr = new CVSManager();

    public AntManager antMgr = new AntManager();

    public WebappManager webappMgr = new WebappManager();

    public MakumbaManager makMgr = new MakumbaManager();

    public static final String LOCK = ".parade-lock~";

    public static Vector<String> lockedDirectories = new Vector<String>();

    /*
     * 1. Calls create row for the new/to be updated rows 2. Calls for each row: - rowRefresh() - directoryRefresh() 3.
     * Add listener to trigger refresh if needed
     */
    public void refresh() {
        logger.info("Starting ParaDe-wide refresh...");

        this.baseDir = ParadeProperties.getParadeBase();

        /* Reads the row definitions and perfoms update/creation */
        Map rowstore = rowproperties.getRowDefinitions();
        if (rowstore.isEmpty()) {
            logger.warn("No row definitions found, check RowProperties");
        }

        createRows(rowstore);

        /*
         * TODO: read in config class/file which managers are row managers and and launch rowRefresh(row)) for all of
         * them
         */

        Iterator i = rows.keySet().iterator();
        while (i.hasNext()) {

            Row r = (Row) rows.get((String) i.next());
            refreshRow(r);
        }

        logger.info("ParaDe-wide refresh finished");

    }
    
    public void rebuildRowCache(Row r) {
        
        long start = new Date().getTime();
        logger.info("Starting to rebuild cache of row "+r.getRowname());
        
        String rowName = r.getRowname();
        
        // first we trash the existing row
        logger.info("Emptying cache of row "+r.getRowname());
        this.getRows().remove(r);
        
        // then we read the row information from the rowstore
        Map<String, String> rowDefinition = rowproperties.getRowDefinitions().get(rowName);
        
        // then we build it again
        Row r1 = buildRow(rowDefinition);
        
        // finally, we refresh it
        logger.info("Populating cache of row "+r.getRowname());
        refreshRow(r1);
        
        long end = new Date().getTime();
        
        logger.info("Finished rebuilding cache of row "+rowName+". Operation took " + (end - start) + " ms");

        
    }

    public void refreshRow(Row r) {
        fileMgr.rowRefresh(r);
        CVSMgr.rowRefresh(r);
        antMgr.rowRefresh(r);
        webappMgr.rowRefresh(r);
        makMgr.rowRefresh(r);
    }

    /* Creates/updates rows */
    private void createRows(Map rowstore) {
        logger.info("Updating rowstore cache...");

        Iterator i = rowstore.keySet().iterator();
        Map rowDefinition = new HashMap();
        String rowname = "";

        while (i.hasNext()) {
            rowDefinition = (Map) rowstore.get((String) i.next());
            buildRow(rowDefinition);
        }

        // removing deleted rows from cache
        Iterator j = this.getRows().keySet().iterator();
        while (j.hasNext()) {
            String key = (String) j.next();

            // if the new rowstore definition doesn't contain the row, we trash it
            if (!rowstore.containsKey(key)) {
                logger.info("Dropping row " + key + " from cache.");
                this.getRows().remove(key);
            }
        }
    }

    private Row buildRow(Map<String, String> rowDefinition) {
        String rowname;
        rowname = rowDefinition.get("name").trim();

        // looks if the row with the same name already exists and updates if necessary
        if (this.getRows().containsKey(rowname)) {

            Row storedRow = (Row) this.getRows().get(rowname);

            String path = rowDefinition.get("path").trim();
            String canonicalPath = path;
            try {
                canonicalPath = new java.io.File(path).getCanonicalPath();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // the path is modified
            if (!canonicalPath.equals(storedRow.getRowpath())) {
                storedRow.setRowpath(rowDefinition.get("path"));
                logger.warn("The path of row " + rowname + " was updated to " + rowDefinition.get("path"));
            }

            // the description is modified
            if (!rowDefinition.get("desc").trim().equals(storedRow.getDescription())) {
                storedRow.setDescription(rowDefinition.get("desc"));
                logger.warn("The description of row " + rowname + " was updated to "
                        + (String) rowDefinition.get("desc"));
            }

            // updating the specific row data
            return registerRow(storedRow, rowDefinition);

            // this is a new row
        } else {
            // creating Row object and passing the information
            Row r = new Row();
            String name = ((String) rowDefinition.get("name")).trim();
            r.setRowname(name);
            logger.info("Registering row " + r.getRowname());
            String path = ((String) rowDefinition.get("path")).trim();
            String canonicalPath = path;
            try {
                canonicalPath = new java.io.File(path).getCanonicalPath();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            r.setRowpath(canonicalPath);
            r.setDescription((String) rowDefinition.get("desc"));

            return registerRow(r, rowDefinition);

        }
    }

    public Row registerRow(Row r, Map<String, String> rowDefinition) {
        r.setParade(this);
        rows.put(r.getRowname(), r);

        fileMgr.newRow(r.getRowname(), r, rowDefinition);
        CVSMgr.newRow(r.getRowname(), r, rowDefinition);
        antMgr.newRow(r.getRowname(), r, rowDefinition);
        webappMgr.newRow(r.getRowname(), r, rowDefinition);
        makMgr.newRow(r.getRowname(), r, rowDefinition);
        
        return r;

    }

    /**
     * Initalises the file system monitoring using JNotify ({@link http://jnotify.sourceforge.net/})
     */
    public void addJNotifyListeners() {

        // for each row we create listeners that will inform us whenever a change occurs in the filesystem
        Iterator i = this.getRows().keySet().iterator();
        while (i.hasNext()) {

            // let's get the path to where the interesting data is
            Row r = (Row) getRows().get(i.next());

            // we're not interested in the ParaDe row
            if (r.getRowpath().equals(getBaseDir()))
                continue;

            // String webappPath = ((RowWebapp) r.getRowdata().get("webapp")).getWebappPath();
            String path = r.getRowpath(); // + java.io.File.separator + webappPath;

            // what kind of changes do we want to watch
            int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;

            // we also want to know what happens in the subdirectories
            boolean watchSubtree = true;

            // now we start watching
            try {
                int watchID = JNotify.addWatch(path, mask, watchSubtree, new JNotifyListener() {

                    public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
                        logger.debug("JNotifyTest.fileRenamed() : wd #" + wd + " root = " + rootPath + ", " + oldName
                                + " -> " + newName);
                        checkSpecialFile(rootPath, oldName);
                        checkSpecialFile(rootPath, newName);
                        if (isLocked(rootPath, oldName, JNotify.FILE_RENAMED))
                            return;
                        cacheDeleted(rootPath, oldName);
                        cacheNew(rootPath, newName);

                    }

                    public void fileModified(int wd, String rootPath, String name) {
                        logger.debug("JNotifyTest.fileModified() : wd #" + wd + " root = " + rootPath + ", " + name);
                        checkSpecialFile(rootPath, name);
                        if (isLocked(rootPath, name, JNotify.FILE_MODIFIED))
                            return;
                        cacheModified(rootPath, name);
                    }

                    public void fileDeleted(int wd, String rootPath, String name) {
                        logger.debug("JNotifyTest.fileDeleted() : wd #" + wd + " root = " + rootPath + ", " + name);
                        checkSpecialFile(rootPath, name);
                        if (isLocked(rootPath, name, JNotify.FILE_DELETED))
                            return;
                        cacheDeleted(rootPath, name);
                    }

                    public void fileCreated(int wd, String rootPath, String name) {
                        logger.debug("JNotifyTest.fileCreated() : wd #" + wd + " root = " + rootPath + ", " + name);
                        checkSpecialFile(rootPath, name);
                        if (isLocked(rootPath, name, JNotify.FILE_CREATED))
                            return;
                        cacheNew(rootPath, name);
                    }

                    /**
                     * Checks whether this is a special file and if something should be done, like refreshing a manager or so
                     * @param rootPath the root path of the file
                     * @param name the name of the file
                     */
                    private void checkSpecialFile(String rootPath, String name) {
                        
                        // TODO this should be available to all the model managers, via the interface
                        
                        // we check if this is the makumba jar, and if yes, we recompute the version number cache
                        String filePath = rootPath + java.io.File.separator + name;
                        if(filePath.indexOf("WEB-INF/lib/makumba") > -1 && filePath.indexOf(".jar") > -1) {
                            
                            logger.info("Makumba JAR "+filePath+" was changed, refreshing its version.");
                            
                            Session session = null;
                            try {
                                session = InitServlet.getSessionFactory().openSession();

                                Parade p = (Parade) session.get(Parade.class, new Long(1));
                                Transaction tx = session.beginTransaction();

                                makMgr.rowRefresh(findRowFromContext(rootPath, p));

                                tx.commit();

                            } finally {
                                session.close();
                            }

                        }
                        
                    }

                    private synchronized void cacheNew(String rootPath, String fileName) {
                        java.io.File f = new java.io.File(rootPath + java.io.File.separator + fileName);

                        if (!f.exists())
                            return;

                        SimpleFileFilter sf = new SimpleFileFilter();

                        if (sf.accept(f)) {
                            cacheFile(rootPath, fileName);
                        }
                    }

                    private synchronized void cacheModified(String rootPath, String fileName) {
                        java.io.File f = new java.io.File(rootPath + java.io.File.separator + fileName);

                        if (!f.exists())
                            return;

                        SimpleFileFilter sf = new SimpleFileFilter();

                        // we don't refresh directories, since the modification of files in there are going to be
                        // notified
                        if (sf.accept(f) && !f.isDirectory()) {
                            cacheFile(rootPath, fileName);
                        }
                    }

                    private synchronized void cacheDeleted(String rootPath, String fileName) {
                        java.io.File f = new java.io.File(rootPath + java.io.File.separator + fileName);

                        SimpleFileFilter sf = new SimpleFileFilter();

                        if (sf.accept(f) && !f.isDirectory()) {
                            deleteFile(rootPath, fileName);
                        }
                    }

                    private void cacheFile(String rootPath, String fileName) {
                        if (rootPath == null || fileName == null)
                            return;

                        logger.debug("Refreshing file cache for file " + fileName + " of directory " + rootPath);

                        java.io.File f = new java.io.File(rootPath + java.io.File.separator + fileName);

                        FileManager fileMgr = new FileManager();
                        Session session = null;

                        try {
                            session = InitServlet.getSessionFactory().openSession();

                            Parade p = (Parade) session.get(Parade.class, new Long(1));
                            Row r = findRowFromContext(rootPath, p);
                            Transaction tx = session.beginTransaction();

                            // we cache the file, and if it's a directory it will be a local update
                            fileMgr.cacheFile(r, f, true);

                            tx.commit();

                        } finally {
                            session.close();
                        }

                        logger.debug("Finished refreshing file cache for file " + fileName + " of directory "
                                + rootPath);

                    }

                    private void deleteFile(String rootPath, String fileName) {
                        if (rootPath == null || fileName == null)
                            return;

                        logger.debug("Deleting file cache for file " + fileName + " of directory " + rootPath);

                        java.io.File f = new java.io.File(rootPath + java.io.File.separator + fileName);

                        FileManager fileMgr = new FileManager();
                        Session session = null;

                        try {
                            session = InitServlet.getSessionFactory().openSession();

                            Parade p = (Parade) session.get(Parade.class, new Long(1));
                            Row r = findRowFromContext(rootPath, p);
                            Transaction tx = session.beginTransaction();

                            fileMgr.removeFileCache(r, rootPath, fileName);

                            tx.commit();

                        } finally {
                            session.close();
                        }

                        logger.debug("Finished deleting file cache for file " + fileName + " of directory " + rootPath);
                    }

                    private Row findRowFromContext(String rowPath, Parade p) {
                        Iterator i = p.getRows().keySet().iterator();

                        boolean row_found = false;
                        Row contextRow = null;
                        while (i.hasNext() && !row_found) {
                            contextRow = (Row) p.getRows().get(i.next());
                            row_found = rowPath.startsWith(contextRow.getRowpath());
                        }
                        return contextRow;
                    }

                    /**
                     * Avoids conflicts with CVS Manager by checking whether there's a lock on the files to come in the
                     * directory / subdirectories affected by the lock.
                     * 
                     */
                    private boolean isLocked(String rootPath, String relativeFilePath, int mask) {
                        if (rootPath == null || relativeFilePath == null)
                            return false;

                        String relativePath = relativeFilePath.indexOf(java.io.File.separator) > -1 ? relativeFilePath
                                .substring(0, relativeFilePath.lastIndexOf(java.io.File.separator)) : "";
                        String fileName = relativeFilePath.indexOf(java.io.File.separator) > -1 ? relativeFilePath
                                .substring(relativeFilePath.lastIndexOf(java.io.File.separator) + 1) : relativeFilePath;
                        String path = rootPath + (relativePath.length() > 0 ? java.io.File.separator : "")
                                + relativePath;
                        String filePath = path + java.io.File.separator + fileName;
                        if (fileName.endsWith(LOCK) && mask == JNotify.FILE_CREATED) {
                            // a lock was just created, we register it
                            if (fileName.equals(LOCK)) {
                                lockedDirectories.add(path);
                                logger.debug("Adding lock for directory " + path);
                            } else if (fileName.endsWith(LOCK) && fileName.length() > LOCK.length()) {
                                lockedDirectories.add(path + java.io.File.separator
                                        + fileName.substring(0, fileName.indexOf(LOCK)));
                                logger.debug("Adding lock for file " + path + java.io.File.separator
                                        + fileName.substring(0, fileName.indexOf(LOCK)));
                            }
                            return true; // we don't want to cache this file anyway

                        } else if (fileName.endsWith(LOCK) && mask == JNotify.FILE_DELETED) {
                            // a lock was removed, we unregister the directory
                            String lockPath = "";
                            if (fileName.equals(LOCK))
                                lockPath = path;
                            else if (fileName.endsWith(LOCK) && fileName.length() > LOCK.length())
                                lockPath = path + java.io.File.separator
                                        + fileName.substring(0, fileName.indexOf(LOCK));

                            if (lockedDirectories.contains(lockPath)) {
                                lockedDirectories.remove(lockPath);
                                logger.debug("Removing lock " + lockPath);
                            } else {
                                logger.error("Tried to remove lock for directory " + path
                                        + " but there was no lock registered");
                            }
                            return true; // we don't want to cache this file anyway
                        } else if (fileName.endsWith(LOCK) && mask == JNotify.FILE_MODIFIED) {
                            // WTF?
                            logger.warn("Lock of directory " + path + " modified, shouldn't happen.");
                        }

                        // does the actual check
                        for (int i = 0; i < lockedDirectories.size(); i++) {
                            if (path.startsWith(lockedDirectories.get(i)) || filePath.equals(lockedDirectories.get(i))) {
                                logger.debug("Lock detected for " + lockedDirectories.get(i));
                                return true;
                            }
                        }

                        return false;
                    }

                });
                logger.info("Adding filesystem watch to row " + r.getRowname());
            } catch (JNotifyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NullPointerException npe) {
                // do nothing. JNotify returns plenty of those.
            }
        }
    }

    /* Model related fields and methods */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Row> getRows() {
        return rows;
    }

    public void setRows(Map<String, Row> rows) {
        this.rows = rows;
    }

    public Parade() {

    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String paradeBase) {
        this.baseDir = paradeBase;
    }

    public static void createFileLock(String absoluteFilePath) {
        java.io.File lock = new java.io.File(absoluteFilePath + LOCK);
        try {
            lock.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void removeFileLock(String absoluteFilePath) {
        java.io.File lock = new java.io.File(absoluteFilePath + LOCK);
        lock.delete();
    }

    public static void createDirectoryLock(String absoluteDirectoryPath) {
        java.io.File f = new java.io.File(absoluteDirectoryPath + java.io.File.separator + LOCK);
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void removeDirectoryLock(String absoluteDirectoryPath) {
        java.io.File f = new java.io.File(absoluteDirectoryPath + java.io.File.separator + LOCK);
        f.delete();
    }

    public static String constructAbsolutePath(String context, String relativePath) {
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Row entryRow = null;

        if (context != null) {
            Query q = s.createQuery("from Row r where r.rowname = :context");
            q.setString("context", context);
            entryRow = (Row) q.list().get(0);
        }

        String absolutePath = entryRow.getRowpath();

        tx.commit();
        s.close();

        if (relativePath == null || relativePath == "")
            return absolutePath;

        if (relativePath.equals("/"))
            return absolutePath;

        if (relativePath.endsWith("/"))
            relativePath = relativePath.substring(0, relativePath.length() - 1);
        absolutePath = entryRow.getRowpath() + java.io.File.separator
                + relativePath.replace('/', java.io.File.separatorChar);

        return absolutePath;

    }
}
