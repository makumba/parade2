package org.makumba.parade.listeners;

import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.hibernate.Session;
import org.makumba.parade.access.ActionLogDTO;
import org.makumba.parade.aether.ObjectTypes;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.tools.LogHandler;
import org.makumba.parade.tools.ParadeLogger;

/**
 * This servlet listens to requests sent by the CVS hook and which notify about newly commited files
 * 
 * @author Manuel Gay
 * 
 */
public class CVSCommitListenerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger logger = ParadeLogger.getParadeLogger(CVSCommitListenerServlet.class.getName());

    @Override
    public void init() {

    }

    @Override
    public void service(ServletRequest req, ServletResponse resp) throws IOException {

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
        String module = path.indexOf("/") > -1 ? path.substring(0, path.indexOf("/")) : path;
        path = path.indexOf("/") > -1 ? path.substring(path.indexOf("/")) : "";

        commit = commit.substring(commit.indexOf(":") + 1);

        if (commit.startsWith("-:New:directory")) {
            // we ignore this
            return;
        }

        Session s = InitServlet.getSessionFactory().openSession();

        String commitLog = user + " commited following files:\n";

        StringTokenizer st = new StringTokenizer(commit, ":");
        int max = st.countTokens() / 3;
        for (int i = 0; i < max; i++) {
            String file = path + "/" + st.nextToken();
            String oldRevision = st.nextToken();
            String newRevision = st.nextToken();
            commitLog += file + " (module " + module + ") from revision " + oldRevision + " to revision " + newRevision
                    + "\n";

            logCommit(module, file, user, newRevision);

            commitQueue.add(new Commit(module, file, newRevision, oldRevision));

        }

        handleFileCommit(s);

        logger.info(commitLog);

    }

    private final Vector<Commit> commitQueue = new Vector<Commit>();

    private void handleFileCommit(Session s) {

        CommitHandler ch = new CommitHandler(new Vector<Commit>(commitQueue), s);
        ch.start();
        commitQueue.clear();

    }

    private void logCommit(String module, String file, String user, String newRevision) {
        ActionLogDTO log = new ActionLogDTO();
        log.setAction("cvsCommitRepository");
        log.setObjectType(ObjectTypes.FILE);
        log.setDate(new Date());
        log.setUser(user);
        log.setFile(file);
        log.setQueryString("&module=" + module + "&newVersion=" + newRevision);

        LogHandler.redirectToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", log);
    }

    public class Commit {
        private final String module;

        private final String file;

        private final String newRevision;

        private final String oldRevision;

        public String getModule() {
            return module;
        }

        public String getFile() {
            return file;
        }

        public String getNewRevision() {
            return newRevision;
        }

        public String getOldRevision() {
            return oldRevision;
        }

        public Commit(String module, String file, String newRevision, String oldRevision) {
            super();
            this.module = module;
            this.file = file;
            this.newRevision = newRevision;
            this.oldRevision = oldRevision;
        }
    }
}
