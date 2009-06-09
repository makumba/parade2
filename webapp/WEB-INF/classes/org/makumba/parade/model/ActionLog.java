package org.makumba.parade.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.makumba.parade.access.ActionLogDTO;
import org.makumba.parade.aether.ObjectTypes;

@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ActionLog {

    private Long id;

    private Date logDate;

    private String user;

    private String context;

    private String url;

    private String queryString;

    private String post;

    private String action;

    private String origin;

    private String paradecontext;

    private String file;

    private ObjectTypes objectType;

    @Column
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Column
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Column
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Column
    public Date getlogDate() {
        return logDate;
    }

    public void setlogDate(Date date) {
        this.logDate = date;
    }

    @Column
    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Column
    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    @Column
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Id @GeneratedValue
    @Column(name="actionlog")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    public String getParadecontext() {
        return paradecontext;
    }

    public void setParadecontext(String paradecontext) {
        this.paradecontext = paradecontext;
    }

    @Column
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Column
    @Type(type="objectType")
    public ObjectTypes getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectTypes objectType) {
        this.objectType = objectType;
    }
    
    /**
     * Populates this ActionLog with data from an ActionLogDTO
     */
    public void populateFrom(ActionLogDTO dto) {
        this.id = dto.getId();
        this.context = dto.getContext();
        this.action = dto.getAction();
        this.file = dto.getFile();
        this.logDate = dto.getDate();
        this.objectType = dto.getObjectType();
        this.origin = dto.getOrigin();
        this.paradecontext = dto.getParadecontext();
        this.post = dto.getPost();
        this.queryString = dto.getQueryString();
        this.url = dto.getUrl();
        this.user = dto.getUser();
    }
    
    
}
