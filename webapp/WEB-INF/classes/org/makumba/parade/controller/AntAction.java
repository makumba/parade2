package org.makumba.parade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AntAction extends Action {
    
    private AntController antCtrl = new AntController();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String op = request.getParameter("op");
        String display = request.getParameter("display");
        String path = request.getParameter("path");
        if (path == null)
            path = (String) request.getAttribute("path");
        if (path == null)
            path = "";

        Response result = antCtrl.onAntAction(context, op);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        return mapping.findForward(display);
    }
}
