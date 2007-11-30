package org.makumba.parade.view;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.hibernate.Session;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.view.managers.LogViewManager;

public class LogViewServlet extends HttpServlet {

public void init() {}
    
    public void service(ServletRequest req, ServletResponse resp) throws java.io.IOException, ServletException {
        PrintWriter out = resp.getWriter();
        
        Session s = InitServlet.getSessionFactory().openSession();
        
        String context = (String)req.getParameter("context");
        if(context == null)
            context = "all";
        
        Calendar now = GregorianCalendar.getInstance();
        
        
        String years = req.getParameter("year");
        if(years == null)
            years = Integer.valueOf(now.get(Calendar.YEAR)).toString();
        String months = req.getParameter("month");
        if(months == null)
            months = Integer.valueOf(now.get(Calendar.MONTH)+1).toString();
        String days = req.getParameter("day");
        if(days == null)
            days = Integer.valueOf(now.get(Calendar.DAY_OF_MONTH)).toString();
        

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        
        LogViewManager logV = new LogViewManager();
        
        out.println(logV.getLogView(s, context, Integer.parseInt(years), (Integer.parseInt(months))-1, (Integer.parseInt(days))));
        
        s.close();
    
    }
    
}
