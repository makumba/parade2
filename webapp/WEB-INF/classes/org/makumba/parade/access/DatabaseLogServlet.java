package org.makumba.parade.access;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.logging.LogRecord;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.init.RowProperties;
import org.makumba.parade.model.ActionLog;
import org.makumba.parade.model.Log;
import org.makumba.parade.model.User;
import org.makumba.parade.tools.PerThreadPrintStreamLogRecord;
import org.makumba.parade.tools.TriggerFilter;
import org.makumba.parade.view.TickerTapeData;
import org.makumba.parade.view.TickerTapeServlet;

/**
 * This servlet makes it possible to log events from various sources into the database.
 * It persists two kinds of logs:
 * <ul>
 * <li>ActionLogs, generated at each access</li>
 * <li>Logs, which are representing one log "line" and link to the ActionLog which led to their generation</li>
 * </ul>
 * 
 * TODO improve filtering
 * 
 * @author Manuel Gay
 *
 */
public class DatabaseLogServlet extends HttpServlet {
    
    private Logger logger = Logger.getLogger(DatabaseLogServlet.class);
    
    private RowProperties rp;
    
    private ThreadLocal<ActionLogDTO> lastCommit = new ThreadLocal<ActionLogDTO>();

    public void init(ServletConfig conf) {
        rp = new RowProperties();

    }

    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
        // when we start tomcat we are not ready yet to log
        // we first need a Hibernate SessionFactory to be initalised
        // this happens only after the Initservlet was loaded
        req.removeAttribute("org.makumba.parade.servletSuccess");
        if(InitServlet.getSessionFactory() == null)
            return;
        req.setAttribute("org.makumba.parade.servletSuccess", true);
        
        // retrieve the record guy
        Object record = req.getAttribute("org.makumba.parade.servletParam");
        if (record == null)
            return;

        // first check if this should be logged at all
        if(record instanceof ActionLogDTO) {
            if(!shouldLog((ActionLogDTO)record))
                return;
        }

        // open a new session, which we need to perform extraction
        Session s = null;
        try {
            
            s = InitServlet.getSessionFactory().openSession();
            
            if(record instanceof ActionLogDTO) {
                handleActionLog(req, record, s);
            } else {
                handleLog(req, record, s);
            }
            
        } finally {
            // close the session in any case
            s.close();
        }
    }

    private void handleActionLog(HttpServletRequest req, Object record, Session s) {
        ActionLogDTO log = (ActionLogDTO) record;
        
        // filter the log, generate additional information and give some meaning
        filterLog(log, s);
        
        // sometimes we just don't log (like for commits)
        if(log.getAction().equals("pinkPanda"))
            return;
        
        //let's see if we have already someone. if not, we create one
        Transaction tx = s.beginTransaction();
        ActionLog actionLog = null;
        if(log.getId() == null) {
            actionLog = new ActionLog();
        } else {
            actionLog = (ActionLog) s.get(ActionLog.class, log.getId());
        }
        log.populate(actionLog);
        s.saveOrUpdate(actionLog);
        tx.commit();
        
        //if we didn't have a brand new actionLog (meaning, a log with some info)
        //we add the populated actionLog as an event to the tickertape
        //TODO refactor me
        if(log.getId() != null) {
            String row = (log.getParadecontext()==null || log.getParadecontext().equals("null"))?((log.getContext()==null || log.getContext().equals("null"))?"parade2":log.getContext()):log.getParadecontext();
            String actionText = "";
            if(log.getAction() != null && !log.getAction().equals("null"))
                actionText = "user "+log.getUser()+" in row "+ row + " did action: "+log.getAction();
            TickerTapeData data = new TickerTapeData(actionText, "", log.getDate().toString());
            TickerTapeServlet.addItem(data);
        }
        // finally we also need to update the ActionLog in the thread
        log.setId(actionLog.getId());
        TriggerFilter.actionLog.set(log);
        
    }

    /**
     * Filter that does some "cosmetics" on the log and extracts meaning
     * 
     * @param log the original log to be altered
     */
    private void filterLog(ActionLogDTO log, Session s) {
        
        String queryString = log.getQueryString();
        String uri = log.getUrl();
        
        if(uri == null)
            uri = "";
        
        if(queryString == null)
            queryString = "";
        
        if(log.getAction() == null)
            log.setAction("");
        
        log.setParadecontext(getParam("context", queryString));
        
        String actionType="", op="", params="", display="", path="", file="";
        
        if(uri.indexOf("browse.jsp") > -1)
            actionType="browseRow";
        if(uri.indexOf("/servlet/browse") > -1)
            actionType="browse";
        if(uri.indexOf("File.do") > -1)
            actionType = "file";
        if(uri.indexOf("File.do?browse") > -1)
            actionType = "fileBrowse";
        if(uri.indexOf("Cvs.do") > -1)
            actionType = "cvs";

        op = getParam("op", queryString);
        params = getParam("params", queryString);
        display = getParam("display", queryString);
        path = getParam("path", queryString);
        file = getParam("file", queryString);
        
        // browse actions
        if(actionType.equals("browseRow")) {
            log.setAction("browseRow");
        }
        if(actionType.equals("browse")) {
            log.setAction("browseDir");
            log.setFile(path);
        }
        if(actionType.equals("fileBrowse")) {
            log.setAction("browseDir");
            log.setFile(path);
        }
        
        // view actions
        if(uri.endsWith(".jspx")) {
            log.setAction("view");
            // fetch the webapp root in a hackish way
            String webapp = rp.getRowDefinitions().get(log.getParadecontext()).get("webapp");
            log.setFile("/"+webapp + uri.substring(0, uri.length()-1));
            
        }
        
        // edit (open editor)
        if(actionType.equals("file") && op.equals("editFile")) {
            log.setAction("edit");
            log.setFile("/" + (path.endsWith("/")?path:path+"/") + file);
        }
        
        // save
        if(actionType.equals("file") && op.equals("saveFile")) {
            log.setAction("save");
            log.setFile("/" + path + "/" + file);
        }
        
        // delete
        if(actionType.equals("file") && op.equals("deleteFile")) {
            log.setAction("delete");
            try {
                log.setFile("/" + URLDecoder.decode(path, "UTF-8") + "/" + URLDecoder.decode(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.setFile("/" + path + "/" + file);
            }
        }
        
        // CVS
        if(actionType.equals("cvs")) {
            
            if(op.equals("check")) {
                log.setAction("cvsCheck");
                log.setFile("/"+params);
            }
            if(op.equals("update")) {
                log.setAction("cvsUpdateDirLocal");
                log.setFile("/" + params);
            }
            if(op.equals("rupdate")) {
                log.setAction("cvsUpdateDirRecursive");
                log.setFile("/" + params);
            }
            if(op.equals("commit")) {
                log.setAction("cvsCommit");
                String[] commitParams = getParamValues("params", queryString, null, 0);
                log.setFile("/" + commitParams[0] + "/" + commitParams[1]);
                lastCommit.set(log);
            }
            if(op.equals("diff")) {
                log.setAction("cvsDiff");
                log.setAction("/"+file);
            }
            if(op.equals("add") || op.equals("addbin")) {
                log.setAction("cvsAdd");
                log.setFile("/"+file);
            }
            if(op.equals("updatefile")) {
                log.setAction("cvsUpdateFile");
                log.setFile("/"+file);
            }
            if(op.equals("deletefile")) {
                log.setAction("cvsDeleteFile");
                log.setFile("/"+file);
            }
        }
        
        // CVS commit (hook)
        if(log.getAction().equals("cvsCommitRepository")) {
            log.setAction("cvsCommit");
            
            if(lastCommit.get() != null) {
                // the user commited through parade
                
                // we just check if it's the same file that has been commited through parade
                if(lastCommit.get().getFile().equals(log.getFile())) {
                    log.setAction("pinkPanda");
                    lastCommit.set(null);
                } else {
                    logger.error("***********************************************************************\n" +
                    		     "Unrecognised parade commit. please tell developers!\n" +
                    		     "***********************************************************************");
                }
            } else {
                // this is an external commit
                // let's try to lookup who did it
                Transaction tx = s.beginTransaction();
                Query q = s.createQuery("from User u where u.cvsuser = ?");
                q.setString(0, log.getUser());
                User u = (User) q.list().get(0);
                if(u != null) {
                    log.setUser(u.getLogin());
                }
                tx.commit();
                
            }
            
        }
    }
    
    private String getParam(String paramName, String queryString) {
        int n = queryString.indexOf(paramName+"=");
        String param = null;
        if(n > -1) {
            param = queryString.substring(n + paramName.length()+1);
            if(param.indexOf("&") > -1) {
                param = param.substring(0, param.indexOf("&"));
            }
        }
        return param;
    }
    
    private String[] getParamValues(String paramName, String queryString, String[] paramValues, int pos) {
        
        if(pos == 0) {
            paramValues = new String[5];
        }
        
        int n = queryString.indexOf(paramName+"=");
        String param = null;
        if(n > -1) {
            param = queryString.substring(n + paramName.length()+1);
            if(param.indexOf("&") > -1) {
                param = param.substring(0, param.indexOf("&"));
            }
            paramValues[pos] = param;
            
            String qs = queryString.substring(0,n) + queryString.substring(n + paramName.length()+1 + param.length());
            pos++;
            return getParamValues(paramName, qs, paramValues, pos);
        }
        return paramValues;
        
    }

    /**
     * Checks whether this access should be logged or not
     * @param log the DTO containing the log entry
     * @return <code>true</code> if this is worth logging, <code>false</code> otherwise
     */
    private boolean shouldLog(ActionLogDTO log) {
        
        if(log.getUrl() != null && (
                   log.getUrl().endsWith(".ico") 
                || log.getUrl().endsWith(".css")
                || log.getUrl().endsWith(".gif")
                || log.getUrl().endsWith(".js")
                || log.getUrl().equals("/servlet/ticker")
                || log.getUrl().equals("/servlet/cvscommit")
                || log.getUrl().equals("/tipOfTheDay.jsp")
                || (log.getUrl().equals("/servlet/browse") && log.getQueryString().indexOf("display=header") > -1)
                || (log.getUrl().equals("/servlet/browse") && log.getQueryString().indexOf("display=tree") > -1)
                || (log.getUrl().equals("/servlet/browse") && log.getQueryString().indexOf("display=command") > -1)
                )
                
                || (log.getOrigin() != null && log.getOrigin().equals("tomcat"))) {
            return false;
        }
        
        return true;
    }

    private void handleLog(HttpServletRequest req, Object record, Session s) {
        // extract useful information from the record

        Log log = new Log();
        ActionLog actionLog = retrieveActionLog(s);
        log.setActionLog(actionLog);

        // this is a java.util.logging.LogRecord
        if (record instanceof LogRecord) {
            LogRecord logrecord = (LogRecord) record;
            log.setDate(new Date(logrecord.getMillis()));
            log.setLevel(logrecord.getLevel().getName());
            log.setMessage(logrecord.getMessage());
            log.setOrigin("java.util.Logging");
            //log.setThrowable(logrecord.getThrown());
        } else if(record instanceof LoggingEvent) {
            LoggingEvent logevent = (LoggingEvent) record;
            log.setOrigin("log4j");
            log.setDate(new Date(logevent.timeStamp));
            log.setLevel(logevent.getLevel().toString());
            log.setMessage(logevent.getRenderedMessage());
            //if(logevent.getThrowableInformation() != null)
                //log.setThrowable(logevent.getThrowableInformation().getThrowable());
            //else
                //log.setThrowable(null);
        } else if(record instanceof PerThreadPrintStreamLogRecord) {
            PerThreadPrintStreamLogRecord pRecord = (PerThreadPrintStreamLogRecord)record;
            log.setDate(pRecord.getDate());
            log.setOrigin("stdout");
            log.setLevel("INFO");
            log.setMessage(pRecord.getMessage());
            //log.setThrowable(null);
        } else if(record instanceof Object[]) {
            Object[] rec = (Object[])record;
            log.setDate((Date)rec[0]);
            log.setOrigin("TriggerFilter");
            log.setLevel("INFO");
            log.setMessage((String)rec[1]);
        }

        Transaction tx = s.beginTransaction();

        // write the guy to the db
        s.saveOrUpdate(log);
        tx.commit();
    }

    private ActionLog retrieveActionLog(Session s) {
        ActionLogDTO actionLogDTO = TriggerFilter.actionLog.get();
        ActionLog actionLog = new ActionLog();
        actionLogDTO.populate(actionLog);
        
        // if the actionLog is there but not persisted, we persist it first
        if(actionLog.getId() == null) {
            Transaction tx = s.beginTransaction();
            s.save(actionLog);
            tx.commit();
            actionLogDTO.setId(actionLog.getId());
            TriggerFilter.actionLog.set(actionLogDTO);
        } else {
            actionLog = (ActionLog) s.get(ActionLog.class, actionLogDTO.getId());
        }
        return actionLog;
    }    

}
