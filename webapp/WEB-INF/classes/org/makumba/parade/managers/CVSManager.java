package org.makumba.parade.managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.makumba.parade.ifc.DirectoryRefresher;
import org.makumba.parade.ifc.RowRefresher;
import org.makumba.parade.model.File;
import org.makumba.parade.model.FileCVS;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowCVS;

public class CVSManager implements DirectoryRefresher, RowRefresher {
	
	static Logger logger = Logger.getLogger(CVSManager.class.getName());

	static Integer IGNORED = new Integer(101);

    static Integer UNKNOWN = new Integer(-1);

    static Integer UP_TO_DATE = new Integer(100);

    static Integer LOCALLY_MODIFIED = new Integer(1);

    static Integer NEEDS_CHECKOUT = new Integer(2);

    static Integer NEEDS_UPDATE = new Integer(3);

    static Integer ADDED = new Integer(4);

    static Integer DELETED = new Integer(5);

    static Integer CONFLICT = new Integer(6);
	
	public static DateFormat cvsDateFormat;
    static {
        cvsDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy",
                Locale.UK);
        cvsDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
	
	public void directoryRefresh(Row row, String path) {
		
		java.io.File currDir = new java.io.File(path);
		
		// we will go through the CVS entries of the real directories
		if(!currDir.exists() || !currDir.isDirectory()) return;
		
		File currFile = (File) row.getFiles().get(path);
			
			if(!(currFile == null) && currFile.getIsDir()) {
				
				Map filedata = currFile.getFiledata();
				FileCVS filecvsdata = (FileCVS) filedata.get("cvs");
				if(filecvsdata == null) {
					filecvsdata = new FileCVS();
					filecvsdata.setDataType("cvs");
					filecvsdata.setFile(currFile);
				}
				
				readFiles(row,currFile,filecvsdata);
				
				filedata.put("filecvs",filecvsdata);
				currFile.setFiledata(filedata);
			}
	}

	public void rowRefresh(Row row) {
		
		RowCVS cvsdata = new RowCVS();
		cvsdata.setDataType("cvs");
		
		readUserAndModule(row, cvsdata);
		
		RowStoreManager rowMgr = new RowStoreManager();
		rowMgr.addManagerData(cvsdata,row);
	}

	
	private void readUserAndModule(Row row, RowCVS data) {
		
		String path = (String) row.getRowpath();
        String s = null;
        try {
            s = new BufferedReader(new FileReader(path + java.io.File.separator + "CVS"
                    + java.io.File.separator + "Root")).readLine();
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }

        if (s.startsWith(":pserver")) {
        	s = s.substring(":pserver:".length());
            data.setUser(s.substring(0, s.indexOf("@")));
        }
        else if (s.startsWith(":extssh:")) {
        	s = s.substring(":extssh:".length());
            data.setUser(s.substring(0, s.indexOf("@")));
        }
        else
        	data.setUser("non :pserver");

        try {
            s = new BufferedReader(new FileReader(path + java.io.File.separator + "CVS"
                    + java.io.File.separator + "Repository")).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        data.setModule(s.substring(s.lastIndexOf('/') + 1));

        try {
            s = "TMAIN";
            s = new BufferedReader(new FileReader(path + java.io.File.separator + "CVS"
                    + java.io.File.separator + "Tag")).readLine();
        } catch (FileNotFoundException e) {
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }
        data.setBranch(s.substring(1));

    }
	
	private void readFiles(Row r, File f, FileCVS cvsdata) {
		
		readCVSEntries(r, f, cvsdata);
		readCVSIgnore(r, f, cvsdata);
		// readCVSCheckUpdate(paradeRow, data, pc);
	}
	
	/* Reads Entries file and extracts information */
    private void readCVSEntries(Row r, File file, FileCVS cvsdata) {
    	
        java.io.File f = new java.io.File((r.getRowpath() + "/" + file.getPath() + "/" +"CVS/Entries").replace('/',java.io.File.separatorChar));
        if (!f.exists()) {
            cvsdata.setIsCVSDir(false);
            return;
        }

        cvsdata.setIsCVSDir(true);
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("/")) {
                    int n = line.indexOf('/', 1);
                    if (n == -1)
                        continue;
                    String name = line.substring(1, n);
                    File cvsfile = (File) r.getFiles().get(name);
                    if (cvsfile == null) {
                    	cvsfile = new File();
                    	file.setName(name);
                        file.setNotOnDisk(true);
                        file.setIsDir(true);
                        file.setRow(r);
                        file.getFiledata().put("cvs",cvsdata);
                        
                    }
                    cvsdata.setStatus(UNKNOWN);
                    line = line.substring(n + 1);
                    n = line.indexOf('/');
                    if (n == -1)
                        continue;
                    String revision = line.substring(0, n);
                    cvsdata.setRevision(revision);
                    line = line.substring(n + 1);
                    n = line.indexOf('/');
                    if (n == -1)
                        continue;

                    java.io.File fl = new java.io.File(file.getPath());

                    if (fl == null && !revision.startsWith("-")) {
                        cvsdata.setStatus(NEEDS_CHECKOUT);
                        continue;
                    }

                    String date = line.substring(0, n);

                    if (date.equals("Result of merge")) {
                        cvsdata.setStatus(LOCALLY_MODIFIED);
                        continue;
                    }

                    if (date.startsWith("Result of merge+")) {
                        cvsdata.setStatus(CONFLICT);
                        continue;
                    }

                    if (date.equals("dummy timestamp")) {
                        cvsdata.setStatus(revision.startsWith("-") ? DELETED : ADDED);
                        continue;
                    }

                    Date fd = null;
                    try {
                        cvsdata.setDate(fd = cvsDateFormat.parse(date));
                    } catch (Throwable t) {
                        logger.error("Couldn't parse date of CVS File "+file.getName(),t);
                        continue;
                    }

                    long cvsModified = fd.getTime();

                    long l = fl.lastModified() - cvsModified
                    // -(TimeZone.getDefault().inDaylightTime(new
                    // Date(fl.lastModified()))?3600000:0);
                    ;
                    if (Math.abs(l) < 1500
                    // for some stupid reason, lastModified() is different in
                    // Windows than Unix
                            // the difference seems to have to do with daylight
                            // saving
                            || Math.abs(Math.abs(l) - 3600000) < 1000) {
                        cvsdata.setStatus(UP_TO_DATE);
                        continue;
                    }

                    cvsdata.setStatus(l > 0 ? LOCALLY_MODIFIED : NEEDS_UPDATE);
                    // stupid windows bug
                    // if(l<0)
                    // System.out.println(l);
                    continue;
                } else if (line.startsWith("D/")) {
                    int n = line.indexOf('/', 2);
                    if (n == -1)
                        continue;
                    String name = line.substring(2, n);
                    File cvsfile = (File) r.getFiles().get(name);
                    
                    if (cvsfile == null) {
                    	cvsfile = new File();
                    	file.setName(name);
                        file.setNotOnDisk(true);
                        file.setIsDir(true);
                        file.setRow(r);
                        file.getFiledata().put("cvs",cvsdata);
                        cvsdata.setIsCVSDir(true);
                        cvsdata.setStatus(NEEDS_CHECKOUT);
                    } else {
                        cvsdata.setStatus(UP_TO_DATE);
                        cvsdata.setRevision("(dir)");
                    }
                }
            }
            br.close();
        } catch (Throwable t) {
            logger.error("Error while trying to set CVS information for file "+file.getName(),t);
        }
    }
    
    /* Reads .cvsignore */
    private void readCVSIgnore(Row r, File file, FileCVS cvsdata) {
    	if (!cvsdata.getIsCVSDir()) return;
    	
    	java.io.File f = new java.io.File((r.getRowpath() + "/" + file.getPath() + "/" + ".cvsignore").replace('/',java.io.File.separatorChar));
        
    	if (!f.exists())
            return;

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                File cvsfile = (File) r.getFiles().get(line);
                if (cvsfile == null)
                    continue;
                cvsdata.setStatus(IGNORED);
            }
            br.close();
        } catch (Throwable t) {
            logger.error("Error while trying to read .cvsignore of directory "+file.getName(),t);
        }
    }

    /*
    public void cvs(java.util.Map data, javax.servlet.jsp.PageContext pc) {
        Vector command = new Vector();
        command.addElement("cvs");
        Config.addCommandOptions(command, "cvs", pc);
        command.addElement(pc.getRequest().getParameterValues("cvs.op")[0]);
        Config.addCommandOptions(command, "cvs.op", pc);
        String mes[];
        if ((mes = pc.getRequest().getParameterValues("cvs.committMessage")) != null) {
            command.addElement("-m");
            command.addElement(mes[0]);
        }
        if (pc.getRequest().getParameterValues("cvs.perDir") == null)
            for (Iterator i = data.values().iterator(); i.hasNext();) {
                Map m = (Map) i.next();
                command.addElement(m.get("file.name"));
            }
        Config.exec(command, (File) pc.findAttribute("file.baseFile"), Config
                .getPrintStreamCVS(pc.getOut()));
    }
    */

	

}
