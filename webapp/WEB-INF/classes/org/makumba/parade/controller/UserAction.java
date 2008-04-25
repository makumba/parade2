package org.makumba.parade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.makumba.parade.model.User;
import org.makumba.parade.model.managers.UserManager;

public class UserAction extends DispatchAction {

        public ActionForward newUser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                HttpServletResponse response) throws Exception {

            String login = (String) request.getSession().getAttribute("org.makumba.parade.user");
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String nickname = request.getParameter("nickname");
            String email = request.getParameter("email");
            //String PAptr = request.getParameter("PAptr");
            
            if(name == null || name.length() == 0) {
                request.setAttribute("result", "Please provide your name");
                request.setAttribute("success", false);
                return (mapping.findForward("newuser"));
            }

            if(surname == null || surname.length() == 0) {
                request.setAttribute("result", "Please provide your surname");
                request.setAttribute("success", false);
                return (mapping.findForward("newuser"));
            }

            if(nickname == null || nickname.length() == 0) {
                request.setAttribute("result", "Please provide your nickname");
                request.setAttribute("success", false);
                return (mapping.findForward("newuser"));
            }
            
            if(email == null || email.length() == 0) {
                request.setAttribute("result", "Please provide your email");
                request.setAttribute("success", false);
                return (mapping.findForward("newuser"));
            }
            
            UserManager userMgr = new UserManager();
            
            Object[] result = userMgr.createUser(login, name, surname, nickname, email);
            User u = (User) result[2];
            request.getSession(true).setAttribute("org.makumba.parade.userObject", u);
            request.getSession(true).setAttribute("user_login", u.getLogin());
            request.getSession(true).setAttribute("user_name", u.getName());
            request.getSession(true).setAttribute("user_surname", u.getSurname());
            request.getSession(true).setAttribute("user_nickname", u.getNickname());
            request.getSession(true).setAttribute("user_email", u.getEmail());
            
            request.setAttribute("result", (String) result[0]);
            request.setAttribute("success", (Boolean) result[1]);
            
            if((Boolean)result[1]) {
                return (mapping.findForward("index"));
            } else {
                return (mapping.findForward("newuser"));
            }
        }
}
