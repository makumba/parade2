package org.makumba.parade.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.makumba.parade.ParadeProperties;
import org.makumba.parade.ifc.RowRefresher;
import org.makumba.parade.ifc.ServletContainer;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowCVS;
import org.makumba.parade.model.RowWebapp;

public class WebappManager implements RowRefresher {
	
	static String reloadLog = (String) ParadeProperties.getProperty("paradeBase") + "tomcat" + java.io.File.separator
    + "logs" + java.io.File.separator + "paradeReloadResult.txt";

	ServletContainer container;
	
	Map containerInitParams;
	
	Properties config;
	
	String fileName = (String) ParadeProperties.paradeBase + "servletContext.properties";
	
	Map webinfCache = new HashMap();
	
	static Logger logger = Logger.getLogger(WebappManager.class.getName());
	
	{
		loadConfig();
	}
	
	
	public void rowRefresh(Row row) {
		
		RowWebapp webappdata = new RowWebapp();
		webappdata.setDataType("webapp");
		
		setWebappInfo(row,webappdata);
		
		RowStoreManager rowMgr = new RowStoreManager();
		rowMgr.addManagerData(webappdata,row);
		
	}
	
	void loadConfig() {
		try {
		    config = new Properties();
		    config.load(new FileInputStream(fileName));
		} catch (Throwable t) {
		    logger.error("Error loading servletcontext.properties",t);
		}
	}
	
	synchronized ServletContainer getServletContainer() {
		if (container == null)
			try {
		        container = (ServletContainer) ParadeProperties.class
		                .getClassLoader()
		                .loadClass(config.getProperty("parade.servletContext.servletContainer"))
		                .newInstance();
		        
			    config.put("parade.servletContext.paradeContext",new File(ParadeProperties.paradeBase).getCanonicalPath());
			    container.makeConfig(config);
			    config.store(new FileOutputStream(fileName),"Parade servlet context config");
			    container.init(config);			    
		    } catch (Throwable t) {
		        logger.error("Error getting servlet container",t);
		    }
		    
		return container;
		}
	
	
	/* stores information about Row's servletContext */
	public void setWebappInfo(Row row, RowWebapp webappdata) {
		String p = row.getRowpath();
		String s = null;
		synchronized (webinfCache) {
			s = (String) webinfCache.get(p);
		    if (s == null || !new File(p + java.io.File.separator + s).isDirectory()) {
		    	webinfCache.put(p, s = searchWebinf(p, new File(p)));
		    }
		}
		if (!s.equals("NO WEBINF")) {
			webappdata.setContextpath(s);
			webappdata.setContextname("/" + row.getRowname());
			webappdata.setStatus(new Integer(getServletContainer().getContextStatus(webappdata.getContextname())));	
		}
	}
	
	
	public String servletContextStartRow(Row row) {
		
		//TODO not sure if Hibernate likes this!
		RowWebapp data = (RowWebapp) row.getRowdata().get("webapp");
		setWebappInfo(row, data);
		
		if (!isParadeCheck(row)) return getServletContainer().startContext(data.getContextname());
		return "ParaDe is already running";
	}
	
	
	public String servletContextStopRow(Row row) {
		//TODO not sure if Hibernate likes this!
		RowWebapp data = (RowWebapp) row.getRowdata().get("webapp");
		setWebappInfo(row, data);
		if (!isParadeCheck(row)) return getServletContainer().stopContext(data.getContextname());
		return "Internal error";
	}
	
	public String[] servletContextReloadRow(Row row) {
//		TODO not sure if Hibernate likes this!
		setWebappInfo(row, (RowWebapp) row.getRowdata().get("webapp"));
		
		String[] result = new String[2];
		
		// must check if it's not this one
		if (!isParade(row)) {
			result[0] = getServletContainer().reloadContext(row.getRowname());
			result[1] = "0";
		} else {
		    try {
		        String antCommand = "ant";
		
		        if (System.getProperty("os.name").toLowerCase().indexOf(
		                "windows") != -1)
		            antCommand = "ant.bat";
		
		        File f = new File(reloadLog);
		        f.delete();
		        Runtime.getRuntime().exec(
		                antCommand + " -buildfile " + ParadeProperties.paradeBase
		                        + "build.xml reload");
		        
		        while (!f.exists()) {
		        	try {
		                Thread.currentThread().sleep(100);
		            } catch (Throwable t) {
		            	logger.warn("Context reload thread sleep failed");
		            }
		        }
		            
		        loadConfig();
		        //TODO make this work
		        result[1] = config.getProperty("parade.servletContext.selfReloadWait");
		        
		
		    } catch (IOException e) {
		        result[0] = "Cannot reload Parade " + e;
		        logger.error("Cannot reload ParaDe",e);
		    }
		}
		
		return result;
	}
	
	public String servletContextInstallSimple(Row row) {
//		TODO not sure if Hibernate likes this!
		RowWebapp data = (RowWebapp) row.getRowdata().get("webapp");
		setWebappInfo(row, data);
		
		if (!isParadeCheck(row)) {
			return getServletContainer().installContext(
					row.getRowname(),
					row.getParade().getParadeBase()
					+ java.io.File.separator
					+ java.io.File.separator
					+ data.getContextpath()
					);
		}
		return "Internal error";
	}
	
	public String servletContextRemoveRow(Row row) {
//		TODO not sure if Hibernate likes this!
		RowWebapp data = (RowWebapp) row.getRowdata().get("webapp");
		setWebappInfo(row, data);
		
		if (!isParadeCheck(row)) return getServletContainer().unInstallContext(data.getContextname());
		return "Internal error";
	}
	
	/* locates WEB-INF within a given path */
	public static String searchWebinf(String original, java.io.File p) {
	String[] s = p.list();
	if (s == null)
	    throw new RuntimeException(original + " is not a correct pathname.");
	
	for (int i = 0; i < s.length; i++) {
	    java.io.File f = new java.io.File(p, s[i]);
	    if (f.isDirectory()
	            && !f.getName().equals("serialized")
	            && f.toString().indexOf("tomcat" + java.io.File.separator + "logs") == -1) {
	        if (f.getName().equals("WEB-INF")
	                && new File(f, "web.xml").exists()) {
	            if (f.getParent().toString().equals(original))
	                return ".";
	            return (f.getParent().toString().substring(original
	                    .length() + 1)).replace(File.separatorChar, '/');
	        } else {
	            String ret = searchWebinf(original, f);
	            if (!ret.equals("NO WEBINF"))
	                return ret;
	        }
	    }
	}
	return "NO WEBINF";
	}
	
	public static boolean isParadeCheck(Row row) {
		if (isParade(row)) {
		    //row.put("result", "You can only reload Parade!");
		    return true;
		}
		return false;
	}
	
	public static boolean isParade(Row row) {
		try {
		    return row.getRowpath().equals(new File(ParadeProperties.paradeBase).getCanonicalPath());
		} catch (Throwable t) {
			logger.error("Internal error: couldn't get row path",t);
		}
		return true;
	}
	
	public static void main(String[] argv) throws IOException {
		WebappManager s = new WebappManager();
		PrintStream ps = new PrintStream(new FileOutputStream(reloadLog));
		ps.flush();
		ps
		        .println(s
		                .getServletContainer()
		                .reloadContext(
		                        "/"
		                                + s.config
		                                        .getProperty("parade.servletContext.paradeContext")));
		}
	
}
