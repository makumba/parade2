package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import org.makumba.parade.model.Row;
import org.makumba.parade.view.interfaces.HeaderView;

public class HeaderViewManager implements HeaderView {

	// this is the beginning of the header
	public String getHeaderView(Row r) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		out.println("<html><head><title>"+r.getRowname()+" heading</title>"+
					"<base target='command'>"+
					"<style type='text/css'>"+
					"A {font-decoration:none}"+
					"</style>"+
					"</head>"+
					"<body bgcolor=#ddddff TOPMARGIN=0 LEFTMARGIN=0 RIGHTMARGIN=0 BOTTOMMARGIN=0 marginwidth=0 marginheight=0 STYLE='margin: 0px'>"+
					"<img src='/images/win-x.gif' align=right alt='remove frames' border=0 hspace=1 vspace=1 onMouseDown=\"src='/images/win-x2.gif'\""+ 
					"onMouseUp=\"src='/images/win-x.gif'; top.location=top.directory.location;\">");

		out.println("<table border=0 cellspacing=0 cellpadding=0>"+
					"<form ACTION='/index.jsp?' TARGET='_top' style='margin:0px;'>"+
					"<tr><td valign=top>"+
					"<a HREF='/index.jsp' TARGET='_top' title='back to front page'>&lt;</a>"+
					"<select SIZE='1' NAME='context' onChange=\"javascript:form.submit();\">");

		for(Iterator i = r.getParade().getRows().keySet().iterator(); i.hasNext();) {
			Row currentRow = (Row) r.getParade().getRows().get(i.next());
			String displayName = currentRow.getRowname();
			if(currentRow.getRowname() == "") displayName = "(root)";
			
			out.println("<option VALUE='"+displayName+"'"+(displayName.equals(r.getRowname())?" selected":"''")+">"+displayName+"</option>");
		}
		out.println("</select><input TYPE='submit' VALUE='Go!'>" +
					"</td>" +
					"</form>");
		
		out.println("<td>&nbsp;</td>");
				
		
		// TODO headers - that should be injected by Spring somehow
		LogViewManager logV = new LogViewManager();
		IcqViewManager icqV = new IcqViewManager();
		SshViewManager sshV = new SshViewManager();
		AntViewManager antV = new AntViewManager();
		WebappViewManager webappV = new WebappViewManager();
		
		out.println();
		out.println("<td valign=top>"+logV.getHeaderView(r)+"&nbsp;</td>");
		out.println("<td valign=top>"+icqV.getHeaderView(r)+"&nbsp;</td>");
		out.println("<td valign=top>"+sshV.getHeaderView(r)+"&nbsp;</td>");
		
		
		out.println("</tr></table></body></html>");
		
		
		return result.toString();
	}

}
