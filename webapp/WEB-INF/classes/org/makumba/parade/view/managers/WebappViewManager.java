package org.makumba.parade.view.managers;

import java.util.List;

import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowWebapp;
import org.makumba.parade.model.managers.ServletContainer;
import org.makumba.parade.view.interfaces.HeaderView;
import org.makumba.parade.view.interfaces.ParadeView;

import freemarker.template.SimpleHash;

public class WebappViewManager implements HeaderView, ParadeView {

    public void setParadeViewHeader(List headers) {
        headers.add("Webapp path");
        headers.add("Webapp status");
    }

    public void setParadeView(SimpleHash rowInformation, Row r) {
        SimpleHash webappModel = setCommands(r, "");
        rowInformation.put("webapp", webappModel);
    }
    
    public void setHeaderView(SimpleHash root, Row r, String path) {
        SimpleHash webapp = setCommands(r, path);
        root.put("webapp", webapp);
    }

    private SimpleHash setCommands(Row r, String path) {
        SimpleHash webappModel = new SimpleHash();
        RowWebapp data = (RowWebapp) r.getRowdata().get("webapp");

        int status = data.getStatus().intValue();
        
        if( r.getRowpath().equals(r.getParade().getBaseDir())) {
            data.setStatus(new Integer(ServletContainer.RUNNING));
            status = ServletContainer.RUNNING;
        }
        
        webappModel.put("webappPath", data.getWebappPath());
        webappModel.put("status", new Integer(status));
        webappModel.put("path", path==null?"":path);
                    
        // TODO - consider the case WEBINF isn't found, ie get server name, port from some request
        if (data.getWebappPath().equals("NO WEBINF")) {
            /*
             * if(pageContext.findAttribute('servletContext.noPrintStatus')==null){ if(stt==ServletContainer.RUNNING) { %>
             * <a href= <%='http://'+request.getServerName()+':'+request.getServerPort()+contextName %> > <%}%>
             * <%=ServletContainer.status[stt]%> <% if(stt==ServletContainer.RUNNING) { %> </a> <%} %> <br> <% }
             * 
             */
        }
        return webappModel;
    }
}
