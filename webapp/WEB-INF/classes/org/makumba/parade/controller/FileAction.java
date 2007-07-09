package org.makumba.parade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.makumba.parade.model.Parade;

public class FileAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String file = request.getParameter("file");
        String op = request.getParameter("op");
        
        // we reconstruct the absolute path
        String absolutePath = Parade.constructAbsolutePath(context, path);
        
        String[] params = { request.getParameter("params"), absolutePath};

        if (op != null && op.startsWith("deleteFile")) {
            Object result[] = CommandController.onDeleteFile(context, params);
            request.setAttribute("result", (String) result[0]);
            request.setAttribute("success", (Boolean) result[1]);
        }

        if (op != null && op.startsWith("editFile")) {
            return (mapping.findForward("edit"));
        }
        
        if (op != null && op.startsWith("upload")) {
            
            request.setAttribute("context", context);
            request.setAttribute("path", path);
            
            return (mapping.findForward("upload"));
        }

        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("file", file);
        request.setAttribute("display", "file");

        return (mapping.findForward("browse"));

    }
}
