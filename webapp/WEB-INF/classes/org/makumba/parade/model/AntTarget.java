package org.makumba.parade.model;

public class AntTarget {
    
    private Long id;
    
    private String target;

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
    
    public AntTarget(String target) {
        this.target = target;
    }

    public AntTarget() {
        
    }
}
