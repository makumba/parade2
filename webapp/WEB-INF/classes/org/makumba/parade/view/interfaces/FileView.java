package org.makumba.parade.view.interfaces;

import org.makumba.parade.model.File;
import org.makumba.parade.model.Row;

public interface FileView {
	
	public String getFileViewHeader();
	public String getFileView(Row r, File f);

}
