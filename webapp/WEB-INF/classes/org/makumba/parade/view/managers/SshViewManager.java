package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.makumba.parade.model.Row;
import org.makumba.parade.view.interfaces.HeaderView;

public class SshViewManager implements HeaderView {

	public String getHeaderView(Row r) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		out.print("<a href='ssh/ssh.jsp' target='command' title='Secure shell'>ssh</a>");
		
		return result.toString();
	}

}
