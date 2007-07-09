package org.makumba.parade.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.init.ParadeProperties;
import org.makumba.parade.init.RowProperties;
import org.makumba.parade.model.managers.AntManager;
import org.makumba.parade.model.managers.CVSManager;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.model.managers.MakumbaManager;
import org.makumba.parade.model.managers.TrackerManager;
import org.makumba.parade.model.managers.WebappManager;
import org.makumba.parade.view.managers.RowStoreViewManager;

public class Parade {

    private Long id;

    private String baseDir = new String();

    private Map rows = new HashMap();

    private static Logger logger = Logger.getLogger(Parade.class.getName());

    // ParaDe managers
    // TODO these should be injected using Spring

    public FileManager fileMgr = new FileManager();

    public CVSManager CVSMgr = new CVSManager();

    public AntManager antMgr = new AntManager();

    public WebappManager webappMgr = new WebappManager();

    public MakumbaManager makMgr = new MakumbaManager();

    /*
     * 1. Calls create row for the new/to be updated rows 2. Calls for each row: - rowRefresh() - directoryRefresh()
     */
    public void refresh() {

        this.baseDir = ParadeProperties.getParadeBase();

        /* Reads the row definitions and perfoms update/creation */
        Map rowstore = (new RowProperties()).getRowDefinitions();
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

            fileMgr.rowRefresh(r);
            CVSMgr.rowRefresh(r);
            antMgr.rowRefresh(r);
            webappMgr.rowRefresh(r);
            makMgr.rowRefresh(r);
        }
    }

    /* Creates/updates rows */
    private void createRows(Map rowstore) {

        Iterator i = rowstore.keySet().iterator();
        Map rowDefinition = new HashMap();
        String rowname = "";

        while (i.hasNext()) {
            rowDefinition = (Map) rowstore.get((String) i.next());
            rowname = ((String) rowDefinition.get("name")).trim();

            // looks if the row with the same name already exists and updates if necessary
            if (this.getRows().containsKey(rowname)) {

                Row storedRow = (Row) this.getRows().get(rowname);

                String path = ((String) rowDefinition.get("path")).trim();
                String canonicalPath = path;
                try {
                    canonicalPath = new java.io.File(path).getCanonicalPath();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!canonicalPath.equals(storedRow.getRowpath())) {
                    storedRow.setRowpath((String) rowDefinition.get("path"));
                    logger.warn("The path of row " + rowname + " was updated to " + (String) rowDefinition.get("path"));
                }
                if (!((String) rowDefinition.get("desc")).trim().equals(storedRow.getDescription())) {
                    storedRow.setDescription((String) rowDefinition.get("desc"));
                    logger.warn("The description of row " + rowname + " was updated to "
                            + (String) rowDefinition.get("desc"));
                }

                newRow(storedRow, rowDefinition);

            // this is a new row
            } else {

                // creating Row object and passing the information
                Row r = new Row();
                String name = ((String) rowDefinition.get("name")).trim();
                r.setRowname(name);
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

                newRow(r, rowDefinition);

            }
        }
        
        //removing deleted rows from cache
        Iterator j = this.getRows().keySet().iterator();
        while(j.hasNext()) {
            String key = (String) j.next();
            
            // if the new rowstore definition doesn't contain the row, we trash it
            if (!rowstore.containsKey(key)) {
                this.getRows().remove(key);
            }
        }
    }

    public void newRow(Row r, Map rowDefinition) {
        r.setParade(this);
        rows.put(r.getRowname(), r);

        fileMgr.newRow(r.getRowname(), r, rowDefinition);
        CVSMgr.newRow(r.getRowname(), r, rowDefinition);
        antMgr.newRow(r.getRowname(), r, rowDefinition);
        webappMgr.newRow(r.getRowname(), r, rowDefinition);
        makMgr.newRow(r.getRowname(), r, rowDefinition);

    }

    /* Model related fields and methods */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map getRows() {
        return rows;
    }

    public void setRows(Map rows) {
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

    public static String constructAbsolutePath(String context, String relativePath) {
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));
        
        Row entryRow = null;
        
        if (context != null)
            entryRow = Row.getRow(p, context);
        
        String absolutePath = entryRow.getRowpath();
        
        tx.commit();
        s.close();
        
        if(relativePath == null || relativePath == "") return absolutePath;
        
        if(relativePath.equals("/")) return absolutePath;
        
        if(relativePath.endsWith("/")) relativePath = relativePath.substring(0, relativePath.length() - 1);
        absolutePath = entryRow.getRowpath() + java.io.File.separator + relativePath.replace("/", java.io.File.separator);
        
        return absolutePath;

    }
}
