package org.makumba.parade.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class UploadForm extends ActionForm {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private FormFile file;

    /**
     * @return the file.
     */
    public FormFile getFile() {
        return file;
    }

    /**
     * @param file
     *            The FormFile to set.
     */
    public void setFile(FormFile file) {
        this.file = file;
    }

    /**
     * 
     * @return the file's name.
     */
    public String getFileName() {
        return file.getFileName();
    }

    /**
     * 
     * @return the file's size.
     */
    public Integer getFileSize() {
        return file.getFileSize();
    }

    /**
     * 
     * @return the file's content type.
     */
    public String getFileContentType() {
        return file.getContentType();
    }

    /**
     * 
     * @return the file's data
     */
    public byte[] getFileData() {
        try {
            return file.getFileData();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
