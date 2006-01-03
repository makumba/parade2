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
import org.makumba.parade.model.Parade;
import org.makumba.parade.view.managers.ViewManager;

public class IndexServlet extends HttpServlet {
	
	public void init() {}

	public void service(ServletRequest req, ServletResponse resp) throws java.io.IOException, ServletException {
		PrintWriter out = resp.getWriter();
		
		Session s = InitServlet.getSessionFactory().openSession();
		Transaction tx = s.beginTransaction();
		
		Parade p = (Parade) s.get(Parade.class, new Long(1));
		String context = req.getParameter("context");
		String op = req.getParameter("op");
		String handler = req.getParameter("handler");
		String entry = req.getParameter("entry");
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		
		ViewManager viewMgr = new ViewManager();
		out.print(viewMgr.getView(p, context, handler, op, entry));
		
		tx.commit();
		
		s.close();
	
	}
}
