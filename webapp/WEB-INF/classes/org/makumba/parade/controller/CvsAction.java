package org.makumba.parade.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.makumba.parade.access.ActionLogDTO;
import org.makumba.parade.access.DatabaseLogServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.tools.TriggerFilter;

public class CvsAction extends DispatchAction {

    private DatabaseLogServlet dbs = new DatabaseLogServlet();
    
    public ActionForward check(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String[] params = request.getParameterValues("params");
        String path = params[0];

        // we need to convert the relative path displayed in the webapp to something usable
        params[0] = Parade.constructAbsolutePath(context, params[0]);

        Object[] result = CvsController.onCheck(context, params);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");

        return (mapping.findForward("command"));

    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String[] params = request.getParameterValues("params");
        String path = params[0];

        // we need to convert the relative path displayed in the webapp to something usable
        params[0] = Parade.constructAbsolutePath(context, params[0]);

        Object[] result = CvsController.onUpdate(context, params);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");

        return (mapping.findForward("command"));

    }

    public ActionForward rupdate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String[] params = request.getParameterValues("params");
        String path = params[0];

        // we need to convert the relative path displayed in the webapp to something usable
        params[0] = Parade.constructAbsolutePath(context, params[0]);

        Object[] result = CvsController.onRUpdate(context, params);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");

        return (mapping.findForward("command"));

    }

    public ActionForward commit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        String[] files = request.getParameterValues("file");
        String context = request.getParameter("context");
        if(files.length > 1 && context == null) {
            context = (String)request.getSession().getAttribute("currentContext");
        }
        
        if(context == null) {
            // doh. FIXME
            context="";
        }
        
        String path = request.getParameter("path");
        String message = request.getParameter("message");
        
        // we reconstruct the absolute paths (the ones passed as params are relative)
        path = Parade.constructAbsolutePath(context, path);

        Object[] result = CvsController.onCommit(context, files, message);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");

        return (mapping.findForward("command"));

    }

    public ActionForward diff(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");

        // we reconstruct the absolute paths (the ones passed as params are relative
        String absolutePath = Parade.constructAbsolutePath(context, path);
        String absoluteFilePath = Parade.constructAbsolutePath(context, file);

        Object[] result = CvsController.onDiff(context, absolutePath, absoluteFilePath);
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

        // we reconstruct the absolute paths (the ones passed as params are relative
        String absolutePath = Parade.constructAbsolutePath(context, path);
        String absoluteFilePath = Parade.constructAbsolutePath(context, file);

        Object[] result = CvsController.onAdd(context, absolutePath, absoluteFilePath);
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

        // we reconstruct the absolute paths (the ones passed as params are relative
        String absolutePath = Parade.constructAbsolutePath(context, path);
        String absoluteFilePath = Parade.constructAbsolutePath(context, file);

        Object[] result = CvsController.onAddBinary(context, absolutePath, absoluteFilePath);
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

        // we reconstruct the absolute paths (the ones passed as params are relative
        String absolutePath = Parade.constructAbsolutePath(context, path);
        String absoluteFilePath = Parade.constructAbsolutePath(context, file);

        Object[] result = CvsController.onUpdateFile(context, absolutePath, absoluteFilePath);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");

        return (mapping.findForward("command"));

    }

    public ActionForward overridefile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String file = request.getParameter("file");
        String path = request.getParameter("path");

        // we reconstruct the absolute paths (the ones passed as params are relative
        String absolutePath = Parade.constructAbsolutePath(context, path);
        String absoluteFilePath = Parade.constructAbsolutePath(context, file);
        
        String[] params = { file.substring(path.length() + 1), path };
        Object[] result = CommandController.onDeleteFile(context, params);
        result = CvsController.onUpdateFile(context, absolutePath, absoluteFilePath);
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

        // we reconstruct the absolute paths (the ones passed as params are relative
        String absolutePath = Parade.constructAbsolutePath(context, path);
        String absoluteFilePath = Parade.constructAbsolutePath(context, file);

        Object[] result = CvsController.onDeleteFile(context, absolutePath, absoluteFilePath);
        request.setAttribute("result", (String) result[0]);
        request.setAttribute("success", (Boolean) result[1]);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "command");
        request.setAttribute("view", "commandOutput");

        return (mapping.findForward("command"));

    }

}
