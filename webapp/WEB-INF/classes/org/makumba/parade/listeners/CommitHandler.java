package org.makumba.parade.listeners;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.access.ActionLogDTO;
import org.makumba.parade.controller.CvsController;
import org.makumba.parade.model.Application;
import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.CVSManager;
import org.makumba.parade.tools.CVSRevisionComparator;
import org.makumba.parade.tools.TriggerFilter;

public class CommitHandler extends Thread {

    private CVSRevisionComparator c = new CVSRevisionComparator();

    private String module;

    private String file;

    private String newRevision;

    private String oldRevision;

    private Session s;

    private boolean closeSession;

    public CommitHandler(String module, String file, String newRevision, String oldRevision, Session s,
            boolean closeSession) {
        super();
        this.module = module;
        this.file = file;
        this.newRevision = newRevision;
        this.oldRevision = oldRevision;
        this.s = s;
        this.closeSession = closeSession;
    }

    @Override
    public void run() {

        updateRepositoryCache(module, file, newRevision, s);
        updateRowFiles(module, file, newRevision, oldRevision.equals("NONE"), s);

        if (closeSession) {
            s.close();
        }

    }

    private void updateRepositoryCache(String module, String file, String newRevision, Session s) {

        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Application a = p.getApplications().get(module);
        if (a != null) {
            if (newRevision.equals("NONE")) {
                a.getCvsfiles().remove(module + file);
            } else {
                a.getCvsfiles().put(module + file, newRevision);
            }
        }

        tx.commit();

    }

    private void updateRowFiles(String module, String file, String newRevision, boolean isNew, Session s) {

        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));

        for (Row r : p.getRows().values()) {

            if (r.getApplication() != null && r.getApplication().getName().equals(module)) {

                // if this row has automatic update disabled
                if (r.getAutomaticCvsUpdate() == 10) {
                    continue;
                }

                if (!isNew) {

                    boolean newerExists = false;

                    File f = r.getFiles().get(r.getRowpath() + file);

                    // sometimes a row may just have the files of a given tag
                    // in that case we ignore it
                    if (f == null) {
                        continue;
                    }

                    String rowRevision = f.getCvsRevision();
                    if (newRevision != null && rowRevision != null) {

                        if (newRevision.equals("1.1.1.1")) {
                            newRevision = "1.1";
                        }
                        if (rowRevision.equals("1.1.1.1")) {
                            rowRevision = "1.1";
                        }

                        newerExists = c.compare(newRevision, rowRevision) == 1;

                        if (newerExists) {

                            // if we have a simple Update or a Merge on a file that the user didn't touch
                            // we just do it so the user doesn't have to worry about it
                            if (!CVSManager.cvsConflictOnUpdate(f) && f.getCvsStatus() == CVSManager.UP_TO_DATE) {
                                CvsController.onUpdateFile(r.getRowname(), f.getParentPath(), f.getPath());

                                // we also log the action as cvsupdate by user system
                                logUpdate(r.getRowname(), file);

                            }
                        }
                    }
                } else {
                    // we just try to update this file
                    CvsController.onUpdateFile(r.getRowname(), r.getRowpath()
                            + file.substring(0, file.lastIndexOf(java.io.File.separator)), r.getRowpath() + file);
                }

            }
        }

        tx.commit();

    }

    private void logUpdate(String paradeContext, String file) {
        ActionLogDTO log = new ActionLogDTO();
        log.setAction("cvsUpdateFile");
        log.setDate(new Date());
        log.setUser("system");
        log.setFile(file);
        log.setParadecontext(paradeContext);
        log.setContext(paradeContext);

        TriggerFilter.redirectToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", log);
    }

}
