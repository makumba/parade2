package org.makumba.parade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;

public class RowsAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String op = request.getParameter("op");

        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));

        if (op != null && op.equals("row")) {
            String context = request.getParameter("context");
            if (context == null) {
                s.close();
                request.setAttribute("result", "Error: no context given");
                request.setAttribute("success", new Boolean(false));
                return mapping.findForward("index");
            }

            Row r = p.getRows().get(context);
            if (r == null) {
                s.close();
                request.setAttribute("result", "Error: no row corresponding to context " + context);
                request.setAttribute("success", new Boolean(false));
                return mapping.findForward("index");
            }

            p.rebuildRowCache(r);

            tx.commit();
            s.close();

            request.setAttribute("result", "Row " + context + " refreshed !");
            request.setAttribute("success", new Boolean(true));

            return mapping.findForward("index");

        } else if (op != null && op.equals("parade")) {
            p.refresh();
            try {
                p.addJNotifyListeners();
            } catch (Throwable e) {
                e.printStackTrace();
            }

            tx.commit();
            s.close();

            request.setAttribute("result", "ParaDe refreshed !");
            request.setAttribute("success", new Boolean(true));

            return mapping.findForward("index");

        } else if (op != null && op.equals("newRow")) {

            p.createNewRows();

            tx.commit();
            s.close();

            request.setAttribute("result", "New rows added!");
            request.setAttribute("success", new Boolean(true));

            return mapping.findForward("index");

        } else {

            request.setAttribute("result", "Error: wrong op parameter");
            request.setAttribute("success", new Boolean(false));
            return mapping.findForward("index");

        }

    }
}
