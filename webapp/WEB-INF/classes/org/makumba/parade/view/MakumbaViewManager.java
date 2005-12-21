package org.makumba.parade.view;

import org.makumba.parade.ifc.ParadeView;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowMakumba;

public class MakumbaViewManager implements ParadeView {

	public String getParadeViewHeader() {
		String header = "<b>Makumba version</b>";
		return header;
	}

	public String getParadeView(Row r) {
		RowMakumba makdata = (RowMakumba) r.getRowdata().get("makumba");
		
		String view = makdata.getVersion();
		return view;
	}

}
