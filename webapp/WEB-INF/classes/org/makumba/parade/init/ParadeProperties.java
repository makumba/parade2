package org.makumba.parade.init;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.makumba.commons.RuntimeWrappedException;
import org.makumba.parade.tools.ParadeLogger;

public class ParadeProperties {

    static String DEFAULT_PROPERTYFILE = "/parade.properties";

    static String DEFAULT_TOMCATPROPERTYFILE = "/tomcat.properties";

    private static Properties paradeConfig;

    private static Properties tomcatConfig;

    static Logger logger = ParadeLogger.getParadeLogger(ParadeProperties.class.getName());

    static {

        try {
            paradeConfig = new Properties();
            paradeConfig.load(ParadeProperties.class.getResourceAsStream(DEFAULT_PROPERTYFILE));

        } catch (Throwable t) {
            logger
                    .severe("Error while loading parade.properties. Make sure you have configured a parade.properties in webapp/WEB-INF/classes (you can copy the example file)");
        }

        try {
            tomcatConfig = new Properties();
            tomcatConfig.load(new FileInputStream(new java.io.File(getParadeBase()) + DEFAULT_TOMCATPROPERTYFILE));

        } catch (Throwable t) {
            logger
                    .severe("Error while loading tomcat.properties. Make sure you have configured a tomcat.properties in parade's root dir (you can copy the example file)");

        }

    }

    public static String getParadeProperty(String configProperty) {
        return paradeConfig.getProperty(configProperty);
    }

    public static String getTomcatProperty(String configProperty) {
        return tomcatConfig.getProperty(configProperty);
    }

    public static List<String> getElements(String configProperty) {
        List<String> l = new LinkedList<String>();

        String s = getParadeProperty(configProperty);
        if (s == null)
            return null;
        StringTokenizer st = new StringTokenizer(s, ",");
        while (st.hasMoreElements()) {
            l.add((st.nextToken()).trim());
        }
        return l;
    }

    public static String getParadeBase() {

        String paradeBase = ".\\";
        try {
            paradeBase = new java.io.File("." + java.io.File.separator).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paradeBase;
    }

    public static String getClassesPath() {
        URL u = RowProperties.class.getResource(DEFAULT_PROPERTYFILE);
        if(u != null) {
            return new java.io.File(u.getPath()).getParent();
        } else {
            throw new RuntimeException("Could not find parade.properties resource. Make sure you configured it in webapp/WEB-INF-classes !");
        }
    }

}
