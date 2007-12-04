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
import org.makumba.parade.model.Log;
import org.makumba.parade.tools.PerThreadPrintStream;
import org.makumba.parade.tools.PerThreadPrintStreamLogRecord;

public class DatabaseLogServlet extends HttpServlet {

    private Session s;

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
        try {
            
            s = InitServlet.getSessionFactory().openSession();

            // extract useful information from the record

            Log log = new Log();
            String prefix=(String)req.getAttribute("org.makumba.parade.logPrefix");
            log.setContext(extractContextFromMessage(prefix));
            log.setUser(extractUserFromMessage(prefix));

            // this is a java.util.logging.LogRecord
            if (record instanceof LogRecord) {
                LogRecord logrecord = (LogRecord) record;
                log.setDate(new Date(logrecord.getMillis()));
                log.setLevel(logrecord.getLevel().getName());
                log.setMessage(logrecord.getMessage());
                //log.setThrowable(logrecord.getThrown());
            } else if(record instanceof LoggingEvent) {
                LoggingEvent logevent = (LoggingEvent) record;
                log.setDate(new Date(logevent.getStartTime()));
                log.setLevel(logevent.getLevel().toString());
                log.setMessage((String)logevent.getMessage());
                //if(logevent.getThrowableInformation() != null)
                    //log.setThrowable(logevent.getThrowableInformation().getThrowable());
                //else
                    //log.setThrowable(null);
            } else if(record instanceof PerThreadPrintStreamLogRecord) {
                PerThreadPrintStreamLogRecord pRecord = (PerThreadPrintStreamLogRecord)record;
                log.setDate(pRecord.getDate());
                log.setLevel("stdout");
                log.setMessage(pRecord.getMessage());
                //log.setThrowable(null);
            }
            
            Transaction tx = s.beginTransaction();

            // write the guy to the db
            s.save(log);
            tx.commit();
            PerThreadPrintStream.canWriteToDb.set(true);
        } finally {
            // close the session in any case
            s.close();
        }
    }

    /**
     * Gets the user who triggered the event
     * 
     * @param prefix
     *            the log message that may contain a username
     * @return
     */
    private static String extractUserFromMessage(String prefix) {
        if(prefix == null)
            return null;
        String accessInfo = extractAccessInfo(prefix);

        return accessInfo.substring(0, accessInfo.indexOf("@"));
    }

    /**
     * Gets the context name from a log message
     * 
     * @param prefix
     *            the message containing the context name
     * @return a Row corresponding to the context
     */
    private static String extractContextFromMessage(String prefix) {
        
        if(prefix == null)
            return null;

        String accessInfo = extractAccessInfo(prefix);
        if (accessInfo == null)
            return null;

        return accessInfo.substring(accessInfo.indexOf("@")+1);
    }

    private static String extractAccessInfo(String s) {
        if (s.indexOf("@") > -1 && s.indexOf("[") > -1 && s.indexOf("]") > -1)
            return s.substring(s.indexOf("[")+1, s.indexOf("]"));
        else
            return null;
    }

}
