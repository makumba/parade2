package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.makumba.parade.model.Row;
import org.makumba.parade.view.interfaces.HeaderView;

public class IcqViewManager implements HeaderView {

	public String getHeaderView(Row r) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		out.println("<script language='JavaScript'>" +
				"<!--" +
				"function icqNewWin() {" +
				"var leftpos = (screen.availWidth - 200)-40;" +
				"resiz = (navigator.appName=='Netscape') ? 0 : 1;" +
				"window.open('http://lite.icq.com/icqlite/web/0,,,00.html', 'TOFI','width=177,height=446,top=40,left='+leftpos+',directories=no,location=no,menubar=no,scroll=no,status=no,titlebar=no,toolbar=no,resizable='+resiz+'');" +
				"//-->" +
				"</script>" +
				"<a href='#start ICQ Lite' target='header' onClick='javascript:icqNewWin();'><img src='/images/icq-online.gif' border=0 alt='Launch ICQ Lite'></a>");
		
		return result.toString();
		
	}

}
