package org.makumba.parade.model.managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowCVS;
import org.makumba.parade.model.RowWebapp;
import org.makumba.parade.model.interfaces.CacheRefresher;
import org.makumba.parade.model.interfaces.ParadeManager;
import org.makumba.parade.model.interfaces.RowRefresher;
import org.makumba.parade.tools.Execute;

public class CVSManager implements CacheRefresher, RowRefresher, ParadeManager {

    public static Logger logger = Logger.getLogger(CVSManager.class.getName());

    public static Integer IGNORED = new Integer(101);

    public static Integer UNKNOWN = new Integer(-1);

    public static Integer UP_TO_DATE = new Integer(100);

    public static Integer LOCALLY_MODIFIED = new Integer(1);

    public static Integer NEEDS_CHECKOUT = new Integer(2);

    public static Integer NEEDS_UPDATE = new Integer(3);

    public static Integer ADDED = new Integer(4);

    public static Integer DELETED = new Integer(5);

    public static Integer CONFLICT = new Integer(6);

    public static DateFormat cvsDateFormat;

    static {
        cvsDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.UK);
        cvsDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * Refreshes the CVS cache of a directory
     * 
     * @param row
     *            the row in which the operation takes place
     * @param path
     *            the path to the directory
     * @param local
     *            should <code>true</code> if this action should be done locally, <code>false</code> if it should
     *            propagate also to sub-directories
     */
    public void directoryRefresh(Row row, String path, boolean local) {

        // for the root paths
        java.io.File currDir = new java.io.File(path);

        // we will go through the CVS entries of the real directory
        if (currDir.isDirectory() && !(currDir.getName() == null)) {
            // getting the File object mapped to this dir
            File currFile = row.getFiles().get(currDir.getAbsolutePath());

            // you never know
            if (!(currFile == null) && currFile.getIsDir())
                readFiles(row, currFile);
        }
    }

    /**
     * Updates the CVS cache of a file
     * 
     * @param row
     *            the row in which the operation takes place
     * @param absolutePath
     *            the absolute path to the file to update
     */
    public void fileRefresh(Row row, String absolutePath) {
        java.io.File f = new java.io.File(absolutePath);
        File currFile = row.getFiles().get(f.getParent());
        if (!(currFile == null) && currFile.getIsDir()) {
            readCVSEntries(row, currFile, f.getName());
        }
    }

    public void rowRefresh(Row row) {
        logger.debug("Refreshing row information for row " + row.getRowname());

        RowCVS cvsdata = new RowCVS();
        cvsdata.setDataType("cvs");

        readUserAndModule(row, cvsdata);

        row.addManagerData(cvsdata);
    }

    private void readUserAndModule(Row row, RowCVS data) {

        String path = row.getRowpath();
        data.setUser(getCVSUser(path));
        data.setModule(getCVSModule(path));
        data.setBranch(getCVSBranch(path));

    }

    public static String getCVSUser(String path) {
        String s = null;
        try {
            s = new BufferedReader(new FileReader(path + java.io.File.separator + "CVS" + java.io.File.separator
                    + "Root")).readLine();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }

        String user = "";

        if (s.startsWith(":pserver")) {
            s = s.substring(":pserver:".length());
            user = s.substring(0, s.indexOf("@"));
        } else if (s.startsWith(":extssh:")) {
            s = s.substring(":extssh:".length());
            user = s.substring(0, s.indexOf("@"));
        } else {
            user = ("non :pserver");
        }

        return user;
    }

    public static String getCVSModule(String path) {
        String s = null;
        try {
            s = new BufferedReader(new FileReader(path + java.io.File.separator + "CVS" + java.io.File.separator
                    + "Repository")).readLine();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return s.substring(s.lastIndexOf('/') + 1);
    }

    public static String getCVSRepository(String path) {
        String s = null;
        try {
            s = new BufferedReader(new FileReader(path + java.io.File.separator + "CVS" + java.io.File.separator
                    + "Root")).readLine();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return s;
    }

    public static String getCVSBranch(String path) {
        String s = null;
        try {
            s = "TMAIN";
            s = new BufferedReader(new FileReader(path + java.io.File.separator + "CVS" + java.io.File.separator
                    + "Tag")).readLine();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }

        return s.substring(1);
    }

    private void readFiles(Row r, File f) {

        readCVSEntries(r, f, null);
        readCVSIgnore(r, f);
        // readCVSCheckUpdate(paradeRow, data, pc);
    }

    /* Reads Entries file and extracts information */
    private void readCVSEntries(Row r, File file, String entry) {
        java.io.File f = new java.io.File((file.getPath() + "/" + "CVS/Entries").replace('/',
                java.io.File.separatorChar));
        if (!f.exists())
            return;

        Set<String> cvsFiles = new HashSet<String>();

        boolean updatedSimpleFileCache = false;

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = br.readLine()) != null || !updatedSimpleFileCache) {
                if (line == null)
                    return;

                // if the Entry is a file
                if (line.startsWith("/")) {
                    int n = line.indexOf('/', 1);
                    if (n == -1)
                        continue;
                    String name = line.substring(1, n);

                    if(name.equals("_root_"))
                        continue;
                    
                    if (entry != null && !(updatedSimpleFileCache = name.equals(entry)))
                        continue;

                    // logger.warn("Looking for CVS file: "+name);

                    String absoluteFilePath = file.getPath() + java.io.File.separator + name;
                    java.io.File currFile = new java.io.File(absoluteFilePath);

                    // we add this file entry to the other entries, for further checking against the cache
                    cvsFiles.add(absoluteFilePath);

                    // checking if the file we are looking for is mapped
                    File cvsfile = r.getFiles().get(absoluteFilePath);

                    boolean missing = false;
                    boolean fileOnDisk = currFile.exists();
                    if (cvsfile == null && !fileOnDisk) {
                        missing = true;
                    } else if (cvsfile != null && !fileOnDisk && cvsfile.getOnDisk()) {
                        // this ain't a virtual file but it's still there
                        // that's not ok, we have some zombie file info in cache
                        r.getFiles().remove(absoluteFilePath);
                        missing = true;
                    } else if (cvsfile != null && !fileOnDisk && !cvsfile.getOnDisk()) {
                        // this is a virtual file which isn't on disk
                        // so either it is missing or it was scheduled for deletion
                        if (cvsfile.getCvsRevision() != null) {
                            String cvsRevision = line.substring(1 + name.length() + 1);
                            if (cvsRevision.startsWith("-")) {
                                missing = false;
                            } else {
                                // file was deleted but is still in repo
                                missing = true;
                            }
                        } else {
                            missing = true;
                        }
                    } else if (cvsfile == null && fileOnDisk) {
                        // the bloody filemanager didn't do his job. we ask it to do it again
                        FileManager fileMgr = new FileManager();
                        fileMgr.cacheFile(r, currFile, true);
                        missing = false;
                    }

                    if (missing && cvsfile == null) {
                        cvsfile = FileManager.setVirtualFileData(r, file, name, false);
                        r.getFiles().put(absoluteFilePath, cvsfile);
                    }

                    if (cvsfile == null)
                        continue;

                    // setting CVS status
                    cvsfile.setCvsStatus(UNKNOWN);
                    if (missing) {
                        cvsfile.setCvsStatus(NEEDS_CHECKOUT);
                        continue;
                    }

                    line = line.substring(n + 1);
                    n = line.indexOf('/');
                    if (n == -1)
                        continue;
                    String revision = line.substring(0, n);
                    cvsfile.setCvsRevision(revision);
                    line = line.substring(n + 1);
                    n = line.indexOf('/');
                    if (n == -1)
                        continue;

                    // we check if the file exists on the disk
                    java.io.File fl = new java.io.File(cvsfile.getPath());

                    if (fl == null && !revision.startsWith("-")) {
                        cvsfile.setCvsStatus(NEEDS_CHECKOUT);
                        cvsfile.setCvsURL(null);
                        continue;
                    }

                    String date = line.substring(0, n);

                    if (date.equals("Result of merge")) {
                        cvsfile.setCvsStatus(LOCALLY_MODIFIED);
                        cvsfile.setCvsURL(null);
                        continue;
                    }

                    if (date.startsWith("Result of merge+")) {
                        cvsfile.setCvsStatus(CONFLICT);
                        cvsfile.setCvsURL(null);
                        continue;
                    }

                    if (date.equals("dummy timestamp")) {
                        cvsfile.setCvsStatus(revision.startsWith("-") ? DELETED : ADDED);
                        cvsfile.setCvsURL(null);
                        continue;
                    }

                    Date fd = null;
                    try {
                        cvsfile.setCvsDate(fd = cvsDateFormat.parse(date));
                    } catch (Throwable t) {
                        logger.error("Couldn't parse date of CVS File " + file.getPath(), t);
                        continue;
                    }

                    // we try to catch any tags
                    // /refundRequestEdit.jsp/1.7/Tue Apr 29 20:50:28 2008//T1.7

                    line = line.substring(n + 1);
                    n = line.indexOf("//");
                    if (n != -1) {
                        String tag = line.substring(n + 2);
                        if (tag.startsWith("T")) {
                            cvsfile.setCvsCheckedOutRevision(tag.substring(1));
                        }
                    }

                    long cvsModified = fd.getTime();

                    long l = fl.lastModified() - cvsModified;
                    if (Math.abs(l) < 1500
                    // for some stupid reason, lastModified() is different in
                            // Windows than Unix
                            // the difference seems to have to do with daylight
                            // saving
                            || Math.abs(Math.abs(l) - 3600000) < 1000) {
                        cvsfile.setCvsStatus(UP_TO_DATE);

                        // if the file is the same as on the repository, we can set a cvs url
                        String rowWebapp = r.getRowpath() + "/" + r.getWebappPath();
                        cvsfile.setCvsURL("cvs://"
                                + getCVSModule(r.getRowpath())
                                + (file.getPath().startsWith(rowWebapp) ? file.getPath().substring(
                                        rowWebapp.length()) : file.getPath().substring(r.getRowpath().length()))
                                + "/" + name);

                        continue;
                    }

                    cvsfile.setCvsStatus(l > 0 ? LOCALLY_MODIFIED : NEEDS_UPDATE);
                    cvsfile.setCvsURL(null);
                    continue;

                    // if the entry is a dir
                } else if (line.startsWith("D/")) {
                    if (entry != null)
                        continue;

                    int n = line.indexOf('/', 2);
                    if (n == -1)
                        continue;
                    String name = line.substring(2, n);

                    String absoluteDirectoryPath = file.getPath() + java.io.File.separator + name;

                    // we add this directory entry to the other entries, for further checking agains the cache
                    cvsFiles.add(absoluteDirectoryPath);

                    // checking if the directory we are looking for is mapped
                    File cvsfile = r.getFiles().get(absoluteDirectoryPath);
                    if (cvsfile == null) {
                        cvsfile = FileManager.setVirtualFileData(r, file, name, true);
                        r.getFiles().put(absoluteDirectoryPath, cvsfile);
                    }

                    if (cvsfile.getCvsStatus() == null) {
                        cvsfile.setCvsStatus(NEEDS_CHECKOUT);
                    } else {
                        cvsfile.setCvsStatus(UP_TO_DATE);
                        cvsfile.setCvsRevision("(dir)");
                    }
                }

            }
            br.close();

            if (entry != null)
                return;

            // now we check if our cache doesn't contain "zombie" cvsdata elements, i.e. if a file doesn't have
            // outdated cvs information
            List<String> cachedFiles = file.getChildrenPaths();
            Iterator<String> i = cachedFiles.iterator();
            while (i.hasNext()) {
                String filePath = i.next();
                if (!cvsFiles.contains(filePath)) {
                    // remove zombie entry
                    r.getFiles().get(filePath).emptyCvsData();
                }
            }

        } catch (Throwable t) {
            logger.error("Error while trying to set CVS information for file " + file.getName() + " at path "
                    + file.getPath(), t);
        }
    }

    /* Reads .cvsignore */
    private void readCVSIgnore(Row r, File file) {
        if (!file.getIsDir())
            return;

        java.io.File f = new java.io.File((file.getPath() + "/" + ".cvsignore")
                .replace('/', java.io.File.separatorChar));

        if (!f.exists())
            return;

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                File cvsfile = r.getFiles().get(file.getPath() + java.io.File.separator + line);
                if (cvsfile == null)
                    continue;
                cvsfile.setCvsStatus(IGNORED);
            }
            br.close();
        } catch (Throwable t) {
            logger.error("Error while trying to read .cvsignore of directory " + file.getName(), t);
        }
    }

    public void newRow(String name, Row r, Map<String, String> m) {
        // TODO Auto-generated method stub

    }

    /**
     * Updates the CVS cache for a whole directory.
     * 
     * @param context
     *            the context in which the operation takes place
     * @param path
     *            the absolute path to the directory
     * @param local
     *            should <code>true</code> if this action should be done locally, <code>false</code> if it should
     *            propagate also to sub-directories
     */
    public synchronized static void updateCvsCache(String context, String path, boolean local) {
        logger.debug("Refreshing CVS cache for path " + path + " of row " + context
                + ((local) ? " locally" : " recursively"));
        CVSManager cvsMgr = new CVSManager();
        Session s = InitServlet.getSessionFactory().openSession();
        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Row r = Row.getRow(p, context);
        Transaction tx = s.beginTransaction();
        cvsMgr.directoryRefresh(r, path, local);
        tx.commit();
        s.close();
        logger.debug("Finished refreshing CVS cache for path " + path + " of row " + context
                + ((local) ? " locally" : " recursively"));
    }

    /**
     * Updates the CVS cache for a single file.
     * 
     * @param context
     *            the context in which the operation takes place
     * @param path
     *            the absolute path to the directory of the file
     * @param filename
     *            the name of the file of which to update the cache
     */
    public synchronized static void updateSimpleCvsCache(String context, String absolutePath) {
        logger.debug("Refreshing CVS cache for file " + absolutePath + " of row " + context);
        CVSManager cvsMgr = new CVSManager();
        Session s = InitServlet.getSessionFactory().openSession();
        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Row r = Row.getRow(p, context);
        Transaction tx = s.beginTransaction();
        cvsMgr.fileRefresh(r, absolutePath);
        tx.commit();
        s.close();
        logger.debug("Finished refreshing CVS cache for file " + absolutePath + " of row " + context);
    }

    /**
     * Checks if there will be a cvs conflict if this file is updated
     * 
     * @param f
     *            the file to check
     * @return <code>true</code> if there will be a CVS conflict on update, <code>false</code> otherwise
     */
    public static boolean cvsConflictOnUpdate(File f) {
        boolean cvsConflictOnUpdate = false;

        // we check what are the consequences of an update of this file
        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);

        // let's get the result
        Vector<String> cmd = new Vector<String>();
        cmd.add("cvs");
        cmd.add("-n");
        cmd.add("update");
        cmd.add(f.getName());

        Execute.exec(cmd, new java.io.File(f.getParentPath()), out);

        // first let's see if everything went fine
        if (result.toString().indexOf("exit value: 1") > -1) {
            logger.error("Could not retrieve CVS status for file " + f.getName() + ". Result of the operation was:\n"
                    + result.toString());
        } else {

            BufferedReader br = new BufferedReader(new StringReader(result.toString()));

            try {

                // we skip the first two lines, that is just displaying the command
                br.readLine();
                br.readLine();

                // U personDelete.jsp
                String line = br.readLine();
                if (line.startsWith("rcsmerge: warning: conflicts during merge")) {
                    cvsConflictOnUpdate = true;
                }

            } catch (IOException ioe) {
                logger.error("IO Exception while reading CVS output: " + ioe.getMessage());
                return false;
            }
        }

        return cvsConflictOnUpdate;
    }
}