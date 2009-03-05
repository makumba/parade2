package org.makumba.parade.model;

public class AntTarget {
    
    private Long id;
    
    private String target;
    
    private Row row;

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
    
    public AntTarget(String target, Row row) {
        this.target = target;
        this.row = row;
    }

    public AntTarget() {
        
    }
}
