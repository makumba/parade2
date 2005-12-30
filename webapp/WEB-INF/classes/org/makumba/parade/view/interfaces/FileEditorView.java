package org.makumba.parade.view.interfaces;

import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;

public interface FileEditorView {
	
	public String getFileEditorView(Row r, String path, File file, String[] source);

}
