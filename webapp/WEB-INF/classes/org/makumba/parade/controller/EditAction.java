package org.makumba.parade.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class EditAction extends DispatchAction {

    public ActionForward editFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String op = request.getParameter("op");
        String[] params = request.getParameterValues("params");

        /*
         * if(op != null && op.startsWith("newFile")) { request.setAttribute("result",
         * CommandController.onNewFile(context, params)); request.setAttribute("success", "true");
         * request.setAttribute("context", context); request.setAttribute("path", params[1]);
         * request.setAttribute("display", "file");
         *  }
         */

        return (mapping.findForward("files"));

    }

}
