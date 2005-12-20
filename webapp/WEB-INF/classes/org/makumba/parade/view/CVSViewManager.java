package org.makumba.parade.view;

import org.makumba.parade.ifc.FileBrowserView;
import org.makumba.parade.ifc.ParadeView;
import org.makumba.parade.model.File;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowCVS;

public class CVSViewManager implements ParadeView, FileBrowserView {

	public String getParadeView(Row r) {
		RowCVS cvsdata = (RowCVS) r.getRowdata().get("cvs");
		
		String view = cvsdata.getUser() + ",<b>" + cvsdata.getModule()+ "</b>,"+ cvsdata.getBranch();
		return view;
	}

	public String getFileView(Row r, File f) {
		// TODO Auto-generated method stub
		return null;
	}

}
