package org.makumba.parade.model;

import java.util.Map;

public class Application {

    private long id;

    private String name;

    private String repository;

    private Map<String, String> cvsfiles;
    
    private Parade parade;

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

    public Application(String name, String repository) {
        this.name = name;
        this.repository = repository;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Parade getParade() {
        return parade;
    }

    public void setParade(Parade parade) {
        this.parade = parade;
    }

}
