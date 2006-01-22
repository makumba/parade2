package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowWebapp;
import org.makumba.parade.model.managers.ServletContainer;
import org.makumba.parade.view.interfaces.HeaderView;
import org.makumba.parade.view.interfaces.ParadeView;

public class WebappViewManager implements HeaderView, ParadeView {

    public String getHeaderView(Row r) {
        RowWebapp data = (RowWebapp) r.getRowdata().get("webapp");

        return "webapp: " + getCommands(r, data);
    }

    public String getParadeViewHeader() {
        String header = "<b>Webapp path, status</b>";
        return header;
    }

    public String getParadeView(Row r) {
        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);

        RowWebapp data = (RowWebapp) r.getRowdata().get("webapp");

        int status = data.getStatus().intValue();

        out.println(data.getWebappPath() + ", " + ServletContainer.status[status] + "<br>");

        // TODO - consider the case WEBINF isn't found, ie get server name, port from some request

        out.println(getCommands(r, data));

        return result.toString();
    }

    private String getCommands(Row r, RowWebapp data) {
        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);

        int status = data.getStatus().intValue();

        if (data.getWebappPath().equals("NO WEBINF")) {
            /*
             * if(pageContext.findAttribute('servletContext.noPrintStatus')==null){ if(stt==ServletContainer.RUNNING) { %>
             * <a href= <%='http://'+request.getServerName()+':'+request.getServerPort()+contextName %> > <%}%>
             * <%=ServletContainer.status[stt]%> <% if(stt==ServletContainer.RUNNING) { %> </a> <%} %> <br> <% }
             * 
             */
        } else {
            if (status == ServletContainer.RUNNING) {
                out.println("<a href='index.jsp?entry=" + r.getRowname()
                        + "&handler=webapp&op=servletContextReload'>reload</a> " + "<a href='index.jsp?entry="
                        + r.getRowname() + "&handler=webapp&op=servletContextStop'>stop</a> ");
            }
            if (status == ServletContainer.STOPPED) {
                out.println("<a href='index.jsp?entry=" + r.getRowname()
                        + "&handler=webapp&op=servletContextStart'>start</a> ");
            }
            if (status != ServletContainer.NOT_INSTALLED) {
                out.println("<a href='index.jsp?entry=" + r.getRowname()
                        + "&handler=webapp&op=servletContextRemove'>uninstall</a>");
            }
            if (status == ServletContainer.NOT_INSTALLED) {
                out.println("<a href='index.jsp?entry=" + r.getRowname()
                        + "&handler=webapp&op=servletContextInstall'>install</a>");
            }
        }

        return result.toString();
    }

}
