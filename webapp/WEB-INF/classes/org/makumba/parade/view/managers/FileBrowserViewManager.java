package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.view.interfaces.FileBrowserView;

public class FileBrowserViewManager implements FileBrowserView {

	public String getFileBrowserView(Parade p, String context, String path) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		Row r = (Row) p.getRows().get(context);
		if(r==null) return "Unknown context "+context;
		
		if(path==null) path="/";
		path=path.replace(java.io.File.separatorChar, '/');
		if(path.startsWith("/")) path=path.substring(1);
		if(path.length()>1&& !path.endsWith("/")) path=path+"/";
		
		FileViewManager fileV = new FileViewManager();
		CVSViewManager cvsV = new CVSViewManager();
		
		
		out.println("<HTML><HEAD><TITLE>"+context+" files</TITLE>"+
				"</HEAD><BODY>");
		
		out.println("<p align='left'" +
					"<font size=+1>[<a href='/servlet/file?context="+r.getRowname()+
					"&path="+path+"'>"+r.getRowname()+"</a>]"+path+
					"</font>");
		out.println("<img src='/images/folder-open.gif'>"+
					"<br><font size=-2>"+r.getRowpath()+"</font>");
		out.println("<table border='0' width='100%' cellspacing='0' cellpadding='2'>");
		
		// headers
		out.println("<tr bgcolor=#ddddff>" +
					"<td align='center'>"+fileV.getFileViewHeader()+"</td>" +
					"<td align='center'>"+cvsV.getFileViewHeader()+"</td>" +
					"</tr>");
		
		//files
		
		
		
		out.println("</p>");
		out.println("</TABLE></BODY></HTML>");
		
		return result.toString();
	}

	



}
