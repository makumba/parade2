package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.view.interfaces.FileView;
import org.makumba.parade.view.interfaces.TreeView;

public class FileViewManager implements FileView, TreeView {

	
	public String getFileViewHeader(Row r, String path) {
		String header = "<td align='left'></td>" + //type
						"<td align='center'><b>Name</b></td>" +
						"<td align='left'>" +
						"<a href='command?view=newFile&context="+r.getRowname()+"&path="+path+"' target='command'><img src='/images/newfile.gif' border=0></a> " +
						"<a href='command?view=newDir&context="+r.getRowname()+"&path="+path+"' target='command'><img src='/images/newfolder.gif' border=0></a>" +
						"</td>"+
						"<td align='left'><b>Age</b></td>" +
						"<td align='left'><b>Size</b></td>";
		return header;
	}
	
	public String getFileView(Row r, String path, File f) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		if(f.getIsDir()) {
			out.print("<td align='left'><img src='/images/folder.gif'></td>"+
					"<td align='left'><b><a href='/servlet/file?context="+r.getRowname()+
					"&path="+f.getPath()+"'>"+f.getName()+
					"<td></td>"+
					"</a></b></td>");
		} else {
			out.print("<td align='left'><img src='/images/layout.gif'></td>"+
					"<td align='left'>"+f.getName()+"</td>"+
					"<td align='left'><a href=edit?context="+r.getRowname()+"&path="+path+"&file="+f.getPath()+"><img src='/images/edit.gif' border=0 alt='Edit "+f.getName()+"'></a></td>");
		}
		
		out.print("</td><td align='left'>"+ViewManager.readableTime(f.getAge().longValue()));
		if(!f.getIsDir()) {
			out.print("</td><td align='left'>"+ViewManager.readableBytes(f.getSize().longValue()));
		} else {
			out.print("</td><td align='left'>");
		}
		

		return result.toString();
	}

	public String getTreeView(Parade p, Row r) {
		
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		out.println("<HTML><HEAD><TITLE>"+r.getRowname()+" tree</TITLE>"+
		"</HEAD><BODY>");
		
		List dirs = FileManager.getSubdirs(r,r.getRowpath());
		
		for(Iterator i = dirs.iterator(); i.hasNext();) {
			String curDir = (String)i.next();
			out.println("<b><a href='file?context="+r.getRowname()+"&path="+curDir+"' target='directory'>"+curDir.substring(r.getRowpath().length(),curDir.length())+"</a><br>");
		}
		
		out.println("</BODY></HTML>");
		
		return result.toString();
	}
	
	private String getJSTreeView(Parade p, String context, String s) {
		Row r = (Row) p.getRows().get(context);
		if(r==null) return "Unknown context "+context;
		
		// get a list of all the dirs
		
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		// defaults:
		String size="normal"; 
		String fontSize="0.7em";
		String imagePath="imagesCompact";
		if(s!=null) size=s; 
		if(size.toLowerCase().equals("big"))
		   { 
		     fontSize="1em";
		   }
		
		out.println("<html><head><title>"+r.getRowname()+ "tree</title> \n");
		out.println("<style type=\"text/css\">");
		out.println("A {"+
		"     color:black;"+
		"     text-decoration:none;"+
		"     font-family:Tahoma,Arial;"+
		"     font-size:<%=fontSize%>;"+
		"}"+
		"A:active {"+ 
		"     color:white;"+
		"     background:rgb(0,0,120);"+
		"}"+
		"</style>"+
		"</head>"+
		
		"<body>");
		
		/*
		List tree= null;
		try{	
		tree=FileManager.setChildren(Config.readDomainData("tree", pageContext));
		}catch(ParadeException e)
			{ out.println("error during reading data "+e); return; }
		*/
		
		out.println("<script src='treeMenu/sniffer.js'></script>"+
				"<script src='treeMenu/TreeMenu.js'></script>"+
				"<div id='menuLayer"+r.getRowname()+"'></div>");
	
		if(size.equals("normal")) {
			out.println("<a href='?context="+r.getRowname()+"&size=big' title='Show bigger'>"+
						"<img src='/images/magnify.gif' align='right' border='0'></a>");
		 } else {
			 out.println("<a href='?context="+r.getRowname()+"&size=big' title='Show smaller'>" +
			 			"<img src='/images/magnify.gif' align='right' border='0'></a>");
		}
		
		out.println("<script language='javascript' type='text/javascript'>" +
					"objTreeMenu = new TreeMenu('menuLayer"+r.getRowname()+"', '/treeMenu/images', 'objTreeMenu', 'directory');"+
					"objTreeMenu.n[0] = new TreeNode('"+(r.getRowname()==""?"(root)":r.getRowname())+">'," +
					"'folder.gif', 'files.jsp?context=<%=context%>', false);");
	/*
						<jsp:include page="treeBranch.jsp">
			<jsp:param name="context" value="<%=context%>" />
			<jsp:param name="path" value="" />
			<jsp:param name="depth" value="0" />
			<jsp:param name="level" value="1" />
			<jsp:param name="treeMenu" value="bla" />
		</jsp:include>
		*/
		out.println("objTreeMenu.drawMenu();"+
					"objTreeMenu.resetBranches();"+
		"</script>");
		
		return null;
	}

}
