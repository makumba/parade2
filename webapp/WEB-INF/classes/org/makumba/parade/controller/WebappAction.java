package org.makumba.parade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class WebappAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String op = request.getParameter("op");
        String path = request.getParameter("path");
        if(path == null)
            path = (String) request.getAttribute("path");
        if(path == null)
            path ="";
        String view = request.getParameter("view");
        if(view == null)
            view = (String) request.getAttribute("view");
        if(view == null)
            view = "commandOutput";
        
        WebappController webappCtrl = new WebappController();
        Object result[] = webappCtrl.onWebappAction(context, op);
        
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("view", view);
        request.setAttribute("display","command");
        
        return mapping.findForward("command"); 
        
    }
}
