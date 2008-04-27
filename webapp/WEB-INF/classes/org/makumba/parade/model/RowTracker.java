package org.makumba.parade.model;

public class RowTracker extends AbstractRowData implements RowData {

    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
