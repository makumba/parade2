package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import org.makumba.parade.model.File;
import org.makumba.parade.model.FileCVS;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowCVS;
import org.makumba.parade.view.interfaces.FileView;
import org.makumba.parade.view.interfaces.HeaderView;
import org.makumba.parade.view.interfaces.ParadeView;

public class CVSViewManager implements ParadeView, FileView, HeaderView {

	public String getParadeViewHeader() {
		String header = "<b>CVS module, user, branch</b>";
		return header;
	}
	
	public String getParadeView(Row r) {
		RowCVS cvsdata = (RowCVS) r.getRowdata().get("cvs");
		
		String view = cvsdata.getUser() + ",<b>" + cvsdata.getModule()+ "</b>,"+ cvsdata.getBranch();
		return view;
	}

	public String getFileView(Row r, String path, File f) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		FileCVS cvsdata = (FileCVS) f.getFiledata().get("cvs");
		RowCVS rowcvsdata = (RowCVS) r.getRowdata().get("cvs");
		
		out.print("<td align='left'>");
		
		String cvscommand="";
		String cvscommit="";
		try {
			cvscommand = "<a target='command' href=command.jsp?&entry="+java.net.URLEncoder.encode(f.getPath(),"UTF-8")+"&op=cvs&cvs.op=";
			cvscommit = "<a target='command' href=cvsCommit.jsp?reload=&entry="+java.net.URLEncoder.encode(f.getPath(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO make this personaliseable. should go with the application definition, once admin interface done
		String cvsweb="http://cvs.makumba.org/cgi-bin/cvsweb.cgi/";
		String webPath = (f.getPath().substring(f.getRow().getRowpath().length())).replace(java.io.File.separatorChar,'/');
		String cvswebLink=cvsweb+rowcvsdata.getModule()+webPath;
		
		// if there's no CVS data
		if(cvsdata == null || f.isNotOnDisk()) {
			if( f.getName().startsWith(".#") ) {
				out.print("<a title='Backup of your working file, can be deleted once you resolved its conflicts with CVS'>Conflict Backup</a>");
			
			} else { // show options to add to cvs
				out.print(cvscommand+"add&reload=><img src='/images/cvs-add.gif' alt='add' border='0'></a>"+
							cvscommand+"add&cvs.op.-kb=&reload=><img src='/images/cvs-add-binary.gif' alt='add binary' border='0'></a>");
			}
			return result.toString();
		}
		switch(cvsdata.getStatus().intValue()){
		
		case 101:{ // IGNORED
			out.print("<i><font size=-2><font color='#999999'><i>ignored</i></font></font></i>");
		}
		break;
		
		case -1:{ // UNKNOWN
			out.print("???");
		}
		break;
		
		case 100:{ // UP_TO_DATE
			if(f.getIsDir()) {
				out.print("<a href='"+cvswebLink+"' title='CVS log'>(dir)</a>");
			} else {
				out.print("<a href='"+cvswebLink+"' title='CVS log'>"+cvsdata.getRevision()+"</a>");
			}
			
		}
		break;
		
		case 1:{ // LOCALLY_MODIFIED
			if(f.getIsDir()) {
				out.print("<a href='"+cvswebLink+"' title='CVS log'>(dir)</a>");
			} else {
				out.println("<a href='"+cvswebLink+"' title='CVS log'>"+cvsdata.getRevision()+"</a>"+
						  cvscommit+"<img src='/images/cvs-committ.gif' alt='CVS committ' border='0'></a>"+
						  cvscommand+"diff><img src='/images/cvs-diff.gif' alt='CVS diff' border='0'></a>");
			}
			
			
		}
		break;
		
		case 2:{ // NEEDS_CHECKOUT
			if(f.getIsDir()) {
				out.print("<a href='"+cvswebLink+"' title='CVS log'>(dir)</a>");
			} else {
				out.println("<a href='"+cvswebLink+"' title='CVS log'>"+cvsdata.getRevision()+"</a>"+
						cvscommand+"update&reload=><img src='/images/cvs-update.gif' alt='CVS checkout' border='0'></a>"+
						cvscommand+"delete&reload=><img src='/images/cvs-remove.gif' alt='CVS remove' border='0'></a>");
			}
			
		}
		break;
		
		case 3:{ // NEEDS_UPDATE
			if(f.getIsDir()) {
				out.print("<a href='"+cvswebLink+"' title='CVS log'>(dir)</a>");
			} else {
				out.println("<a href='"+cvswebLink+"' title='CVS log'>"+cvsdata.getRevision()+"</a>"+
						cvscommand+"update&reload=><img src='/images/cvs-update.gif' alt='CVS update' border='0'></a>");
			}
			
		}
		break;
		
		case 4:{ // ADDED
			out.println(cvsdata.getRevision()+
					cvscommit+"><img src='/images/cvs-committ.gif' alt='CVS committ' border='0'></a>");
		}
		break;
		
		case 5:{ // DELETED
			out.println("<a href='"+cvswebLink+"' title='CVS log'>"+cvsdata.getRevision()+"</a>"+
					cvscommit+"><img src='/images/cvs-committ.gif' alt='CVS committ' border='0'></a>");
		}
		break;
		
		case 6:{ // CONFLICT
		out.println("<a href='"+cvswebLink+"' title='CVS log'><b><font color='red'>Conflict</font></b> "+cvsdata.getRevision()+"</a>"+
					cvscommit+"<img src='/images/cvs-committ.gif' alt='CVS committ' border='0'></a>"+
					cvscommand+"diff><img src='/images/cvs-diff.gif' alt='CVS diff' border='0'></a>");
		}
		break;
		}
		
		out.println("</td>");
		
		return result.toString();
	}

	public String getFileViewHeader(Row r, String path) {
		String header = "<td align='left'><b>CVS</b></td>";
		return header;
	}

	public String getHeaderView(Row r) {
		/*
		<%-- $Header$ --%>
<br>CVS: 
<%
String cvscommand="<a target='command' href=command.jsp?"+pageContext.findAttribute("parade.sameDir")+"&cvs.perDir=&op=cvs&cvs.op=";
%>
<%=cvscommand%>update&cvs.-l=&cvs.-n=&cvs.op.-d=&cvs.op.-P=>check status</a>
<%=cvscommand%>update&cvs.op.-d=&cvs.op.-P=&cvs.op.-l=&reload=>local update</a>
<%=cvscommand%>update&cvs.op.-d=&cvs.op.-P=&reload=>recursive update</a>

		 * 
		 */
		return null;
	}

}
