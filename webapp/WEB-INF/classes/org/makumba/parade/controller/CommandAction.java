package org.makumba.parade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class CommandAction extends DispatchAction {

    public ActionForward newFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String[] params = {request.getParameter("params"), path};

        Object[] result = CommandController.onNewFile(context, params);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "file");

        return mapping.findForward("files");
    }

    public ActionForward newDir(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String[] params = {request.getParameter("params"), path};

        Object[] result = CommandController.onNewDir(context, params);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "file");

        return mapping.findForward("files");
    }
}
