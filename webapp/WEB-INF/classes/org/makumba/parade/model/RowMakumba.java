package org.makumba.parade.model;

public class RowMakumba extends AbstractRowData implements RowData {

    private Long id;

    private String version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
