package org.makumba.aether.model;

public class RelationQuery {
    
    private long id;
    
    private String query;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    
    public RelationQuery() {
        
    }
    
    public String toString() {
        return query;
    }

}
