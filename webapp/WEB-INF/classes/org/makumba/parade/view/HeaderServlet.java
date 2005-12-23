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

public class HeaderServlet extends HttpServlet {

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
		String context = (String)req.getParameter("context");
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		/*
		FileViewManager fileV = new FileViewManager();
		out.println(fileV.getTreeView(p,context));
		*/
		out.println("<HTML><BODY>Here will come the header</BODY></HTML>");
		
		tx.commit();
		
		s.close();
	
	}
}
