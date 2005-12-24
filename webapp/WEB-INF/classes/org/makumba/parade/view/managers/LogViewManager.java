package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.makumba.parade.model.Row;
import org.makumba.parade.view.interfaces.HeaderView;

public class LogViewManager implements HeaderView {

	public String getHeaderView(Row r) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		out.print("[<a href='log?context="+r.getRowname()+" title='"+r.getRowname()+" log' target='directory'>log</a>]"+
				"<a href='/logs/server-output.txt' title='Server log' target='directory'>all-log</a>" +
				"-<a href='/logs' title='other logs' target='directory'>s</a> " +
				"<a href='/tomcat-docs' title='Tomcat documentation' target='directory'>Tomcat</a> " +
				"<a href='/makumba-docs' title='Makumba documentation' target='directory'>Makumba</a>");
		
		return result.toString();
	}

}
