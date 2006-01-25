package org.makumba.parade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class CvsAction extends DispatchAction {

    public ActionForward check(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String[] params = request.getParameterValues("params");
       
        Object[] result = CvsController.onCheck(context, params);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", params[0]);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");

        return (mapping.findForward("command"));

    }
    
    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String[] params = request.getParameterValues("params");
       
        Object[] result = CvsController.onUpdate(context, params);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", params[0]);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");

        return (mapping.findForward("command"));

    }
    
    public ActionForward rupdate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String[] params = request.getParameterValues("params");
       
        Object[] result = CvsController.onRUpdate(context, params);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", params[0]);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");

        return (mapping.findForward("command"));

    }
    
    public ActionForward commit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String[] params = request.getParameterValues("params");
       
        Object[] result = CvsController.onCommit(context, params);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", params[0]);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");
        
        return (mapping.findForward("command"));

    }
    
    public ActionForward diff(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");
        
        Object[] result = CvsController.onDiff(context, path, file);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");
        
        return (mapping.findForward("command"));

    }
    
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");
       
        Object[] result = CvsController.onAdd(context, path, file);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");
        
        return (mapping.findForward("command"));

    }
    
    public ActionForward addbin(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");
       
        Object[] result = CvsController.onAddBinary(context, path, file);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");
        
        return (mapping.findForward("command"));

    }
    
    public ActionForward updatefile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");
       
        Object[] result = CvsController.onUpdateFile(context, path, file);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");
        
        return (mapping.findForward("command"));

    }
    
    public ActionForward deletefile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");
        
        Object[] result = CvsController.onDeleteFile(context, path, file);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");
        
        return (mapping.findForward("command"));

    }
    
    
    
    
    

    
}
