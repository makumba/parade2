package org.makumba.parade;

public interface ServletContainer {
    public static final int NOT_INSTALLED = 0;

    public static final int STOPPED = 1;

    public static final int RUNNING = 2;

    public static final String[] status = { "not installed", "stopped",
            "running" };

    public void init(java.util.Properties config);

    public void makeConfig(java.util.Properties config,
            javax.servlet.jsp.PageContext pc);

    public int getContextStatus(String contextName);

    public String installContext(String contextName, String contextPath);

    public String unInstallContext(String contextName);

    public String startContext(String contextName);

    public String stopContext(String contextName);

    public String reloadContext(String contextName);

}
