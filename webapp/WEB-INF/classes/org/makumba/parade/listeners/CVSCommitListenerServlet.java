package org.makumba.parade.listeners;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.makumba.parade.access.ActionLogDTO;
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

        // System.out.println("*******"+((HttpServletRequest)req).getQueryString());

        // user:file1:old_version:new_version:file2:old_version:new_version:
        String commit = req.getParameter("commit");

        // we remove the last ":"
        commit = commit.substring(0, commit.length() - 1);

        String user = commit.substring(0, commit.indexOf(":"));
        commit = commit.substring(commit.indexOf(":") + 1);

        String commitLog = user + " commited following files:\n";

        StringTokenizer st = new StringTokenizer(commit, ":");
        int max = st.countTokens() / 3;
        for (int i = 0; i < max; i++) {
            String name = st.nextToken();
            String old_version = st.nextToken();
            String new_version = st.nextToken();

            commitLog += name + " from version " + old_version + " to version " + new_version + "\n";
            commitChanges.put(name, new_version);
            logCommit(name, user);
        }

        logger.info(commitLog);

    }

    private void logCommit(String file, String user) {
        ActionLogDTO log = new ActionLogDTO();
        log.setAction("cvsCommitRepository");
        log.setDate(new Date());
        log.setUser(user);
        log.setFile(file);

        TriggerFilter.redirectToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", log);
    }

}
