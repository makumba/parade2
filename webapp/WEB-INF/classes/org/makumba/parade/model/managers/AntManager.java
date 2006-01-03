package org.makumba.parade.model.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowAnt;
import org.makumba.parade.model.interfaces.ParadeManager;
import org.makumba.parade.model.interfaces.RowRefresher;

public class AntManager implements RowRefresher, ParadeManager {
	
	static Logger logger = Logger.getLogger(AntManager.class.getName());
	
	public void rowRefresh(Row row) {
		RowAnt antdata = new RowAnt();
		antdata.setDataType("ant");
		
		File buildFile = setBuildFile(row, antdata);
		if(buildFile == null || !buildFile.exists()) {
			logger.error("AntManager: no build file found for row "+row.getRowname());
		} else {
			Project p = getProject(buildFile, row, antdata);
	        if (p == null) {
	        	logger.error("AntManager: couldn't initialise the project");
	        }
	        setTargets(antdata, p);
		}
		
		row.addManagerData(antdata);
		
	}
	
	private java.io.File setBuildFile(Row row, RowAnt data) {
        File dir = new File(row.getRowpath());

        String buildFilePath = data.getBuildfile();
        File buildFile = null;
        
        if(buildFilePath == "") {
        	buildFile = setBuildFilePath(row, data, dir);
        }
    	
        return buildFile;
        
    }

	private java.io.File setBuildFilePath(Row row, RowAnt data, File dir) {
		File buildFile;
		buildFile = new java.io.File(dir + File.separator + "build.xml");
		if (!buildFile.exists()) {
			data.setBuildfile("No build file");
		} else {
			try {
				data.setBuildfile(buildFile.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return buildFile;
	}
	
	
	private synchronized Project getProject(File buildFile, Row row, RowAnt data) {
		Project project = null;
		
		if(data.getLastmodified() == null || buildFile.lastModified() > data.getLastmodified().longValue()) {
			data.setLastmodified(new Long(buildFile.lastModified()));
			project = new Project();
			
			try {
                project.init();
                ProjectHelper.getProjectHelper().parse(project, buildFile);
            } catch (Throwable t) {
                java.util.logging.Logger.getLogger("org.makumba.parade.ant")
                        .log(java.util.logging.Level.WARNING,
                                "project config error", t);
                return null;
            }

            try {
				project.setUserProperty("ant.file", buildFile.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            DefaultLogger lg = new DefaultLogger();
            lg.setEmacsMode(true);
            lg.setMessageOutputLevel(Project.MSG_INFO);
            project.addBuildListener(lg);
		    
		}
		return project;
    }
	
	private void setTargets(RowAnt antdata, Project p) {
		Project project = p;
        Enumeration ptargets = project.getTargets().elements();

        List targets = antdata.getTargets();
        while (ptargets.hasMoreElements()) {
        	Target currentTarget = (Target) ptargets.nextElement();
        	if(currentTarget.getDescription() != null && currentTarget.getDescription() != "") {
        		targets.add("#"+currentTarget.getName());
        	} else {
        		targets.add(currentTarget.getName());
        	}
        	
        	Collections.sort(antdata.getTargets());
        }
    }

	public void newRow(String name, Row r, Map m) {
		// TODO Auto-generated method stub
		
	}
	
	/* executes an Ant command 
	public void executeAntCommandSimple(RowAnt data, Row row) throws IOException {
        java.lang.Runtime rt = java.lang.Runtime.getRuntime();
        rt.gc();
        long memSize = rt.totalMemory() - rt.freeMemory();
        setBuildFile(row, data);
        Project project = data.getProject();
        DefaultLogger lg = (DefaultLogger) data.getLogger();
        
        PrintStream ps = Config.getPrintStream(pc.getOut());
        ps.println("heap size: " + memSize);
        ps.println(Main.getAntVersion());
        ps.println("Buildfile: " + row.get("ant.file"));
        String s[] = pc.getRequest().getParameterValues("antCommand");
        Vector v = new Vector();
        for (int i = 0; i < s.length; i++)
            v.addElement(s[i]);

        synchronized (project) {
            lg.setOutputPrintStream(ps);
            lg.setErrorPrintStream(ps);

            lg.buildStarted(null);
            Throwable error = null;
            try {
                project.executeTargets(v);
            } catch (Throwable t) {
                error = t;
            }
            BuildEvent be = new BuildEvent(project);
            be.setException(error);
            lg.buildFinished(be);
        }
        ps.flush();
        rt.gc();
        long memSize1 = rt.totalMemory() - rt.freeMemory();
        ps.println("heap size: " + memSize1);
        ps.println("heap grew with: " + (memSize1 - memSize));
    }
    */

	
	

}
