package org.makumba.parade.view.interfaces;

import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;

public interface FileEditorView {
    
    /*
     * Generates the view of the file editor
     * r - row object of the current row
     * path - path ??
     * file - the file to be displayed
     * source - 
     */

    public String getFileEditorView(Row r, String path, File file, String[] source);

}
