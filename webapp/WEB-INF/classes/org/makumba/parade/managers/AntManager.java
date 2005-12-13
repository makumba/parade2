package org.makumba.parade.managers;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Main;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;
import org.makumba.parade.ifc.RowRefresher;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowAnt;

public class AntManager implements RowRefresher {
	
	static Logger logger = Logger.getLogger(AntManager.class.getName());
	
	public void rowRefresh(Row row) {
		RowAnt antdata = new RowAnt();
		antdata.setDataType("ant");
				
		Project p = setBuildFile(row, antdata);
        if (p == null) {
        	logger.error("AntManager: couldn't initialise the project");
            return;
        }
        setTargets(antdata, p);
        
		RowStoreManager rowMgr = new RowStoreManager();
		rowMgr.addManagerData(antdata,row);
		
	}
	
	private Project setBuildFile(Row row, RowAnt data) {
        File dir = new File(row.getRowpath());

        File buildFile = data.getBuildfile();
        if(buildFile == null) {
        	buildFile = new File(dir, "build.xml");
        	if (!buildFile.exists()) return null;
        	data.setBuildfile(buildFile);
        }
        
        return getProject(buildFile, row, data);
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
			
            project.setUserProperty("ant.file", buildFile.getAbsolutePath());
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

        ArrayList topTargets = new ArrayList();
        ArrayList subTargets = new ArrayList();

        while (ptargets.hasMoreElements()) {
            Target currentTarget = (Target) ptargets.nextElement();

            if (currentTarget.getDescription() == null)
                subTargets.add(currentTarget.getName());
            else {
                String obj[] = { currentTarget.getName(),
                        currentTarget.getDescription() };
                topTargets.add(obj);
            }
        }
        Collections.sort(subTargets);
        Collections.sort(topTargets, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((String[]) o1)[0].compareTo(((String[]) o2)[0]);
            }
        });
        
        
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
