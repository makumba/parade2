package org.makumba.parade.view;

import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.view.managers.RowDisplay;

public class IndexServlet extends HttpServlet {

    public void init() {
    }

    public void service(ServletRequest req, ServletResponse resp) throws java.io.IOException, ServletException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        String context = req.getParameter("context");
        if (context == null)
            context = (String) req.getAttribute("context");
        
        String opResult = req.getParameter("opResult");
        if (opResult == null)
            opResult = (String) req.getAttribute("opResult");
        
        Boolean successAttr = (Boolean) req.getAttribute("success");
        boolean success = true;
        if (successAttr == null)
            success = false;
        else
            success = successAttr.booleanValue();

        PrintWriter out = resp.getWriter();

        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));

        if (context != null) {
            RequestDispatcher dispatcher = super.getServletContext().getRequestDispatcher("/servlet/browse");
            dispatcher.forward(req, resp);
        }

        RowDisplay rowDisp = new RowDisplay();
        out.print(rowDisp.getView(p, context, opResult, success));

        tx.commit();

        s.close();

    }
}
