package org.makumba.parade.view.managers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.view.interfaces.CommandView;

public class CommandViewManager implements CommandView {

	public String getCommandView(String view, Row r, String path, String file) {
		if(view == null || view.equals(""))
			return "include:/tipOfTheDay.jsp";
		if(view!=null && view.equals("newFile")) {
			return newFileView(r, path);
		}
		if(view!=null && view.equals("newDir")) {
			return newDirView(r, path);
		}
		
		return "No such view defined for Command";
	}

/*
 * <form action="editFile.jsp" target="directory">
<%
String context=request.getParameterValues("context")[0];

String path="";
if(request.getParameterValues("path")!=null)
	path=request.getParameterValues("path")[0];
%>
<input type=hidden size="50" value="<%=path.length()>1?path+java.io.File.separator:""%>" name=path>
<input type=hidden value="<%=context%>" name=context>
Create new file:<input type=text name=file>
<input type=submit value=Edit>
</form>
 */

	private String newFileView(Row r, String path) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		out.println(
"<html><head><title>Command view for "+r.getRowname()+"</title></head><body>\n"+
"<form action='/?handler=file' target='_top' method='POST'>\n"+
"<input type=hidden value='"+r.getRowname()+"' name=context>\n"+
"<input type=hidden value='newFile#"+path+"' name=op>\n"+
"Create new file: <input type=text name=entry>\n"+
"<input type=submit value=Create>\n"+
"</form>\n"+
"</body</html>\n"
		);
		
		return result.toString();
	}
	
	private String newDirView(Row r, String path) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		out.println(
"<html><head><title>Command view for "+r.getRowname()+"</title></head><body>\n"+
"<form action='/?handler=file' target='_top' method='POST'>\n"+
"<input type=hidden value='"+r.getRowname()+"' name=context>\n"+
"<input type=hidden value='newDir#"+path+"' name=op>\n"+
"Create new directory: <input type=text name=entry>\n"+
"<input type=submit value=Create>\n"+
"</form>\n"+
"</body</html>\n"
		);
		
		return result.toString();
	}
	
	public String uploadFile(String path, String file, Object content, Object context) {
		
		boolean success = true;
		
		try {
			  OutputStream dest= new BufferedOutputStream(new FileOutputStream(path+File.separator+file));
			  ((org.makumba.Text)content).writeTo(dest);
			  dest.close();
		} catch(Exception ew) {
			success = false;
			return("Error writing file: "+ew);
		}
				
		if(success) {
			Session s = InitServlet.getSessionFactory().openSession();
			Transaction tx = s.beginTransaction();
			
			Parade p = (Parade) s.get(Parade.class, new Long(1));
			FileManager fileMgr = new FileManager();
			fileMgr.uploadFile(p, path+File.separator+file, (String) context);
			
			tx.commit();
			s.close();
		}
		return("");
	}
	
}
