package org.makumba.parade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class EditAction extends DispatchAction {

    public ActionForward editFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String file = request.getParameter("file");
        String editor = request.getParameter("editor");

        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("file", file);      
        return mapping.findForward(editor);
    }

    public ActionForward revertFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String file = request.getParameter("file");
        String editor = request.getParameter("editor");

        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("file", file);      
        return mapping.findForward(editor);
    }
    
    public ActionForward saveFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String file = request.getParameter("file");
        String editor = request.getParameter("editor");
        String[] source = request.getParameterValues("source");

        Response result = FileController.onSaveFile(context, path, file, source);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("file", file);
        return mapping.findForward(editor);
    }
}
