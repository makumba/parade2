package org.makumba.parade.model;

import java.util.Map;

public class Application {

    private long id;
    
    private String name;
    
    private String description;
    
    private String repository;
    
    private String module;
    
    private Map<String, String> cvsfiles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public Map<String, String> getCvsfiles() {
        return cvsfiles;
    }

    public void setCvsfiles(Map<String, String> cvsfiles) {
        this.cvsfiles = cvsfiles;
    }
    
    public Application() {
        
    }
    
    public Application(String name, String repository, String description, String module) {
        this.name = name;
        this.repository = repository;
        this.description = description;
        this.module = module;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

}
