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
	
	public void init(ServletConfig conf) {
		try {
			super.init();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void service(ServletRequest req, ServletResponse resp) throws java.io.IOException, ServletException {

		Session s = InitServlet.getSessionFactory().openSession();
		Transaction tx = s.beginTransaction();
		
		Parade p = (Parade) s.get(Parade.class, new Long(1));
		
		resp.setContentType("text/html");
		String context = (String) req.getParameter("context");
		PrintWriter out = resp.getWriter();
		ViewManager viewMgr = new ViewManager();
		out.print(viewMgr.getView(p,context));
		
		tx.commit();
		
		s.close();
	
	}
}
