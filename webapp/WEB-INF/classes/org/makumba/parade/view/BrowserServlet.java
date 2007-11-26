package org.makumba.parade.view;

import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.view.managers.CommandViewManager;
import org.makumba.parade.view.managers.FileDisplay;
import org.makumba.parade.view.managers.FileViewManager;
import org.makumba.parade.view.managers.HeaderDisplay;

public class BrowserServlet extends HttpServlet {

    public void init() {
    }

    public synchronized void service(ServletRequest req, ServletResponse resp) throws java.io.IOException,
            ServletException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        
        // fetching parameters
        String display = req.getParameter("display");
        if (display == null)
            display = (String) req.getAttribute("display");

        String context = req.getParameter("context");
        if (context == null)
            context = (String) req.getAttribute("context");

        String view = req.getParameter("view");
        if (view == null)
            view = (String) req.getAttribute("view");

        String file = req.getParameter("file");
        if (file == null)
            file = (String) req.getAttribute("file");

        String path = null;
        
        String getPathFromSession = req.getParameter("getPathFromSession");
        if(getPathFromSession != null) {
            path = (String) ((HttpServletRequest)req).getSession().getAttribute("path");
        } else {
            path = req.getParameter("path");
        }
        if (path == null)
            path = (String) req.getAttribute("path");
        
        if(path != null && path != "") ((HttpServletRequest)req).getSession().setAttribute("path", path);
        
        String opResult = (String) req.getAttribute("result");
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
        
        Row r = (Row) p.getRows().get(context);
        if (r == null) {
            out.println("Unknown context " + context);
        } else {
            
            //fetching data from the persistent store
            //this is needed for lazy connections
            //r.getFiles().size();
            Hibernate.initialize(r.getFiles());

            // initialising the displays
            HeaderDisplay hdrV = new HeaderDisplay();
            CommandViewManager cmdV = new CommandViewManager();
            FileViewManager fileV = new FileViewManager();
            FileDisplay filebrowserV = new FileDisplay();

            // switiching to the right display
            String page = "";
            if (display.equals("header")) {
                // FIXME - path in here is null always, but should actually be equal to the currently browsed path
                page = hdrV.getHeaderView(r, path);
            }
            if (display.equals("tree")) {
                page = fileV.getTreeView(p, r);
            }
            if (display.equals("file")) {
                page = filebrowserV.getFileBrowserView(p, r, path, opResult, success);
            }
            if (display.equals("command")) {
                page = cmdV.getCommandView(view, r, path, file, opResult);
            }

            // checking whether we include a JSP or not
            if (page.startsWith("jsp:")) {
                String url = page.substring(page.indexOf(":") + 1);
                RequestDispatcher dispatcher = super.getServletContext().getRequestDispatcher(url);
                dispatcher.forward(req, resp);
            } else {
                out.println(page);
            }

        }

        tx.commit();
        s.close();

    }

}
