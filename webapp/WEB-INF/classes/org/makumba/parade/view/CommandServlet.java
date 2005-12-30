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
import org.makumba.parade.view.managers.CommandViewManager;
import org.makumba.parade.view.managers.FileBrowserViewManager;

public class CommandServlet extends HttpServlet {
	
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
		String context = req.getParameter("context");
		String view = req.getParameter("view");
		String path = req.getParameter("path");
		String file = req.getParameter("file");
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		
		PrintWriter out = resp.getWriter();
		
		Row r = (Row) p.getRows().get(context);
	
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		CommandViewManager cmdV = new CommandViewManager();
		out.println(cmdV.getCommandView(view, r, path, file));
	
		tx.commit();
		
		s.close();
	
	}
}
