package org.makumba.parade.view.managers;

import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowAnt;
import org.makumba.parade.view.interfaces.ParadeView;
import org.makumba.parade.view.interfaces.HeaderView;

public class AntViewManager implements ParadeView, HeaderView {

	public String getParadeViewHeader() {
		String header = "<b>Ant buildfile</b>";
		return header;
	}
	
	public String getParadeView(Row r) {
		RowAnt antdata = (RowAnt) r.getRowdata().get("ant");
		
		String view = antdata.getBuildfile();
		return view;
	}

	public String getHeaderView(Row r) {
		// TODO Auto-generated method stub
		/*
		 * ant: 

		String sep="";
for(Iterator i= ((Collection)pageContext.findAttribute("ant.topTargets")).iterator(); i.hasNext(); )
{
String s=((String[])i.next())[0];
for(Iterator j=Config.getColumns("ant.allowedOps"); j.hasNext(); )
{ 

	if(!s.equals(j.next()))
		continue;
%><%=sep%> <a href=paradeCommand.jsp?op=executeAntCommand&entry=<%=pageContext.findAttribute("parade.row")%>&antCommand=<%=s%> ><%=s%></a><%
sep=",";
}
}
for(Iterator i= ((Collection)pageContext.findAttribute("ant.subTargets")).iterator(); i.hasNext(); )
{
	String s=((String)i.next());
for(Iterator j=Config.getColumns("ant.allowedOps"); j.hasNext(); )
{ 
	if(!s.equals(j.next()))
		continue;
%><%=sep%> <a href=paradeCommand.jsp?op=executeAntCommand&entry=<%=pageContext.findAttribute("parade.row")%>&antCommand=<%=s%> ><%=s%></a><%
sep=",";
}
}
		 * 
		 * 
		 * 
		 */
		return null;
	}

	

}
