package org.makumba.parade.ifc;

import org.makumba.parade.model.File;
import org.makumba.parade.model.Row;

public interface FileBrowserView {
	
	public String getFileViewHeader();
	public String getFileView(Row r, File f);

}
