package org.makumba.parade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * @author Manuel Gay
 * @author Joao Andrade
 *
 */
public class WebappAction extends DispatchAction {
    
    private WebappController webappController = new WebappController();

    public ActionForward servletContextStart(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String display = request.getParameter("display");
        
        Response result = webappController.onContextStart(context);
        
        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward(display);
    }

    public ActionForward servletContextStop(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String display = request.getParameter("display");
        
        Response result = webappController.onContextStop(context);
        
        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward(display);
    }

    public ActionForward servletContextReload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String display = request.getParameter("display");
        
        Response result = webappController.onContextReload(context);
        
        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward(display);
    }

    public ActionForward servletContextRemove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String display = request.getParameter("display");
        
        Response result = webappController.onContextRemove(context);
        
        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward(display);
    }

    public ActionForward servletContextInstall(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String display = request.getParameter("display");
        
        Response result = webappController.onContextInstall(context);
        
        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward(display);
    }

    public ActionForward servletContextRedeploy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String display = request.getParameter("display");
        
        Response result = webappController.onContextRedeploy(context);
        
        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward(display);
    }
}
