package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.view.interfaces.FileView;
import org.makumba.parade.view.interfaces.TreeView;

public class FileViewManager implements FileView, TreeView {

	
	public String getFileViewHeader() {
		String header = "<b>Name</b></td><td align='center'><b>Age</b></td><td align='center'><b>Size</b>";
		return header;
	}
	
	public String getFileView(Row r, File f) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		if(f.getIsDir()) {
			out.print("<b>"+f.getName()+"</b>");
		} else {
			out.print(f.getName());
		}
		out.print("</td><td>"+ViewManager.readableTime(f.getAge().longValue()));
		out.print("</td><td>"+ViewManager.readableBytes(f.getSize().longValue()));

		return result.toString();
	}

	public String getTreeView(Parade p, Row r) {
		
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		out.println("<HTML><HEAD><TITLE>"+r.getRowname()+" tree</TITLE>"+
		"</HEAD><BODY><CENTER>");
		
		Set files = r.getFiles().keySet();
		Object[] filesArray = files.toArray();
		
		for(int i =0; i<filesArray.length;i++) {
			File f = (File) r.getFiles().get(filesArray[i]);
			try {
				if(f.getIsDir()) {
					out.println("<b><a href='?context="+r.getRowname()+"&file="+java.net.URLEncoder.encode((String)filesArray[i],"UTF-8")+"'>"+(String)filesArray[i]+"</a></b><br>");
				} else {
					out.println("<a href='?context="+r.getRowname()+"&file="+java.net.URLEncoder.encode((String)filesArray[i],"UTF-8")+"'>"+(String)filesArray[i]+"</a><br>");
				}
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		out.println("</CENTER></BODY></HTML>");
		
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