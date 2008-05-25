package org.makumba.parade.model.managers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Application;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.tools.Execute;

/**
 * Simple thread that checks out a CVS module and makes a hidden row in parade
 * 
 * TODO if sthing already checked out - delete and checkout out again
 * 
 * @author Manuel Gay
 *
 */
public class RowFromCVSModuleCheckoutHandler extends Thread {

    private Application a;

    private String path;

    private Logger logger = Logger.getLogger(RowFromCVSModuleCheckoutHandler.class);
    
    public RowFromCVSModuleCheckoutHandler(Application a, String path) {
        this.a = a;
        this.path = path;
    }

    public void run() {

        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);

        // let's get the result
        Vector<String> cmd = new Vector<String>();
        cmd.add("cvs");
        cmd.add("-d" + a.getRepository());
        cmd.add("co"); // listing from the root
        cmd.add(a.getName());

        Execute.exec(cmd, new File(path).getParentFile(), out);

        if (result.toString().indexOf("exit value: 1") > -1) {
            logger.error("Could not retrieve checkout module for application " + a.getName()
                    + ". Result of the operation was:\n" + result.toString());
        } else {
            
            Row r = new Row();
            r.setRowname(a.getName()+"-module"); // just in case, this may conflict with existing rows of the same name
            logger.info("Registering special row " + r.getRowname());
            String canonicalPath = path;
            try {
                canonicalPath = new java.io.File(path).getCanonicalPath();
            } catch (IOException e) {
                logger.error("Could not get the canonical path for row " + r.getRowname() + " with path " + path);
            }
            r.setRowpath(canonicalPath);
            r.setDescription("Row of CVS module "+a.getName());
            r.setWebappPath(a.getWebappPath());
            r.setModuleRow(true);
            r.setAutomaticCvsUpdate(Row.AUTO_CVS_UPDATE_ENABLED);
            
            Session s = null;
            Transaction tx = null;
            try {
                s = InitServlet.getSessionFactory().openSession();
                tx = s.beginTransaction();
                
                s.save(r);
                
                Parade p = (Parade) s.get(Parade.class, new Long(1));
                
                HashMap<String, String> rowData = new HashMap<String, String>();
                rowData.put("webapp", a.getWebappPath());
                
                p.registerRow(r, rowData);
                p.refreshRow(r);
                
                tx.commit();
                
            } finally {
                if(s!= null) {
                    s.close();
                }
            }
        }
    }

}
