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
import org.makumba.parade.model.Row;
import org.makumba.parade.view.managers.HeaderViewManager;

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
		PrintWriter out = resp.getWriter();
		
		Session s = InitServlet.getSessionFactory().openSession();
		Transaction tx = s.beginTransaction();
		
		
		Parade p = (Parade) s.get(Parade.class, new Long(1));
		String context = (String)req.getParameter("context");
		
		Row r = (Row)p.getRows().get(context);
		if(r == null) {
			out.println("Unknown context "+context);
		} else {
			resp.setContentType("text/html");
			resp.setCharacterEncoding("UTF-8");
						
			HeaderViewManager hdrV = new HeaderViewManager();
			out.println(hdrV.getHeaderView(r));
		}
		
		
		
		tx.commit();
		
		s.close();
	
	}
}
