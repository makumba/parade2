package org.makumba.parade;

import java.util.HashMap;
import java.util.Map;

public class ParadeProperties {
	
	static Map conf = new HashMap();
	
	private static void init() {
	
		//has maybe to be generated automcatically
		conf.put("paradeBase","e:/bundle/server/parade2/");
		
		
		conf.put("tomcat.output", "tomcat/logs");
		
		conf.put("parade.authorizerClass","");
		conf.put("parade.authorizationMessage","Please enter your username and password");
		conf.put("parade.authorizationDB","");
		
		/*
		boolean RowStore = true;
		boolean FileManager = false;
		boolean CvsManager = false;
		boolean ServletContextManager = true;
		boolean AntManager = false;
		boolean MakumbaManager = false;
		*/
		
	}
	
	public static Map getConf() {
		init();
		return conf;
	
	}
	
}
