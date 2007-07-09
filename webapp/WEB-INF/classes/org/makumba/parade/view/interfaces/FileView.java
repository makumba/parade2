package org.makumba.parade.view.interfaces;

import org.makumba.parade.model.File;
import org.makumba.parade.model.Row;

import freemarker.template.SimpleHash;

public interface FileView {

    public void setFileView(SimpleHash fileView, Row r, String path, File f);

}
