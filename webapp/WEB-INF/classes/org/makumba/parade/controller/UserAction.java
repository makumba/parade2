package org.makumba.parade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.makumba.parade.model.managers.UserManager;

public class UserAction extends DispatchAction {

        public ActionForward newUser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                HttpServletResponse response) throws Exception {

            String login = (String) request.getSession().getAttribute("org.makumba.parade.user");
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String nickname = request.getParameter("nickname");
            String email = request.getParameter("email");
            String PAptr = request.getParameter("PAptr");
           
            UserManager userMgr = new UserManager();
            
            Object[] result = userMgr.createUser(login, name, surname, nickname, email, PAptr);
            request.getSession().setAttribute("org.makumba.parade.userObject", result[2]);
            request.setAttribute("result", (String) result[0]);
            request.setAttribute("success", (Boolean) result[1]);
            
            if((Boolean)result[1]) {
                return (mapping.findForward("index"));
            } else {
                return (mapping.findForward("newuser"));
            }
        }
}
