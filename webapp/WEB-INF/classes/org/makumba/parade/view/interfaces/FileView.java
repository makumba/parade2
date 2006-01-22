package org.makumba.parade.view.interfaces;

import org.makumba.parade.model.File;
import org.makumba.parade.model.Row;

public interface FileView {

    public String getFileViewHeader(Row r, String path);

    public String getFileView(Row r, String path, File f);

}
