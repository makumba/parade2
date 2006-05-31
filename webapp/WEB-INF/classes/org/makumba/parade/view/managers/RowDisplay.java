package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.model.managers.WebappManager;

public class RowDisplay {

    public String getView(Parade p, String context, String opResult, boolean success) {

        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);

        RowStoreViewManager rowstoreV = new RowStoreViewManager();
        CVSViewManager cvsV = new CVSViewManager();
        AntViewManager antV = new AntViewManager();
        WebappViewManager webappV = new WebappViewManager();
        MakumbaViewManager makV = new MakumbaViewManager();

        out.println("<HTML><HEAD><TITLE>Welcome to ParaDe</TITLE>" + "</HEAD><BODY><CENTER>");
        out.println("<link rel='StyleSheet' href='/style/rowstore.css' type='text/css'>");
        out.println("<BORDER class='rowstore'>");
        

        if (opResult != null) {
            if (success)
                out.println("<div class='success'>" + opResult + "</div>");
            else
                out.println("<div class='failure'>" + opResult + "</div>");
        }

        out.println("<TABLE class='rowstore'>");

        // printing headers
        out.println("<TR>");

        out.println(rowstoreV.getParadeViewHeader());
        out.println(cvsV.getParadeViewHeader());
        out.println(antV.getParadeViewHeader());
        out.println(webappV.getParadeViewHeader());
        out.println(makV.getParadeViewHeader());
        
        out.print("</TR>");

        // printing row information
        Iterator i = p.getRows().keySet().iterator();
        int counter = 0;
        while (i.hasNext()) {
            String key = (String) i.next();

            out.println("<TR class='" + (((counter % 2) == 0) ? "odd" : "even") + "'>");

            out.println("<TD align='center'>");
            out.println(rowstoreV.getParadeView((Row) p.getRows().get(key)));
            out.println("</TD>");
            out.println("<TD align='center'>");
            out.println(cvsV.getParadeView((Row) p.getRows().get(key)));
            out.println("</TD>");
            out.println("<TD align='center'>");
            out.println(antV.getParadeView((Row) p.getRows().get(key)));
            out.println("</TD>");
            out.println("<TD align='center'>");
            out.println(webappV.getParadeView((Row) p.getRows().get(key)));
            out.println("</TD>");
            out.println("<TD align='center'>");
            out.println(makV.getParadeView((Row) p.getRows().get(key)));
            out.println("</TD>");

            out.println("</TR>");
            
            counter++;

        }

        out.println("</TABLE>" +
                "<BR><BR>" +
                "<div class='command'><a href='/Rows.do?op=paraderefresh'>Refresh ParaDe</a></div>" +
                "</CENTER></BODY></HTML>");

        return result.toString();


    }
    
}
