package org.makumba.parade.model;

public class RowMakumba extends AbstractRowData implements RowData {

    private Long id;

    private String version;

    private String db;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String database) {
        this.db = database;
    }

}
