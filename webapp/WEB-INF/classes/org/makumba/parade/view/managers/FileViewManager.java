package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowWebapp;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.model.managers.ServletContainer;
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
						"<td align='left'><b>Size</b></td>"+
						
						/* javascript for deleting files
						"<script>\n"+
						"function deleteFile(params){\n"+
						"	result = window.confirm('Are you sure you want to delete this file?');\n"+
						"	if(result){\n"+
						"		url=\"?context="+r.getRowname()+"&handler=file&op=delete&params=\"+(params);\n"+
						"		window.alert(url);"+
						"		window.location.href=url;\n"+
						"	}\n"+
						"}\n"+
						"</script>\n";
						*/
						"<script language=\"JavaScript\">\n"+
						"<!-- \n"+
						"function deleteFile(path) {\n"+
						"  if(confirm('Are you sure you want to delete this file ?'))\n"+
						"  {\n"+
						"	url='?context="+r.getRowname()+"&handler=file&op=delete&params="+r.getRowname()+"%23'+encodeURIComponent(path);\n"+
						"	location.href=url;\n"+ 
						"  }\n"+
						"}\n"+
						"</script>\n";
		
		return header;
	}
	
	public String getFileView(Row r, String path, File f) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		RowWebapp webappdata = (RowWebapp) r.getRowdata().get("webapp");
		
		if(f.getIsDir()) {
			out.print("<td align='left'><img src='/images/folder.gif'></td>"+
					"<td align='left'><b><a href='/servlet/file?context="+r.getRowname()+
					"&path="+f.getPath()+"'>"+f.getName()+
					"<td></td>"+
					"</a></b></td>");
		} else {
			
			// icons
			String fl=f.getName().toLowerCase();
			String image = "unknown";

		    if(fl.endsWith(".java")) image="java"; 
		    if(fl.endsWith(".mdd") || fl.endsWith(".idd")) image="text"; 
		    if(fl.endsWith(".jsp")||fl.endsWith(".properties") || fl.endsWith(".xml") || fl.endsWith(".txt") || fl.endsWith(".conf")) image="text";
		    if(fl.endsWith(".doc")||fl.endsWith(".jsp")||fl.endsWith(".html") || fl.endsWith(".htm") || fl.endsWith(".rtf")) image="layout";
		    if(fl.endsWith(".gif")||fl.endsWith(".png") || fl.endsWith(".jpg") || fl.endsWith(".jpeg")) image="image";
		    if(fl.endsWith(".zip")||fl.endsWith(".gz") || fl.endsWith(".tgz") || fl.endsWith(".jar")) image="zip";
		    if(fl.endsWith(".avi")||fl.endsWith(".mpg") || fl.endsWith(".mpeg") || fl.endsWith(".mov")) image="movie";
		    if(fl.endsWith(".au")||fl.endsWith(".mid") || fl.endsWith(".vaw") || fl.endsWith(".mp3")) image="sound";
			
		    out.print("<td align='left'><img src='/images/"+image+".gif' border='0'></td>\n");
		    
		    // name
		    String addr="";
		    String webappPath = webappdata.getWebappPath();
		    
		    if(webappdata.getStatus().intValue() == ServletContainer.RUNNING && path.startsWith(java.io.File.separator+webappPath)) {
		    	String pathURI = path.substring(path.indexOf(webappPath)+webappPath.length()).replace(java.io.File.separator,"/")+"/";
			    		    	
		    	if(fl.endsWith(".java")) {
					String dd=pathURI+f.getName();
					dd=dd.substring(dd.indexOf("classes")+8, dd.lastIndexOf(".")).replace('/', '.');
					addr="/"+r.getRowname()+"/classes/"+dd;
			    }
			    if(fl.endsWith(".mdd") || fl.endsWith(".idd")) {
					String dd=pathURI+f.getName();
					dd=dd.substring(dd.indexOf("dataDefinitions")+16, dd.lastIndexOf(".")).replace('/', '.');
					addr="/"+r.getRowname()+"/dataDefinitions/"+dd;
					}
				if(fl.endsWith(".jsp")||fl.endsWith(".html") || fl.endsWith(".htm") || fl.endsWith(".txt") || fl.endsWith(".gif") || fl.endsWith(".png") || fl.endsWith(".jpeg") || fl.endsWith(".jpg") || fl.endsWith(".css") || fl.startsWith("readme") )
					addr="/"+r.getRowname()+pathURI+f.getName();
				if(fl.endsWith(".jsp"))
					addr+="x";
		    }
		    
			if(!addr.equals("")) {
				out.print("<td align='left'><a href='"+addr+"'>"+f.getName()+"</a></td>\n");
			} else {
				out.print("<td align='left'>"+f.getName()+"</td>\n");
			}

			// actions
		    try {
				out.print(
						"<td align='left'><a href=/servlet/edit?context="+r.getRowname()+"&path="+path+"&file="+f.getPath()+"><img src='/images/edit.gif' border=0 alt='Edit "+f.getName()+"'></a>\n"+
						"<a href=\"javascript:deleteFile('"+URLEncoder.encode(URLEncoder.encode(f.getPath(),"UTF-8"),"UTF-8")+"')\"><img src='/images/delete.gif' border='0' alt='Delete "+f.getName()+"'></a></td>"
						);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// time && size
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
