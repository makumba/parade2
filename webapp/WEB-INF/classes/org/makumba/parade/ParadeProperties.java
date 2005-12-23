package org.makumba.parade;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ParadeProperties {
	
	static public String paradeBase = "." + java.io.File.separator;
	static String fileName = paradeBase + "parade.properties";
	
	private static Properties config;
	
	static public String paradeBaseRelativeToTomcatWebapps = ".."
        + File.separator + "..";
	
	static Logger logger = Logger.getLogger(ParadeProperties.class.getName());
	
	static {
		
        try {
            config = new Properties();
            config.load(new FileInputStream(fileName));
        } catch (Throwable t) {
            logger.error("Error while loading parade.properties",t);
        }
    }
	
	public static String getProperty(String configProperty) {
        return config.getProperty(configProperty);
    }
	
	
	
}
