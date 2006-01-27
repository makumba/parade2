package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import org.makumba.parade.model.Row;
import org.makumba.parade.view.interfaces.HeaderView;

public class HeaderViewManager implements HeaderView {

    // this is the beginning of the header
    public String getHeaderView(Row r, String path) {
        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);

        out.println("<HTML><HEAD><TITLE>Command view for " + r.getRowname() + "</TITLE>");
        out.println("<link rel='StyleSheet' href='/style/parade.css' type='text/css'>");
        out.println("<link rel='StyleSheet' href='/style/header.css' type='text/css'>");
        out.println("<base target='command'>");
        out.println("</HEAD><BODY class='header'>"
        
        + "<body>"
        + "<img src='/images/win-x.gif' align=right alt='remove frames' border=0 hspace=1 vspace=1 onMouseDown=\"src='/images/win-x2.gif'\""
        + "onMouseUp=\"src='/images/win-x.gif'; top.location=top.directory.location;\">");

        out.println("<table class='header'>"
                + "<form ACTION='/browse.jsp?' TARGET='_top' style='margin:0px;'>" + "<tr><td valign=top>"
                + "<a HREF='/' TARGET='_top' title='back to front page'>&lt;</a>"
                + "<select SIZE='1' NAME='context' onChange=\"javascript:form.submit();\">");

        for (Iterator i = r.getParade().getRows().keySet().iterator(); i.hasNext();) {
            Row currentRow = (Row) r.getParade().getRows().get(i.next());
            String displayName = currentRow.getRowname();
            if (currentRow.getRowname() == "")
                displayName = "(root)";

            out.println("<option VALUE='" + displayName + "'"
                    + (displayName.equals(r.getRowname()) ? " selected" : "") + ">" + displayName + "</option>");
        }
        out.println("</select><input TYPE='submit' VALUE='Go!'>" + "</td>" + "</form>");
        
        out.println("<td valign=top>[<a href='log?context=" + r.getRowname() + " title='" + r.getRowname()
                + " log' target='directory'>log</a>]"
                + "<a href='/logs/server-output.txt' title='Server log' target='directory'>all-log</a>"
                + "-<a href='/logs' title='other logs' target='directory'>s</a> "
                + "<a href='/tomcat-docs' title='Tomcat documentation' target='directory'>Tomcat</a> "
                + "<a href='/makumba-docs' title='Makumba documentation' target='directory'>Makumba</a></td>");

        out.println("<td valign=top>"
                + "<script language='JavaScript'>"
                + "<!--"
                + "function icqNewWin() {"
                + "var leftpos = (screen.availWidth - 200)-40;"
                + "resiz = (navigator.appName=='Netscape') ? 0 : 1;"
                + "window.open('http://lite.icq.com/icqlite/web/0,,,00.html', 'TOFI','width=177,height=446,top=40,left='+leftpos+',directories=no,location=no,menubar=no,scroll=no,status=no,titlebar=no,toolbar=no,resizable='+resiz+'');"
                + "} //-->"
                + "</script>"
                + "<a href='#start ICQ Lite' target='header' onClick='javascript:icqNewWin();'><img src='/images/icq-online.gif' border=0 alt='Launch ICQ Lite'></a></td>");

        out.println("<td valign=top><a href='ssh/ssh.jsp' target='command' title='Secure shell'>ssh</a></td>");

        // TODO headers - that should be injected by Spring somehow
        AntViewManager antV = new AntViewManager();
        WebappViewManager webappV = new WebappViewManager();

        out.println("<td valign=top>" + antV.getHeaderView(r, path) + "</td>");
        out.println("<td valign=top>" + webappV.getHeaderView(r, path) + "</td>");

        out.println("</tr></table></body></html>");

        return result.toString();
    }

}
