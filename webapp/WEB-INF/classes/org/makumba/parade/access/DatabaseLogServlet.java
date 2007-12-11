package org.makumba.parade.access;

import java.io.IOException;
import java.util.Date;
import java.util.logging.LogRecord;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.spi.LoggingEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.ActionLog;
import org.makumba.parade.model.Log;
import org.makumba.parade.tools.PerThreadPrintStreamLogRecord;
import org.makumba.parade.tools.TriggerFilter;

public class DatabaseLogServlet extends HttpServlet {

    public void init(ServletConfig conf) {

    }

    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.removeAttribute("org.makumba.parade.servletSuccess");
        if(InitServlet.getSessionFactory() == null)
            return;
        req.setAttribute("org.makumba.parade.servletSuccess", true);
        
        // retrieve the record guy
        Object record = req.getAttribute("org.makumba.parade.servletParam");
        if (record == null)
            return;

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
        
        // finally we also need to update the ActionLog in the thread
        log.setId(actionLog.getId());
        TriggerFilter.actionLog.set(log);
        
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
            log.setDate(new Date(logevent.getStartTime()));
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
