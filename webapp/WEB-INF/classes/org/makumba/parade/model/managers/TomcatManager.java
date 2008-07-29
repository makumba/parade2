package org.makumba.parade.model.managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.makumba.parade.auth.ForeignHttpAuthorizer;
import org.makumba.parade.init.ParadeProperties;

public class TomcatManager implements ServletContainer {

    String managerURL;

    String user, pass;

    Map<String, Integer> servletContextCache = new HashMap<String, Integer>();

    static Logger logger = Logger.getLogger(TomcatManager.class);

    public void makeConfig(java.util.Properties config) {
        config.put("parade.servletContext.tomcatServerName", ParadeProperties.getTomcatProperty("tomcat.server"));
        config.put("parade.servletContext.tomcatServerPort", ParadeProperties.getTomcatProperty("tomcat.http.port"));
    }

    public void init(java.util.Properties config) {
        managerURL = "http://" + ParadeProperties.getTomcatProperty("tomcat.server") + ":"
                + ParadeProperties.getTomcatProperty("tomcat.http.port") + "/manager/";
        user = ParadeProperties.getTomcatProperty("tomcat.manager.username");
        pass = ParadeProperties.getTomcatProperty("tomcat.manager.password");
    }

    protected String makeAccess(String s) {
        String result = "";
        try {
            HttpURLConnection uc = ForeignHttpAuthorizer.sendAuth(new URL(managerURL + s), user, pass);

            if (uc.getResponseCode() != 200)
                logger.error(uc.getResponseMessage());
            if (uc.getContentLength() == 0)
                logger.error("content zero");

            StringWriter sw = new StringWriter();
            InputStreamReader ir = new InputStreamReader(uc.getInputStream());
            char[] buf = new char[1024];
            int n;
            while ((n = ir.read(buf)) != -1)
                sw.write(buf, 0, n);
            result = sw.toString();
        } catch (IOException e) {
            logger.error(e);
        }
        return result;
    }

    public synchronized int getContextStatus(String contextName) {
        if (servletContextCache.isEmpty())
            readContextStatus();

        Integer stt = servletContextCache.get(contextName);
        if (stt == null)
            return NOT_INSTALLED;
        return stt.intValue();
    }

    void readContextStatus() {
        servletContextCache = new HashMap<String, Integer>();
        StringTokenizer st = new StringTokenizer(makeAccess("list"), "\n");
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (s.startsWith("OK"))
                continue;
            int n = s.indexOf(":");
            int m;
            if (n != -1 && (m = s.indexOf(":", n + 1)) != -1) {
                String contextName = s.substring(0, n);
                String status = s.substring(n + 1, m);
                if (status.equals("stopped")) {
                    servletContextCache.put(contextName, new Integer(STOPPED));
                    continue;
                }
                if (status.equals("running")) {
                    servletContextCache.put(contextName, new Integer(RUNNING));
                    continue;
                }
            }
            throw new RuntimeException("Cannot understand context status list:\n" + s);
        }
    }

    public synchronized String installContext(String contextName, String contextPath) {
        if (getContextStatus(contextName) != NOT_INSTALLED)
            return "context " + contextName + " already installed";
        try {

            String dir = ".";
            String context = new File(contextPath).getCanonicalPath();
            String canDir = null;
            File f;
            while ((f = new File(dir)).exists() && !context.startsWith(canDir = f.getCanonicalPath()))
                dir += File.separator + "..";

            if (!f.exists())
                throw new RuntimeException("cannot find common root to context");
            
            if(contextName == null)
                throw new RuntimeException("cannot install context, context name was null");

            File deployer = File.createTempFile("parade-deploy", ".xml");
            BufferedWriter out = new BufferedWriter(new FileWriter(deployer));
            out.write("<Context path=\"");
            out.write(contextName);
            out.write("\" docBase=\"");
            // out.write(ParadeProperties.paradeBaseRelativeToTomcatWebapps);
            out.write(File.separator);
            out.write((new java.io.File(dir)).getCanonicalPath());
            out.write(File.separator);
            out.write(context.substring(canDir.length()));
            out.write("\" reload=\"true\" debug=\"0\" crossContext=\"true\"></Context>");
            out.flush();
            out.close();

            // needed for tomcat > 5.5.7
            // FIXME: should make sure that the engine and hostname are the same as in tomcat config
            new File("tomcat/conf/Catalina/localhost".replace('/', File.separatorChar)).mkdirs();

            String s = makeAccess("deploy?config=file:/" + deployer.getAbsolutePath() + "&path=" + contextName);
            deployer.delete();

            if (s.startsWith("OK")) {
                servletContextCache.put(contextName, new Integer(RUNNING));
                String s1 = stopContext(contextName);
                if (s1.startsWith("OK"))
                    s += "<br>" + startContext(contextName);
                else {
                    s += "<br>Attempting to stop and start " + contextName
                            + " for checking correct installation failed " + pleaseCheck(s1);
                    servletContextCache.put(contextName, new Integer(STOPPED));
                }
            }
            return s;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public synchronized String unInstallContext(String contextName) {
        if (getContextStatus(contextName) == NOT_INSTALLED)
            return "context " + contextName + " not installed";
        String s = makeAccess("remove?path=" + contextName);
        if (s.startsWith("OK"))
            servletContextCache.remove(contextName);
        return s;
    }

    public synchronized String startContext(String contextName) {
        if (getContextStatus(contextName) == RUNNING)
            return "context " + contextName + " already running";
        if (getContextStatus(contextName) == NOT_INSTALLED)
            return "context " + contextName + " not installed";
        String s = makeAccess("start?path=" + contextName);
        if (s.startsWith("OK"))
            servletContextCache.put(contextName, new Integer(RUNNING));
        else
            s = "Could not start " + contextName + ". " + pleaseCheck(s);
        return s;
    }

    public synchronized String stopContext(String contextName) {
        if (getContextStatus(contextName) == STOPPED)
            return "context " + contextName + " already stopped";
        if (getContextStatus(contextName) == NOT_INSTALLED)
            return "context " + contextName + " not installed";
        String s = makeAccess("stop?path=" + contextName);
        if (s.startsWith("OK"))
            servletContextCache.put(contextName, new Integer(STOPPED));
        return s;
    }

    public synchronized String reloadContext(String contextName) {
        if (getContextStatus(contextName) == STOPPED)
            return "context " + contextName + " stopped";
        if (getContextStatus(contextName) == NOT_INSTALLED)
            return "context " + contextName + " not installed";
        String s = makeAccess("reload?path=" + contextName);
        if (s.startsWith("OK"))
            servletContextCache.put(contextName, new Integer(RUNNING));
        else
            s = "Could not reload " + contextName + ". " + pleaseCheck(s);
        return s;
    }
    
    public synchronized String redeployContext(String contextName, String contextPath) {
        String s = unInstallContext(contextName);
        String s1 = "";
        if (s.startsWith("OK")) {
            s1 = s;
            s = installContext(contextName, contextPath);
        }
        if(s.startsWith("OK")) {
            s = s1 + "<br>" + s;
            servletContextCache.put(contextName, new Integer(RUNNING));
            
        } else {
            s = "Could not redeploy " + contextName + ". " + pleaseCheck(s);
        }
        return s;
    }


    static String pleaseCheck(String s) {
        return "Please check the output on the Parade log. "
                + "Make sure that the filters and servlets declared in web.xml exist and are compiled. "
                + "Make sure that the tag libraries declared in web.xml exist. "
                + "A complete CVS update followed by 'clean' and 'compile' might help. "
                + "<br>Message from Tomcat (may be uncomplete) is: " + s;
    }

}
