package org.makumba.parade.view;

import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.hibernate.Session;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.view.managers.LogViewManager;

public class LogViewServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void init() {
    }

    @Override
    public void service(ServletRequest req, ServletResponse resp) throws java.io.IOException, ServletException {
        PrintWriter out = resp.getWriter();

        Session s = InitServlet.getSessionFactory().openSession();

        String context = null;
        Object ctxValues = req.getParameterValues("context");
        if (ctxValues != null)
            context = (String) (((Object[]) ctxValues))[0];
        if (context == null)
            context = "all";

        String view = req.getParameter("view");
        if (view == null)
            view = "log";

        Calendar now = Calendar.getInstance();

        String years = req.getParameter("year");
        if (years == null || years.equals("") || years.equals("null"))
            years = Integer.valueOf(now.get(Calendar.YEAR)).toString();
        String months = req.getParameter("month");
        if (months == null || months.equals("") || months.equals("null"))
            months = Integer.valueOf(now.get(Calendar.MONTH) + 1).toString();
        String days = req.getParameter("day");
        if (days == null || days.equals("") || days.equals("null"))
            days = Integer.valueOf(now.get(Calendar.DAY_OF_MONTH)).toString();
        String filter = req.getParameter("filter");
        if (filter == null || filter.equals("") || filter.equals("null"))
            filter = "none";

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        LogViewManager logV = new LogViewManager();
        
        if (view.equals("logmenu")) {
            out.println(logV.getLogMenuView(s, context, filter, Integer.parseInt(years),
                    (Integer.parseInt(months)) - 1, (Integer.parseInt(days))));
        } else if (view.equals("actionlog")) {
            out.println(logV.getActionLogView(s, context));
        } else {
            out.println(logV.getLogView(s, context, filter, Integer.parseInt(years), (Integer.parseInt(months)) - 1,
                    (Integer.parseInt(days))));
        }
        
        s.close();

    }

}
