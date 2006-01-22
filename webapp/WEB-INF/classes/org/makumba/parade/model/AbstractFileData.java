package org.makumba.parade.model;

public class AbstractFileData {

    private Long id;

    private File file;

    private String dataType;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String managername) {
        this.dataType = managername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
