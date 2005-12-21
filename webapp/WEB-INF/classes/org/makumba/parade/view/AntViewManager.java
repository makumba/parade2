package org.makumba.parade.view;

import org.makumba.parade.ifc.ParadeView;
import org.makumba.parade.ifc.ToolView;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowAnt;

public class AntViewManager implements ParadeView, ToolView {

	public String getParadeViewHeader() {
		String header = "<b>Ant buildfile</b>";
		return header;
	}
	
	public String getParadeView(Row r) {
		RowAnt antdata = (RowAnt) r.getRowdata().get("ant");
		
		String view = antdata.getBuildfile().getAbsolutePath();
		return view;
	}

	public String getToolView(Row r) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
