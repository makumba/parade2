package org.makumba.parade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.makumba.parade.model.Parade;

public class CvsAction extends DispatchAction {

    public ActionForward check(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String path = request.getParameter("path");

        // we need to convert the relative path displayed in the webapp to something usable)
        String absolutePath = Parade.constructAbsolutePath(context, path);

        Response result = CvsController.onCheck(context, absolutePath);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward("command");
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String path = request.getParameter("path");

        // we need to convert the relative path displayed in the webapp to something usable)
        String absolutePath = Parade.constructAbsolutePath(context, path);

        Response result = CvsController.onUpdate(context, absolutePath);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward("command");
    }

    public ActionForward rUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String path = request.getParameter("path");

        // we need to convert the relative path displayed in the webapp to something usable)
        String absolutePath = Parade.constructAbsolutePath(context, path);

        Response result = CvsController.onRUpdate(context, absolutePath);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward("command");
    }

    public ActionForward commit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String[] files = request.getParameterValues("file");
        String context = request.getParameter("context");
        if (files.length > 1 && context == null) {
            context = (String) request.getSession().getAttribute("currentContext");
        }

        if (context == null) {
            // doh. FIXME
            context = "";
        }

        String path = request.getParameter("path");
        String message = request.getParameter("message");

        // we reconstruct the absolute paths (the ones passed as params are relative)
        path = Parade.constructAbsolutePath(context, path);

        Response result = CvsController.onCommit(context, files, message);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward("command");
    }

    public ActionForward diff(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");

        // we reconstruct the absolute paths (the ones passed as params are relative)
        String absolutePath = Parade.constructAbsolutePath(context, path);
        String absoluteFilePath = Parade.constructAbsolutePath(context, file);

        Response result = CvsController.onDiff(context, absolutePath, absoluteFilePath);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward("command");
    }

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");

        // we reconstruct the absolute paths (the ones passed as params are relative)
        String absolutePath = Parade.constructAbsolutePath(context, path);
        String absoluteFilePath = Parade.constructAbsolutePath(context, file);

        Response result = CvsController.onAdd(context, absolutePath, absoluteFilePath);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward("command");
    }

    public ActionForward addBin(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");

        // we reconstruct the absolute paths (the ones passed as params are relative)
        String absolutePath = Parade.constructAbsolutePath(context, path);
        String absoluteFilePath = Parade.constructAbsolutePath(context, file);

        Response result = CvsController.onAddBinary(context, absolutePath, absoluteFilePath);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward("command");
    }

    public ActionForward updateFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");

        // we reconstruct the absolute paths (the ones passed as params are relative)
        String absolutePath = Parade.constructAbsolutePath(context, path);
        String absoluteFilePath = Parade.constructAbsolutePath(context, file);

        Response result = CvsController.onUpdateFile(context, absolutePath, absoluteFilePath);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward("command");
    }

    public ActionForward overrideFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");

        // we reconstruct the absolute paths (the ones passed as params are relative)
        String absolutePath = Parade.constructAbsolutePath(context, path);
        String absoluteFilePath = Parade.constructAbsolutePath(context, file);

        Response result = FileController.onDeleteFile(context, path, file.substring(path.length() + 1));
        if (result == null) {
            result = CvsController.onUpdateFile(context, absolutePath, absoluteFilePath);
        }

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward("command");
    }

    public ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");

        // we reconstruct the absolute paths (the ones passed as params are relative)
        String absolutePath = Parade.constructAbsolutePath(context, path);
        String absoluteFilePath = Parade.constructAbsolutePath(context, file);

        Response result = CvsController.onDeleteFile(context, absolutePath, absoluteFilePath);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward("command");
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
    public ActionForward deleteDirectory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");

        // we reconstruct the absolute paths (the ones passed as params are relative)
        String absolutePath = Parade.constructAbsolutePath(context, path);
        String absoluteFilePath = Parade.constructAbsolutePath(context, file);

        Response result = CvsController.onDeleteDirectory(context, absolutePath, absoluteFilePath);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward("command");
    }
}
