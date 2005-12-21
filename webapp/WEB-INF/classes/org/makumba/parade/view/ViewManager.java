package org.makumba.parade.view;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import javax.servlet.ServletRequest;

import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;

public class ViewManager {
	
	private Parade p;
	private ServletRequest req;
	
	public void getView(PrintWriter out) {
		
		String browse = (String)req.getParameter("browse");
		
		//we are in the browse view
		if(browse != null) {
			out.println("<HTML><HEAD><TITLE>"+browse+" browser</TITLE>"+
						"</HEAD><BODY><CENTER>");
			
			out.println("Welcome to the browse view of row "+browse);
			
			out.println("</TABLE></CENTER></BODY></HTML>");
			
		}
		
		//we are in the table view
		if(browse == null) {
			RowStoreViewManager rowstoreV = new RowStoreViewManager();
			CVSViewManager cvsV = new CVSViewManager();
			AntViewManager antV = new AntViewManager();
			MakumbaViewManager makV = new MakumbaViewManager();
			
			
			out.println("<HTML><HEAD><TITLE>Welcome to ParaDe</TITLE>"+
						"</HEAD><BODY><CENTER>"+
						"<TABLE>");
			
			// printing headers
			out.print("<TR bgcolor=#ddddff>");
			
			out.print("<TD align='center'>");
			out.println(rowstoreV.getParadeViewHeader());
			out.print("</TD>");
			out.print("<TD align='center'>");
			out.println(cvsV.getParadeViewHeader());
			out.print("</TD>");
			out.print("<TD align='center'>");
			out.println(antV.getParadeViewHeader());
			out.print("</TD>");
			out.print("<TD align='center'>");
			out.println(makV.getParadeViewHeader());
			out.print("</TD>");
			
			
			out.print("</TR>");
			
			// printing row information
			Iterator i = p.getRows().keySet().iterator();
			while(i.hasNext()) {
				String key = (String) i.next();
				
				out.print("<TR bgcolor=#f5f5ff>");
				
				out.print("<TD align='center'>");
				out.println(rowstoreV.getParadeView((Row) p.getRows().get(key)));
				out.print("</TD>");
				out.print("<TD align='center'>");
				out.println(cvsV.getParadeView((Row) p.getRows().get(key)));
				out.print("</TD>");
				out.print("<TD align='center'>");
				out.println(antV.getParadeView((Row) p.getRows().get(key)));
				out.print("</TD>");
				out.print("<TD align='center'>");
				out.println(makV.getParadeView((Row) p.getRows().get(key)));
				out.print("</TD>");
				
				
				out.print("</TR>");

			}

			out.println("</TABLE></CENTER></BODY></HTML>");
		}
		
		
		
		
	}
	
	public ViewManager(Parade p, ServletRequest req) {
		this.p = p;
		this.req = req;
	}

}
