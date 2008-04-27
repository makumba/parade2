package org.makumba.parade.controller;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class UploadForm extends ActionForm {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private FormFile theFile;

    /**
     * @return Returns the theFile.
     */
    public FormFile getTheFile() {
        return theFile;
    }

    /**
     * @param theFile
     *            The FormFile to set.
     */
    public void setTheFile(FormFile theFile) {
        this.theFile = theFile;
    }
}
