package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import org.makumba.parade.init.ParadeProperties;
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
		
		String view = antdata.getBuildfile()+"<br>\n";
		view+=getTargets(r);
		return view;
	}

	public String getHeaderView(Row r) {
		return "&nbsp; ant: "+getTargets(r);
	}
	
	private String getTargets(Row r) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		RowAnt data = (RowAnt) r.getRowdata().get("ant");
		
		for(Iterator i = ParadeProperties.getElements("ant.allowedOps").iterator(); i.hasNext();) {
			String allowed = (String) i.next();
			for(Iterator j = data.getTargets().iterator(); j.hasNext();) {
				String target = (String) j.next();
				if(!target.equals(allowed))
					continue;
				out.print("<a href=?handler=ant&op=doSomething>"+target+"</a>");
				if(i.hasNext())
					out.println(",");
			}
		}
		
		return result.toString();
	}
}
