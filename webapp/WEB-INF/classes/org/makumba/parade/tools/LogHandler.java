package org.makumba.parade.tools;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.makumba.parade.access.ActionLogDTO;
import org.makumba.parade.init.InitServlet;

/**
 * The LogHandler handles an advanced logging mechanism. For each access, an
 * {@link ActionLogDTO} object is created and passed to the beforeServlet, which populates it with relavant information
 * (e.g. the username of the person who did the access). The ActionLogDTO instance is then persisted a first time by the
 * DatabaseLogServlet, which ensures that the log is written to the database. The ActionLogDTO is as well hold in a
 * ThreadLocal so as to be accessible by other classes at any time during the access.<br>
 * The redirectToServlet() method enables any classes using it to redirect an object to a servlet (typically the
 * DatabaseLogServlet) so that they can be for instance persisted.<br>
 * Before performing the redirection, the TriggerFilter makes sure that there's an ActionLogDTO instance and tries to
 * heuristically determine the accessed context (or the context by which the access is made).<br>
 * Another point worth mentioning is that if the DatabaseLogServlet is not ready to log yet, the incoming log events
 * are being queued and then flushed as soon as the servlet is ready. <br>
 * This is probably the most complex mechanism of the whole application.<br>
 * <br>
 * 
 * @author Cristian Bogdan
 * @author Manuel Gay
 * @author Koen Speelmeijer
 */


public class LogHandler {
    // we need a context to be able to find others...
    private static ServletContext staticContext;

    private static ServletContext staticRootCtx;

    public static ThreadLocal<ActionLogDTO> actionLog = new ThreadLocal<ActionLogDTO>();

    private static ThreadLocal<ActionLogDTO> tomcatActionLog = new ThreadLocal<ActionLogDTO>();

    public static ThreadLocal<String> prefix = new ThreadLocal<String>();

    // guard that makes sure that we don't enter in an infinite logging loop
    private static ThreadLocal guard = new ThreadLocal() {
        @Override
        public Object initialValue() {
            return false;
        }
    };

    public static ThreadLocal<Boolean> shutDown = new ThreadLocal<Boolean>() {
        @Override
        public Boolean initialValue() {
            return new Boolean(false);
        }
    };
    
    public static void setStaticContext(ServletContext context) {
        staticContext = context;
    }
    
    public static void setCTX(ServletContext ctx) {
        staticRootCtx = ctx;
    }
    
    public static void setActionLog(ActionLogDTO log) {
        actionLog.set(log);
    }
    
    public static RequestDispatcher getRequestDispatcher(String pageURI) {
        return staticRootCtx.getRequestDispatcher(pageURI);
    }

    public static void invokeServlet(String servletName, ServletContext ctx, ServletRequest req, ServletResponse resp)
            throws java.io.IOException, ServletException {

        ctx.getRequestDispatcher(servletName).include(req, resp);
    }
    
    // we need a vector so adding from multiple threads simultaneously is safe
    static Vector<LogHandlerQueueData> queue = new Vector<LogHandlerQueueData>();
    
    static {
        Object[] record = { new java.util.Date(), "Server restart" };
        LogHandlerQueueData restart = new LogHandlerQueueData(
                "/servlet/org.makumba.parade.access.DatabaseLogServlet", record);
        queue.add(restart);
    }
    
    /**
     * Issues a request to a given servlet with a given object as attribute. This mechanism is for now only used by the
     * DatabaseLogServlet. Makes sure that an ActionLogDTO is available in the ThreadLocal and heuristically determines
     * the origin context of the access.
     * 
     * @param servletName
     *            the name (or path) to the servlet to be invoked
     * @param attributeValue
     *            the value of the attribute to pass
     */
    public static void redirectToServlet(String servletName, Object attributeValue) {
    
        if (guard.get().equals(false)) {
            guard.set(true);
    
            ActionLogDTO l = actionLog.get();
    
            // if we're shutting down tomcat, we stop logging, or tomcat can't shutdown anymore
            if (shutDown.get()) {
                return;
            }
    
            ActionLogDTO log = computeActionLogAndSetPrefix(attributeValue);
    
            try {
    
                // if this is a new ActionLog we log it first
                if (log != l) {
                    directSendToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", log);
                }
    
                directSendToServlet(servletName, attributeValue);
    
            } finally {
                guard.set(false);
            }
        }
    }
    
    public static ActionLogDTO computeActionLogAndSetPrefix(Object attributeValue) {
        ActionLogDTO log = computeHeuristicContextInformation(attributeValue);
        setPrefix();
        return log;
    }
    
    private static void directSendToServlet(String servletName, Object attributeValue) {
        LogHandlerQueueData data = new LogHandlerQueueData(servletName, attributeValue);
        if (staticRootCtx != null) {
            if(!data.sendTo(staticRootCtx)) {
                queue.add(data);
            }
        } else {
            // this happens only in the beginning
            computeStaticRoot(data);
        }
    }
    
    private static final String WEBAPP_CLASSLOADER = "WebappClassLoader";
    
    private static final String TOMCAT_STARTUP = "org.apache.catalina.startup.Bootstrap.load";
    
    private static final String TOMCAT_SHUTDOWN = "org.apache.catalina.startup.Catalina.stop";
    
    private static final String MANAGER_DEPLOY = "Manager: install: Installing context configuration at";
    
    private static final String MANAGER_UNDEPLOY = "Manager: undeploy: Undeploying web application at '/";
    
    private static final String MANAGER_START = "Manager: start: Starting web application at '/";
    
    private static final String MANAGER_STOP = "Manager: stop: Stopping web application at '/";
    
    private static final String MANAGER_INIT = "Manager: init:";
    
    private static final String MANAGER_LIST = "Manager: list:";
    
    private static final String ROOT_XML_ERROR = "Error deploying configuration descriptor ROOT.xml";
    
    /**
     * Tries to compute the context information, based on the thread
     * 
     * @param log
     *            the ActionLogDTO in which to place the computed info
     */
    private static ActionLogDTO computeHeuristicContextInformation(Object attributeValue) {
    
        ActionLogDTO log = actionLog.get();
    
        String threadName = Thread.currentThread().getName();
        
        if(Thread.currentThread() == null || Thread.currentThread().getContextClassLoader() == null) {
            handleOtherCases(log);
            return log;
        }
        
        String classLoaderName = Thread.currentThread().getContextClassLoader().toString();
        
        // let's figure out here if this is tomcat doing some stuff
        if (threadName.equals("main")) {
    
            // log = createEmptyActionLogDTO("system");
    
            // if we don't have a tomcatActionLog t1his means that probably we didn't start tomcat yet
            if (tomcatActionLog.get() == null && log == null) {
    
                log = createEmptyActionLogDTO("system");
    
                StringWriter s = new StringWriter();
                new Throwable().printStackTrace(new PrintWriter(s));
    
                if (s.toString().indexOf(TOMCAT_STARTUP) > -1) {
                    // yes, it's definitely tomcat starting
    
                    log.setAction("start");
                    log.setOrigin("tomcat");
    
                    // we also want to keep this guy for the record
                    tomcatActionLog.set(log);
                } else {
                    // now this is strange...
                    PerThreadPrintStream.oldSystemOut
                            .println("HEURISTIC: I thought that tomcat was just starting up, but I couldn't detect it in the stacktrace. help!");
                }
                // anyway, let's set our new log
                actionLog.set(log);
    
            } else if (tomcatActionLog.get() != null) {
                // we are sure that tomcat is started
    
                StringWriter s = new StringWriter();
                new Throwable().printStackTrace(new PrintWriter(s));
    
                if (s.toString().indexOf(TOMCAT_SHUTDOWN) > -1) {
    
                    // tomcat shutting down, we want to register that
    
                    if (log != null && !log.getAction().equals("stopping")) {
                        log = createEmptyActionLogDTO("system");
                        log.setAction("stopping");
                        log.setOrigin("tomcat");
                        actionLog.set(log);
                        shutDown.set(new Boolean(true));
                        PerThreadPrintStream.setEnabled(false);
                    } else if (log != null && log.getAction().equals("stopping")) {
                        // we already logged that.
                        return log;
                    }
                } else {
                    handleOtherCases(log);
                }
            }
        } else if (!threadName.equals("main")) {
            // we are in another thread
            // and probably it's going to be some webapp
    
            if (classLoaderName.indexOf(WEBAPP_CLASSLOADER) > -1) {
    
                // ok, let's try to figure out what happens
                // let's first try to see if this is the manager
    
                if (attributeValue instanceof LogRecord) {
                    LogRecord record = (LogRecord) attributeValue;
                    String message = record.getMessage();
    
                    if (message.indexOf(MANAGER_DEPLOY) > -1) {
                        // we are installing some context
                        log = createEmptyActionLogDTO("manager");
                        log.setAction("deploying");
                        log.setOrigin("tomcat");
    
                        // TODO: read in the deploy file and fetch the context
                        // then store it in the log
    
                        message = message.substring(MANAGER_DEPLOY.length() + " 'file:".length());
                        message = message.substring(0, message.indexOf("'"));
    
                        String contextPath = null;
                        BufferedReader r = null;
                        try {
                            r = new BufferedReader(new FileReader(new java.io.File(message)));
                            String line = r.readLine();
                            contextPath = line.substring(line.indexOf("path=\"") + 6);
                            contextPath = contextPath.substring(0, contextPath.indexOf("\"")).substring(1);
                            r.close();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (contextPath != null)
                            log.setContext(contextPath);
    
                        actionLog.set(log);
                    } else if (message.indexOf(MANAGER_UNDEPLOY) > -1) {
                        // the manager is undeploying someone
                        log = createEmptyActionLogDTO("manager");
                        log.setAction("undeploying");
                        message = message.substring(MANAGER_UNDEPLOY.length());
                        log.setContext(message.substring(0, message.indexOf("'")));
                        log.setOrigin("tomcat");
                        actionLog.set(log);
                    } else if (message.indexOf(MANAGER_START) > -1) {
                        // the manager is starting someone
                        log = createEmptyActionLogDTO("manager");
                        log.setAction("starting");
                        message = message.substring(MANAGER_START.length());
                        log.setContext(message.substring(0, message.indexOf("'")));
                        log.setOrigin("tomcat");
                        actionLog.set(log);
                    } else if (message.indexOf(MANAGER_STOP) > -1) {
                        // the manager is stopping someone
                        log = createEmptyActionLogDTO("manager");
                        log.setAction("stopping");
                        message = message.substring(MANAGER_STOP.length());
                        log.setContext(message.substring(0, message.indexOf("'")));
                        log.setOrigin("tomcat");
                        actionLog.set(log);
                    } else if (message.indexOf(MANAGER_LIST) > -1) {
                        // the manager is listing the contexts
                        log = createEmptyActionLogDTO("manager");
                        log.setAction("listing contexts");
                        log.setOrigin("tomcat");
                        actionLog.set(log);
                    } else if (message.indexOf(MANAGER_INIT) > -1) {
                        // the manager is initialising
                        log = createEmptyActionLogDTO("manager");
                        log.setAction("initialising");
                        log.setOrigin("tomcat");
                        actionLog.set(log);
                    } else {
                        handleOtherCases(log);
                    }
                } else {
                    handleOtherCases(log);
                }
    
                handleOtherCases(log);
    
            } else {
                LogRecord record = (LogRecord) attributeValue;
                
                if(record.getMessage().contains(ROOT_XML_ERROR)) {
                    log = createEmptyActionLogDTO("tomcat");
                    log.setAction("initialising");
                    log.setOrigin("tomcat");
                    actionLog.set(log);
    
                    Logger logger = ParadeLogger.getParadeLogger(TriggerFilter.class.getName());
                    logger.severe(record.getThrown().getMessage());
                    
                    try {
                        InitServlet.createDummyDatabaseConnection();
                    } catch(Throwable e) {
                        logger.severe("Error connecting to database. MySQL.jar might be missing or corrupt.");
                        InitServlet.shutdownTomcat();
                        throw new Error("Could not initialize tomcat. Shutdown.");
                    }
                } else {
                    handleOtherCases(log);
                }
            }
        }
    
        return log;
    
    }

    private static void handleOtherCases(ActionLogDTO log) {
        if (log == null) {
            log = createEmptyActionLogDTO("system-u"); // "system-unknown"
            log.setContext("parade2");
            actionLog.set(log);
        }
    }
    
    /**
     * Creates an empty ActionLogDTO with the given username
     * 
     * @param user
     *            the username
     * @return an ActionLogDTO containing a date and username
     */
    private static ActionLogDTO createEmptyActionLogDTO(String user) {
        ActionLogDTO log;
        log = new ActionLogDTO();
        log.setDate(new Date());
        log.setUser(user);
        return log;
    }
    
    /**
     * Attempts to compute a static root context. Adds elements to be logged to the queue and flushes as soon as we have
     * a root context.
     * 
     * @param data
     *            the {@link LogHandlerQueueData} object holding the information to be logged
     */
    private static synchronized void computeStaticRoot(LogHandlerQueueData data) {
        if (staticRootCtx != null) {
            // probably staticRootCtx was set just after we checked for it and just before we came into this method
            data.sendTo(staticRootCtx);
            return;
        }
        ServletContext ctx = null;
    
        queue.add(data);
        // here queue has at least one member!
    
        if ((ctx = staticContext) == null)
            // || (ctx= staticContext.getContext("/"))==null )
            return;
        
        // we have a root context, we try to send the first guy
        Iterator<LogHandlerQueueData> i = queue.iterator();
        if (!i.next().sendTo(ctx))
            // root context is not ready to process
            return;
        // jackpot! the root context exists and is ready for action. we publish all shit before
        for (; i.hasNext();)
            i.next().sendTo(ctx);
    
        // now we are ready to publish the static so all other losers can use it without coming into synchronized code
        staticRootCtx = ctx;
    }
    
    /**
     * Based on the information in the ActionLogDTO, computes the prefix to appear in the logs:
     * <ul>
     * <li><em>user@context</em> in case this information is there</li>
     * <li><em>user@parade2</em> if the context could not be computer (or the context is parade)</li>
     * <li><em>(unknown user)@context</em> if the user is not there</li>
     * <li><em>tomcat</em> if it is tomcat performing an operation</li>
     * </ul>
     * 
     */
    public static void setPrefix() {
        String prefix = null;
        if (actionLog.get() != null) {
            String context = actionLog.get().getContext();
            String user = actionLog.get().getUser();
            if (context == null || context.equals("null")) {
                if (actionLog.get().getOrigin() != null) {
                    prefix = "[" + actionLog.get().getOrigin() + "]";
                } else {
                    context = "parade2";
                }
            }
            if (user == null || user.equals("null"))
                user = "(unknown user)";
    
            if (prefix == null)
                prefix = "[" + user + "@" + context + "]";
        } else {
            prefix = "(unknown user)@parade2";
        }
    
        LogHandler.prefix.set(prefix);
    }
    
    /**
     * This class stores information useful for the queuing mechanism described before.
     * 
     */
    static class LogHandlerQueueData {
    
        LogHandlerQueueData(String servletName, Object attributeValue) {
            this.servletName = servletName;
            this.attributeValue = attributeValue;
            setPrefix();
            this.prefix = LogHandler.prefix.get();
        }
    
        /**
         * Tries to invoke the stored servlet using a given rootContext
         * 
         * @param rootCtx
         *            the root ServletContext to be used for invocation
         * @return <code>true</code> if the org.makumba.parade.servletSuccess attribute was set by the invoked servlet,
         *         <code>false</code> otherwise
         */
        boolean sendTo(ServletContext rootCtx) {
            ServletRequest req = new HttpServletRequestDummy();
            if (attributeValue != null)
                req.setAttribute("org.makumba.parade.servletParam", attributeValue);
            if (prefix != null)
                req.setAttribute("org.makumba.parade.logPrefix", prefix);
            try {
                invokeServlet(servletName, rootCtx, req, new HttpServletResponseDummy());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ServletException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return req.getAttribute("org.makumba.parade.servletSuccess") != null;
        }
    
        private String prefix;
    
        private String servletName;
    
        private Object attributeValue;
    
    }

}
