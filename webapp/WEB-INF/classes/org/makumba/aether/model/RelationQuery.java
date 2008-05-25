package org.makumba.aether.model;

public class RelationQuery {
    
    private long id;
    
    private String query;
    
    private String description;
    
    private String arguments;
    
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

}
