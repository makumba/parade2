package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.tools.FileComparator;
import org.makumba.parade.view.interfaces.FileBrowserView;

public class FileBrowserViewManager implements FileBrowserView {

	public String getFileBrowserView(Parade p, Row r, String path, String opResult) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		//if this is the root of the row
		if(path==null) path = r.getRowpath();
		
		FileViewManager fileV = new FileViewManager();
		CVSViewManager cvsV = new CVSViewManager();
		TrackerViewManager trackerV = new TrackerViewManager();
		
		out.println("<HTML><HEAD><TITLE>"+r.getRowname()+" files</TITLE>"+
				"</HEAD><BODY>");
		
		if(!(opResult == null) && !opResult.equals(""))
			try {
				out.println(URLDecoder.decode(opResult,"UTF-8")+"<br>");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		out.println("<p align='left'>" +
					"<font size=+1>[<a href='/servlet/file?context="+r.getRowname()+"'>"+
					r.getRowname()+"</a>]/"+getParentDir(r, path)+
					"</font>");
		out.println("<img src='/images/folder-open.gif'>"+
					"<br><font size=-2>"+r.getRowpath()+"</font>");
		out.println("<table border='0' width='100%' cellspacing='0' cellpadding='2'>");
		
		out.println("</p>");
		
		// headers
		out.println("<tr bgcolor=#ddddff>" +
					fileV.getFileViewHeader(r, path)+
					cvsV.getFileViewHeader(r, path)+
					trackerV.getFileViewHeader(r, path)+
					"</tr>");
		
		//files
		List files = FileManager.getChildren(r, path);
		FileComparator fc = new FileComparator();
		
		Collections.sort(files, fc);
		String relativePath = path.substring(r.getRowpath().length(), path.length());
		
		int counter = 0;
		for(Iterator j = files.iterator(); j.hasNext();) {
			File currentFile = (File) j.next();
			out.println("<tr bgcolor="+(((counter % 2) == 0) ? "#ffffff" : "#f5f5ff")+" valign=top>" +
						fileV.getFileView(r, relativePath, currentFile)+
						cvsV.getFileView(r, relativePath, currentFile)+
						trackerV.getFileView(r, relativePath, currentFile)+
						"</td></tr>");
			
			counter++;
		}
		
		
		
		
		out.println("</TABLE></BODY></HTML>");
		
		return result.toString();
	}

	private String getParentDir(Row r, String path) {
		String relativePath = path.substring(r.getRowpath().length(), path.length());
		String parentDir = "";
		String currentPath = path.substring(0,r.getRowpath().length());
		
		StringTokenizer st = new StringTokenizer(relativePath, java.io.File.separator);
		while(st.hasMoreTokens()) {
			String thisToken = st.nextToken();
			currentPath+=java.io.File.separator + thisToken; 
			parentDir+="<a href='/servlet/file?context="+r.getRowname()+
						"&path="+currentPath+"'>"+thisToken+"</a>"+"/";
		}
		return parentDir;
	}

	



}
