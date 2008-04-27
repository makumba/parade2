package org.makumba.parade.listeners;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.access.ActionLogDTO;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Application;
import org.makumba.parade.model.Parade;
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

        //System.out.println("*******" + ((HttpServletRequest) req).getQueryString());

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
            String old_version = st.nextToken();
            String new_version = st.nextToken();
            commitLog += file + " (module " + module + ") from version " + old_version + " to version " + new_version
                    + "\n";

            commitChanges.put(file, new_version);

            logCommit(module, file, user, new_version);
            
            updateRepositoryCache(module, file, new_version);
        }

        logger.info(commitLog);

    }

    private void logCommit(String module, String file, String user, String new_version) {
        ActionLogDTO log = new ActionLogDTO();
        log.setAction("cvsCommitRepository");
        log.setDate(new Date());
        log.setUser(user);
        log.setFile(file);
        log.setQueryString("&module="+module+"&newVersion="+new_version);

        TriggerFilter.redirectToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", log);
    }
    
    private void updateRepositoryCache(String module, String file, String new_version) {
        
        Session s = null;
        Transaction tx = null;
        try {
            s = InitServlet.getSessionFactory().openSession();
            tx = s.beginTransaction();
            
            Parade p = (Parade) s.get(Parade.class, new Long(1));
            Application a = p.getApplications().get(module);
            if(a != null) {
                if(new_version.equals("NONE")) {
                    a.getCvsfiles().remove(module + file);
                } else {
                    a.getCvsfiles().put(module + file, new_version);
                }
            }
            
        } finally {
            tx.commit();
            s.close();
        }
        
    }

}
