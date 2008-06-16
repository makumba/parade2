package org.makumba.parade.model.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.makumba.parade.init.ParadeProperties;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowWebapp;
import org.makumba.parade.model.interfaces.ParadeManager;
import org.makumba.parade.model.interfaces.RowRefresher;

public class WebappManager implements RowRefresher, ParadeManager {

    static String reloadLog = ParadeProperties.getParadeBase() + java.io.File.separator + "tomcat"
            + java.io.File.separator + "logs" + java.io.File.separator + "paradeReloadResult.txt";

    ServletContainer container;

    Properties config;

    private static String DEFAULT_SERVLETCONTEXTPROPERTIES = "/servletContext.properties";

    static Logger logger = Logger.getLogger(WebappManager.class.getName());

    {
        loadConfig();
    }

    public void newRow(String name, Row r, Map<String, String> m) {

        RowWebapp webappdata = new RowWebapp();
        webappdata.setDataType("webapp");
        webappdata.setWebappPath(m.get("webapp") == null?"":m.get("webapp"));
        r.addManagerData(webappdata);
    }

    public void softRefresh(Row row) {
        logger.debug("Refreshing row information for row " + row.getRowname());

        RowWebapp webappdata = (RowWebapp) row.getRowdata().get("webapp");

        setWebappInfo(row, webappdata);
        row.addManagerData(webappdata);
    }

    public void hardRefresh(Row row) {
        softRefresh(row);
    }

    private void loadConfig() {
        try {
            config = new Properties();
            config.load(this.getClass().getResourceAsStream(DEFAULT_SERVLETCONTEXTPROPERTIES));
        } catch (Throwable t) {
            logger.error("Error loading servletcontext.properties", t);
        }
    }

    private synchronized ServletContainer getServletContainer() {
        if (container == null)
            try {
                container = (ServletContainer) ParadeProperties.class.getClassLoader().loadClass(
                        config.getProperty("parade.servletContext.servletContainer")).newInstance();

                config.put("parade.servletContext.paradeContext", new File(ParadeProperties.getParadeBase())
                        .getCanonicalPath());
                container.makeConfig(config);
                config.store(new FileOutputStream(ParadeProperties.getClassesPath() + java.io.File.separator
                        + "servletcontext.properties"), "Parade servlet context config");
                container.init(config);
            } catch (Throwable t) {
                logger.error("Error getting servlet container", t);
            }

        return container;
    }

    /* stores information about Row's servletContext */
    public void setWebappInfo(Row row, RowWebapp webappdata) {

        // checks if there's a WEB-INF dir
        String webinfDir = row.getRowpath() + java.io.File.separator + webappdata.getWebappPath()
                + java.io.File.separator + "WEB-INF";

        if (!new java.io.File(webinfDir).isDirectory()) {
            logger.warn("No WEB-INF directory found for row " + row.getRowname() + ": directory " + webinfDir
                    + " does not exist");
            webinfDir = "NO WEBINF";
            webappdata.setWebappPath("NO WEBINF");
            webappdata.setStatus(new Integer(ServletContainer.NOT_INSTALLED));
        }
        if (!webinfDir.equals("NO WEBINF")) {
            if (row.getRowname().equals("(root)")) {
                webappdata.setContextname("/");
            } else {
                webappdata.setContextname("/" + row.getRowname());
            }
            webappdata.setStatus(new Integer(getServletContainer().getContextStatus(webappdata.getContextname())));
        }
    }

    public String servletContextStartRow(Row row) {
        RowWebapp data = (RowWebapp) row.getRowdata().get("webapp");

        if (!isParadeCheck(row)) {
            String result = getServletContainer().startContext(data.getContextname());
            setWebappInfo(row, data);
            return result;
        }

        return "Error: ParaDe is already running";
    }

    public String servletContextStopRow(Row row) {
        RowWebapp data = (RowWebapp) row.getRowdata().get("webapp");

        if (!isParadeCheck(row)) {
            String result = getServletContainer().stopContext(data.getContextname());
            setWebappInfo(row, data);
            return result;
        }

        return "Error: you cannot stop ParaDe like this !";
    }
    
    public String servletContextRedeployRow(Row row) {
        RowWebapp data = (RowWebapp) row.getRowdata().get("webapp");

        String result = "";
        if (!isParade(row)) {
            String webapp = row.getRowpath() + File.separator + data.getWebappPath();
            result = getServletContainer().redeployContext(data.getContextname(), webapp);
        } else {
            result = "Error: you cannot redeploy ParaDe this way!";
        }
        return result;
        
    }

    public String servletContextReloadRow(Row row) {
        RowWebapp data = (RowWebapp) row.getRowdata().get("webapp");

        String result = "";

        // must check if it's not this one
        if (!isParade(row)) {
            result = getServletContainer().reloadContext(data.getContextname());
        } else {
            try {
                String antCommand = "ant";

                File f = new File(reloadLog);
                Runtime.getRuntime().exec(
                        antCommand + " -buildfile " + ParadeProperties.getParadeBase() + java.io.File.separator
                                + "build.xml reload");
                f.delete();

                while (!f.exists()) {
                    try {
                        Thread.sleep(100);
                    } catch (Throwable t) {
                        logger.warn("Context reload thread sleep failed");
                    }
                }

                loadConfig();
                // TODO make this work
                result = config.getProperty("parade.servletContext.selfReloadWait");

            } catch (IOException e) {
                result = "Error: Cannot reload Parade " + e;
                logger.error("Cannot reload ParaDe", e);
            }
        }

        return result;
    }

    public String servletContextInstallRow(Row row) {
        RowWebapp data = (RowWebapp) row.getRowdata().get("webapp");

        if (!isParadeCheck(row)) {
            String webapp = row.getRowpath() + File.separator + data.getWebappPath();
            String result = getServletContainer().installContext(data.getContextname(), webapp);
            setWebappInfo(row, data);
            return result;
        }
        return "Error: ParaDe should not be installed in this way !";
    }

    public String servletContextRemoveRow(Row row) {
        RowWebapp data = (RowWebapp) row.getRowdata().get("webapp");

        if (!isParadeCheck(row)) {
            String result = getServletContainer().unInstallContext(data.getContextname());
            setWebappInfo(row, data);
            return result;
        }
        return "Error: you cannot uninstall ParaDe !";
    }

    private boolean isParadeCheck(Row row) {
        if (isParade(row)) {
            // row.put("result", "You can only reload Parade!");
            return true;
        }
        return false;
    }

    private boolean isParade(Row row) {
        try {
            return row.getRowpath().equals(new File(ParadeProperties.getParadeBase()).getCanonicalPath());
        } catch (Throwable t) {
            logger.error("Error: couldn't get row path", t);
        }
        return true;
    }


}
