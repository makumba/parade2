package org.makumba.parade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * Struts Action for File
 * 
 * 
 * @author Manuel Gay
 * @author Joao Andrade
 * 
 */
public class FileAction extends DispatchAction {

    public ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String file = request.getParameter("file");

        Response result = FileController.onDeleteFile(context, path, file);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("file", file);
        return mapping.findForward("files");
    }

    public ActionForward deleteDir(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String file = request.getParameter("file");

        Response result = FileController.onDeleteDir(context, path, file);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("file", file);
        return mapping.findForward("files");
    }

    /**
     * @author Joao Andrade
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward editFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("codePressEdit");
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
        return mapping.findForward(editor + "Edit");
    }
}
