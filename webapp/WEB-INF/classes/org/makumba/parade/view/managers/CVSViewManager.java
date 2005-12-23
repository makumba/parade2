package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import org.makumba.parade.model.File;
import org.makumba.parade.model.FileCVS;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowCVS;
import org.makumba.parade.view.interfaces.FileView;
import org.makumba.parade.view.interfaces.ParadeView;

public class CVSViewManager implements ParadeView, FileView {

	public String getParadeViewHeader() {
		String header = "<b>CVS module, user, branch</b>";
		return header;
	}
	
	public String getParadeView(Row r) {
		RowCVS cvsdata = (RowCVS) r.getRowdata().get("cvs");
		
		String view = cvsdata.getUser() + ",<b>" + cvsdata.getModule()+ "</b>,"+ cvsdata.getBranch();
		return view;
	}

	public String getFileView(Row r, File f) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		FileCVS cvsdata = (FileCVS) f.getFiledata().get("cvs");
		RowCVS rowcvsdata = (RowCVS) r.getRowdata().get("cvs");
		
		String cvscommand="";
		String cvscommitt="";
		try {
			cvscommand = "<a target='command' href=command.jsp?&entry="+java.net.URLEncoder.encode(f.getPath(),"UTF-8")+"&op=cvs&cvs.op=";
			cvscommitt = "<a target='command' href=cvsCommit.jsp?reload=&entry="+java.net.URLEncoder.encode(f.getPath(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String cvsweb="http://cvs.makumba.org/cgi-bin/cvsweb.cgi/"; //this should go to 
		String cvswebLink=cvsweb+rowcvsdata.getModule()+"/"+f.getPath();
		
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
		case 101:{ // IGNORED %>
			out.print("<i><font size=-2><font color='#999999'><i>ignored</i></font></font></i>");
			break;
		}
		case -1:{ // UNKNOWN
			out.print("???");
		}
		break;
		
		case 100:{ // UP_TO_DATE
			out.print("<a href='"+cvswebLink+"' title='CVS log'>"+cvsdata.getRevision()+"</a>");
		}
		break;
		
		case 1:{ // LOCALLY_MODIFIED
			out.print("<a href='"+cvswebLink+"' title='CVS log'>"+cvsdata.getRevision()+"</a>"+
					  cvscommitt+"<img src='/images/cvs-committ.gif' alt='CVS committ' border='0'></a>"+
					  cvscommand+"diff><img src='/images/cvs-diff.gif' alt='CVS diff' border='0'></a>");
			
		}
		break;
		
		case 2:{ // NEEDS_CHECKOUT
			out.print("<a href='"+cvswebLink+"' title='CVS log'>"+cvsdata.getRevision()+"</a>"+
						cvscommand+"update&reload=><img src='/images/cvs-update.gif' alt='CVS checkout' border='0'></a>"+
						cvscommand+"delete&reload=><img src='/images/cvs-remove.gif' alt='CVS remove' border='0'></a>");
		}
		break;
		
		case 3:{ // NEEDS_UPDATE
			out.print("<a href='"+cvswebLink+"' title='CVS log'>"+cvsdata.getRevision()+"</a>"+
						cvscommand+"update&reload=><img src='/images/cvs-update.gif' alt='CVS update' border='0'></a>");
		}
		break;
		
		case 4:{ // ADDED %>
			out.print(cvsdata.getRevision()+
					cvscommitt+"><img src='/images/cvs-committ.gif' alt='CVS committ' border='0'></a>");
		}
		break;
		
		case 5:{ // DELETED
			out.print("<a href='"+cvswebLink+"' title='CVS log'>"+cvsdata.getRevision()+"</a>"+
					cvscommitt+"><img src='/images/cvs-committ.gif' alt='CVS committ' border='0'></a>");
		}
		break;
		
		case 6:{ // CONFLICT
		out.print("<a href='"+cvswebLink+"' title='CVS log'><b><font color='red'>Conflict</font></b> "+cvsdata.getRevision()+"</a>"+
					cvscommitt+"<img src='/images/cvs-committ.gif' alt='CVS committ' border='0'></a>"+
					cvscommand+"diff><img src='/images/cvs-diff.gif' alt='CVS diff' border='0'></a>");
		}
		break;
		}
		
		return result.toString();
	}

	public String getFileViewHeader() {
		String header = "<b>CVS</b>";
		return header;
	}

}
