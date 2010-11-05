package org.makumba.parade.model.managers;

import java.io.BufferedWriter;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.interfaces.FileRefresher;
import org.makumba.parade.model.interfaces.ParadeManager;
import org.makumba.parade.model.interfaces.RowRefresher;
import org.makumba.parade.tools.ParadeException;
import org.makumba.parade.tools.ParadeLogger;
import org.makumba.parade.tools.SimpleFileFilter;
import org.makumba.parade.tools.WordCount;

/**
 * This class manages the cache for the files in ParaDe.
 * 
 * TODO clean it up and document
 * 
 * @author Manuel Gay
 * 
 */
public class FileManager implements RowRefresher, FileRefresher, ParadeManager {
    static Logger logger = ParadeLogger.getParadeLogger(FileManager.class.getName());

    private FileFilter filter = new SimpleFileFilter();

    public void softRefresh(Row row) {
        // TODO Auto-generated method stub
    }

    /*
     * Creates a first File for the row which is its root dir and invokes its refresh() method
     */
    public void hardRefresh(Row row) {
        logger.fine("Refreshing row information for row " + row.getRowname());

        File root = new File();

        try {
            java.io.File rootPath = new java.io.File(row.getRowpath());
            root.setName("_root_");
            root.setPath(rootPath.getCanonicalPath());
            root.setParentPath("");
            root.setRow(row);
            root.setDate(new Long(new java.util.Date().getTime()));
            root.setSize(new Long(0));
            root.setOnDisk(false);
            row.getFiles().clear();
            root.setIsDir(true);
            row.getFiles().put(root.getPath(), root);

        } catch (Throwable t) {
            logger.severe("Couldn't access row path of row " + row.getRowname());
        }

        root.refresh();
    }

    /**
     * Refreshes the cache state of a directory
     * 
     * @param row
     *            the row we work with
     * @param path
     *            the path to the directory
     * @param local
     *            indicates whether this should be a local refresh or if it should propagate also to subdirs
     * 
     * 
     */
    public synchronized void directoryRefresh(Row row, String path, boolean local) {
        java.io.File currDir = new java.io.File(path);

        try {

            if (currDir.isDirectory()) {

                java.io.File[] dir = currDir.listFiles();

                Set<String> dirContent = new HashSet<String>();
                for (java.io.File d : dir) {

                    if (filter.accept(d) && !(d.getName() == null)) {
                        dirContent.add(d.getCanonicalPath());
                        cacheFile(row, d, local);
                    }
                }

                // now we clear zombie entries from the cache
                File fileInCache = row.getFiles().get(path);
                if (fileInCache != null) {
                    for (File child : fileInCache.getChildren(null)) {

                        if (dirContent.contains(child.getPath())) {
                            continue;
                            // if the file is not on disk but it was scheduled for CVS deletion, we keep it
                        } else if (!dirContent.contains(child.getPath()) && child.getCvsStatus() != null
                                && child.getCvsStatus() == CVSManager.DELETED) {
                            child.setOnDisk(false);
                            continue;
                        } else {
                            row.getFiles().remove(child.getPath());
                        }
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Creates / updates file cache entries
     * 
     * @param row
     *            the row this file belongs to
     * @param file
     *            the java.io.File to extract metadata from
     * @param local
     *            whether this is a local or a recursive cache update
     */
    public void cacheFile(Row row, java.io.File file, boolean local) {
        if (!file.exists() || file == null) {
            logger.warning("Trying to add non-existing/null file");
            return;
        }
        if (row == null) {
            logger.severe("Row was null while trying to add file with path " + file.getAbsolutePath());
            return;
        }

        if (file.isDirectory()) {
            File dirData = setFileData(row, file, true);
            addFile(row, dirData);

            if (!local) {
                directoryRefresh(row, dirData.getPath(), false);
            }

        } else if (file.isFile()) {
            File fileData = setFileData(row, file, false);
            makeParentDirs(row, fileData);
            addFile(row, fileData);
        }
    }

    private void makeParentDirs(Row row, File f) {
        File parent = row.getFiles().get(f.getParentPath().replace('/', java.io.File.separatorChar));
        if (parent == null) {
            logger.warning("Creating new parent directory cache " + f.getParentPath() + " for file " + f.getPath());
            parent = setFileData(row, new java.io.File(f.getParentPath()), true);
            addFile(row, parent);
            makeParentDirs(row, parent);
        }
    }

    /* adding file to Row files */
    private void addFile(Row row, File fileData) {
        // +++
        if (fileData.getCrawled() == null) {
            // set to 0 when you insert the file into the database so the crawler will crawl the file for the first time
            fileData.setCrawled(new Long(0));
        }
        // ---
        row.getFiles().put(fileData.getPath(), fileData);

        // logger.warning("Added file: "+fileData.getName());
    }

    /* setting File informations */
    private File setFileData(Row row, java.io.File file, boolean isDir) {
        File fileData = null;
        String path = null;
        try {
            path = file.getCanonicalPath();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // if we already had a file in cache we simple update it
        if ((fileData = row.getFiles().get(path)) != null) {
            fileData.setDate(new Long(file.lastModified()));
            fileData.setSize(new Long(file.length()));
            fileData.setOnDisk(true);
            if (!isDir) {
                fileData.setPreviousChars(fileData.getCurrentChars());
                fileData.setCurrentChars(WordCount.count(file.getAbsolutePath()));
            }

            // otherwise we make a new file
        } else {
            fileData = new File();
            fileData.setIsDir(isDir);
            fileData.setRow(row);
            try {
                fileData.setPath(file.getCanonicalPath());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            fileData.setParentPath(file.getParent().replace(java.io.File.separatorChar, '/'));
            fileData.setName(file.getName());
            fileData.setDate(new Long(file.lastModified()));
            fileData.setSize(new Long(file.length()));
            if (!isDir) {
                fileData.setCurrentChars(WordCount.count(file.getAbsolutePath()));
                fileData.setPreviousChars(WordCount.count(file.getAbsolutePath()));
            }
            fileData.setOnDisk(true);
        }
        return fileData;
    }

    public static File setVirtualFileData(Row r, File path, String name, boolean dir) {
        File virtualFile = new File();
        virtualFile.setName(name);
        virtualFile.setPath(path.getPath() + java.io.File.separator + name);
        virtualFile.setParentPath(path.getPath().replace(java.io.File.separatorChar, '/'));
        virtualFile.setOnDisk(false);
        virtualFile.setIsDir(dir);
        virtualFile.setRow(r);
        virtualFile.setDate(new Long((new Date()).getTime()));
        virtualFile.setSize(new Long(0));
        virtualFile.setCurrentChars(0);
        virtualFile.setPreviousChars(0);
        return virtualFile;
    }

    public void newRow(String name, Row r, Map<String, String> m) {
        // TODO Auto-generated method stub
    }

    public static String newFile(String fullname) {
        String result = null;
        java.io.File f = new java.io.File(fullname);

        if (f.exists()) {
            return "Error: a file with that name already exists.";
        }

        try {
            if (f.createNewFile()) {
                result = "New file " + f.getName() + " created.";
            } else {
                result = "Error: couldn't create file " + f.getAbsolutePath();
            }
        } catch (IOException e) {
            result = "Error: couldn't write. Make sure ParaDe has the security rights to write on the filesystem.";
            e.printStackTrace();
        }
        return result;
    }

    public static String newDir(String fullname) {
        String result = null;
        java.io.File f = new java.io.File(fullname);

        if (f.exists()) {
            return "Error: a file with that name already exists.";
        }

        if (f.mkdir()) {
            result = "New directory " + f.getName() + " created.";
        } else {
            result = "Error: couldn't create directory " + f.getAbsolutePath()
                    + ". Make sure ParaDe has the security rights to write on the filesystem.";
        }
        return result;
    }

    public static String deleteFile(String fullname) {
        String result = null;
        java.io.File f = new java.io.File(fullname);

        if (!f.exists()) {
            return "Error: a file with that name doesn't exist.";
        }

        if (f.delete()) {
            result = "File " + f.getName() + " deleted";
        } else {
            logger.severe("Error: couldn't delete file " + f.getAbsolutePath() + " " + "\n" + "Reason: exists: "
                    + f.exists() + ", canRead: " + f.canRead() + ", canWrite: " + f.canWrite());
            result = "Error while trying to delete file " + f.getName() + ".";
        }
        return result;
    }

    /**
     * @author Joao Andrade
     * @param r
     * @param path
     * @param entry
     * @return
     */
    public static String deleteDir(String fullname) {
        String result = null;
        java.io.File f = new java.io.File(fullname);
        String reason = "</br> ";

        if (!f.exists()) {
            return "Error: a file with that name doesn't exist.";
        }

        // FIXME Joao - a bit of an hack, but until non-empty folder deletion gets implemented this will do
        int nFiles = f.list().length;
        if (Arrays.asList(f.list()).contains("CVS") && nFiles == 1) {
            String cvsFolderName = f.getPath() + java.io.File.separator + "CVS";
            java.io.File cvsFolder = new java.io.File(cvsFolderName);
            for (String cvsFileName : cvsFolder.list()) {
                java.io.File cvsFile = new java.io.File(cvsFolderName + java.io.File.separator + cvsFileName);
                cvsFile.delete();
            }
            cvsFolder.delete();
        } else if (nFiles > 0) {
            reason += "The directory must be empty.";
        }
        // end of hack

        if (f.delete()) {
            result = "Directory " + f.getName() + " deleted";
        } else {
            logger.severe("Error couldn't delete directory " + f.getAbsolutePath() + " " + "\n" + "Reason: exists: "
                    + f.exists() + ", canRead: " + f.canRead() + ", canWrite: " + f.canWrite());
            result = "Error while trying to delete directory " + f.getName() + ".";
        }
        return result + reason;
    }

    /**
     * @author Joao Andrade
     * @param f
     * @return
     */
    // TODO review the code
    public static String saveFile(String fullname, String content) {
        String result = null;
        java.io.File f = new java.io.File(fullname);

        if (content == null) {
            throw new ParadeException("Cannot save file: ParaDe did not receive any contents from your browser."
                    + " If you use the Codepress editor, make sure that JavaScript is enabled"
                    + " and try reloading the edit page.");
        }

        // we save
        if (f.getParent() != null) {
            java.io.File d = new java.io.File(f.getParent());
            d.mkdirs();
        }
        try {
            f.createNewFile();

            // FIXME fishy windows line-break code. see if that doesn't cause trouble
            boolean windows = System.getProperty("line.separator").length() > 1;
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
            for (int i = 0; i < content.length(); i++) {
                if (windows || content.charAt(i) != '\r')
                    pw.print(content.charAt(i));
            }
            pw.close();
            result = "File " + f.getName() + " saved successfully.";
        } catch (IOException e) {
            result = "Error while trying to save file " + f.getName() + ".";
            logger.severe("Error while creating file:\n" + e.getMessage());
        }
        return result;
    }

    public static String uploadFile(String fullname, byte[] content) {
        String result = null;

        try {
            FileOutputStream fileOut = new FileOutputStream(fullname);
            fileOut.write(content);
            fileOut.flush();
            fileOut.close();
            result = "File " + fullname + " uploaded successfully";
        } catch (FileNotFoundException e) {
            result = "Error: File " + fullname + " wasn't found.";
        } catch (IOException e) {
            result = "Error: File " + fullname + " couldn't be written.";
        }
        return result;
    }

    public void fileRefresh(Row row, String fullname) {
        java.io.File f = new java.io.File(fullname);
        boolean isCached = row.getFiles().get(fullname) != null;
        if (!f.exists() && isCached) {
            // file was deleted but cache still exists
            removeFileCache(row.getFiles().get(fullname));
        } else if (f.exists() && !isCached) {
            // file exists but it isn't cached
            cacheFile(row, f, true);
        } else {
            // refresh file information
            cacheFile(row, f, true);
        }
    }

    public void removeFileCache(Row r, String path, String entry) {
        File cacheFile = r.getFiles().get(path + java.io.File.separator + entry);
        if (cacheFile != null) {
            removeFileCache(cacheFile);
        }
    }

    public void removeFileCache(File f) {
        // if there is CVS data for this file we keep it and set is as virtual
        if (f.getCvsStatus() != null) {
            f.setOnDisk(false);
        } else {
            f.getRow().getFiles().remove(f.getPath());
        }
    }

    /**
     * @author Joao Andrade
     * @param context
     * @param fullname
     */
    public static void updateSimpleCaches(String context, String fullname) {
        // updates the caches
        // TODO add other caches (e.g. tracker) here
        FileManager.updateSimpleFileCache(context, fullname);
        CVSManager.updateSimpleCvsCache(context, fullname);
    }

    public static void updateSimpleFileCache(String context, String fullname) {
        logger.fine("Refreshing file cache for file " + fullname + " of row " + context);
        Session s = InitServlet.getSessionFactory().openSession();
        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Row r = p.getRow(context);
        Transaction tx = s.beginTransaction();
        FileManager fileMgr = new FileManager();
        fileMgr.fileRefresh(r, fullname);
        tx.commit();
        s.close();
        logger.fine("Finished refreshing file cache for file " + fullname + " of row " + context);
    }

    /* updates the File cache of a directory */
    public static void updateDirectoryCache(String context, String path, boolean local) {
        logger.fine("Refreshing file cache for directory " + path + " of row " + context);
        Session s = InitServlet.getSessionFactory().openSession();
        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Row r = p.getRow(context);
        Transaction tx = s.beginTransaction();
        FileManager fileMgr = new FileManager();
        fileMgr.directoryRefresh(r, path, local);
        tx.commit();
        s.close();
        logger.fine("Finished refreshing file cache for directory " + path + " of row " + context);
    }

    public static void fileWrite(java.io.File file, String content) throws IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        pw.print(content);
    }

    // checks if a file should still be cached or if it's a zombie
    // FIXME should probably be in a more general CacheManager or so
    public static void checkShouldCache(String context, Collection<File> files) {
        for (File file : files) {
            java.io.File f = new java.io.File(file.getPath());
            if (!f.exists()) {
                if (file.getCvsStatus() != null && file.getCvsStatus().equals(CVSManager.DELETED)) {
                    file.getRow().getFiles().remove(file.getPath());
                }
            }
        }
    }

    /**
     * @author Joao Andrade
     * @param context
     * @param relativePath
     * @param filename
     * @return
     */
    public static String getFullFilename(String context, String relativePath, String filename) {
        String path = Parade.constructAbsolutePath(context, relativePath);
        return (path + "/" + filename).replace('/', java.io.File.separatorChar);
    }

    /**
     * @author Joao Andrade
     * @param rowName
     * @param path
     * @return
     */
    public static Boolean isInsideRow(String rowName, String path) {
        Session session = InitServlet.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Parade p = (Parade) session.get(Parade.class, new Long(1));
        Row row = p.getRow(rowName);
        Boolean isInside = row.isInside(path);
        tx.commit();
        session.close();
        return isInside;
    }
}
