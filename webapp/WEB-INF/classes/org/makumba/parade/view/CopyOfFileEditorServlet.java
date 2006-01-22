package org.makumba.parade.view;

import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.view.managers.FileEditViewManager;

public class CopyOfFileEditorServlet extends HttpServlet {

    public void init() {
    }

    public void service(ServletRequest req, ServletResponse resp) throws java.io.IOException, ServletException {
        PrintWriter out = resp.getWriter();

        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));
        String context = (String) req.getParameter("context");
        String filePath = (String) req.getParameter("file");
        String path = (String) req.getParameter("path");
        String[] source = req.getParameterValues("source");

        Row r = (Row) p.getRows().get(context);
        if (r == null) {
            out.println("Unknown context " + context);
        } else {
            File file = (File) r.getFiles().get(filePath);
            if (file == null) {
                out.println("Unknown file " + filePath);
            } else {
                resp.setContentType("text/html");
                resp.setCharacterEncoding("UTF-8");
                FileEditViewManager fileEditV = new FileEditViewManager();
                out.println(fileEditV.getFileEditorView(r, path, file, source));
            }
        }

        tx.commit();

        s.close();

    }
}
