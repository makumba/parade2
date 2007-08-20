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
import org.makumba.parade.view.managers.CodePressFileEditViewManager;
import org.makumba.parade.view.managers.FileEditViewManager;

public class FileEditorServlet extends HttpServlet {

	public void init() {}
	
	public void service(ServletRequest req, ServletResponse resp) throws java.io.IOException, ServletException {
		PrintWriter out = resp.getWriter();
		
		Session s = InitServlet.getSessionFactory().openSession();
		Transaction tx = s.beginTransaction();
		
        /* context - the context / rowname
         * file - the path to the file to be edited
         * path - the relative path (displayed to the user)
         * 
         */
		
		Parade p = (Parade) s.get(Parade.class, new Long(1));
		String context = (String)req.getParameter("context");
		String fileName = (String)req.getParameter("file");
		String path = (String)req.getParameter("path");
        String[] source =  req.getParameterValues("source");
		
		
		Row r = (Row)p.getRows().get(context);
		if(r == null) {
			out.println("Unknown context "+context);
		} else {
            
            // we need to build the absolute Path to the file
            String absoluteFilePath = Parade.constructAbsolutePath(context, path) + java.io.File.separator + fileName;
            
			File file = (File) r.getFiles().get(absoluteFilePath);
			if(file == null) {
				out.println("Internal ParaDe error: cannot access file "+absoluteFilePath);
			} else {
                resp.setContentType("text/html");
				resp.setCharacterEncoding("UTF-8");
				
                // TODO we should give the possibility to choose which editor to use
                // but maybe it's not that relevant anymore since almost everyone has JS
                
                //FileEditViewManager fileEditV = new FileEditViewManager();
				
                //uncomment here to toggle to codepress
                CodePressFileEditViewManager fileEditV = new CodePressFileEditViewManager();
                
                out.println(fileEditV.getFileEditorView(r, path, file, source));
			}
		}
		
		tx.commit();
		
		s.close();
	
	}
}
