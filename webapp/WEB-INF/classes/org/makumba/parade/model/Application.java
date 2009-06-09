package org.makumba.parade.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.MapKey;

@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Application {

    private long id;

    private String name;

    private String repository;

    private String webappPath;

    private Map<String, String> cvsfiles;

    private Parade parade;

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    @CollectionOfElements
    @MapKey(columns={@Column(name="filename")})
    @Column(name="fileversion")
    public Map<String, String> getCvsfiles() {
        return cvsfiles;
    }

    public void setCvsfiles(Map<String, String> cvsfiles) {
        this.cvsfiles = cvsfiles;
    }

    public Application() {

    }

    public Application(String name, String repository, String webappPath) {
        this.name = name;
        this.repository = repository;
        this.webappPath = webappPath;
    }

    @Id @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="id_parade")
    public Parade getParade() {
        return parade;
    }

    public void setParade(Parade parade) {
        this.parade = parade;
    }

    @Column
    public String getWebappPath() {
        return webappPath;
    }

    public void setWebappPath(String webappPath) {
        this.webappPath = webappPath;
    }

}
