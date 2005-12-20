package org.makumba.parade.view;

import org.makumba.parade.ifc.ParadeView;
import org.makumba.parade.model.Row;

public class RowStoreViewManager implements ParadeView {

	public String getParadeView(Row r) {
		String view = r.getRowname() + "\n" + r.getRowpath() +"</td><td>" + r.getDescription();
		return view;
	}

}
