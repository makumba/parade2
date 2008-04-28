package org.makumba.parade.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.access.ActionLogDTO;
import org.makumba.parade.controller.CvsController;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Application;
import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.CVSManager;
import org.makumba.parade.tools.Execute;
import org.makumba.parade.tools.TriggerFilter;

/**
 * This servlet listens to requests sent by the CVS hook and which notify about newly commited files
 * 
 * @author Manuel Gay
 * 
 */
public class CVSCommitListenerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(CVSCommitListenerServlet.class);

    @Override
    public void init() {

    }

    @Override
    public void service(ServletRequest req, ServletResponse resp) throws IOException {

        Map<String, String> commitChanges = new HashMap<String, String>();

        // System.out.println("*******" + ((HttpServletRequest) req).getQueryString());

        // normal file commit:
        // user:path:file1:old_version:new_version:file2:old_version:new_version:

        // new dir commit:
        // manu:mak-demo/generatedCode/bla:-:New:directory:NONE:NONE:

        // file delete commit:
        // manu:mak-demo/generatedCode/bla:bli:1.1:NONE:

        String commit = req.getParameter("commit");

        // we remove the last ":"
        commit = commit.substring(0, commit.length() - 1);

        String user = commit.substring(0, commit.indexOf(":"));
        commit = commit.substring(commit.indexOf(":") + 1);

        // module/path
        String path = commit.substring(0, commit.indexOf(":"));
        String module = path.substring(0, path.indexOf("/"));
        path = path.substring(path.indexOf("/"));

        commit = commit.substring(commit.indexOf(":") + 1);

        if (commit.startsWith("-:New:directory")) {
            // we ignore this
            return;
        }

        String commitLog = user + " commited following files:\n";

        StringTokenizer st = new StringTokenizer(commit, ":");
        int max = st.countTokens() / 3;
        for (int i = 0; i < max; i++) {
            String file = path + "/" + st.nextToken();
            String oldRevision = st.nextToken();
            String newRevision = st.nextToken();
            commitLog += file + " (module " + module + ") from revision " + oldRevision + " to revision " + newRevision
                    + "\n";

            commitChanges.put(file, newRevision);

            logCommit(module, file, user, newRevision);

            Session s = null;
            try {
                s = InitServlet.getSessionFactory().openSession();
                updateRepositoryCache(module, file, newRevision, s);
                updateRowFiles(module, file, newRevision, s);
            } finally {
                s.close();
            }
        }

        logger.info(commitLog);

    }

    private void logCommit(String module, String file, String user, String newRevision) {
        ActionLogDTO log = new ActionLogDTO();
        log.setAction("cvsCommitRepository");
        log.setDate(new Date());
        log.setUser(user);
        log.setFile(file);
        log.setQueryString("&module=" + module + "&newVersion=" + newRevision);

        TriggerFilter.redirectToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", log);
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

    private void updateRowFiles(String module, String file, String newRevision, Session s) {

        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));

        for (Row r : p.getRows().values()) {

            if (r.getApplication() != null && r.getApplication().getName().equals(module)) {

                boolean newerExists = false;

                File f = r.getFiles().get(r.getRowpath() + file);

                String rowRevision = f.getCvsRevision();
                if (newRevision != null && rowRevision != null) {

                    if (newRevision.equals("1.1.1.1")) {
                        newRevision = "1.1";
                    }
                    if (rowRevision.equals("1.1.1.1")) {
                        rowRevision = "1.1";
                    }

                    try {
                        Double rowRev = Double.parseDouble(rowRevision);
                        Double repositoryRev = Double.parseDouble(newRevision);
                        newerExists = repositoryRev > rowRev;
                    } catch (NumberFormatException nfe) {
                        logger.warn("Could not parse either the rowRevision " + f.getCvsRevision()
                                + " or the newRevision " + newRevision + " of file " + f.getFileURI());
                    }

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
