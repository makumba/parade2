package org.makumba.parade.access;

import java.util.Date;

import org.makumba.parade.model.ActionLog;

public class ActionLogDTO {
    
    private Long id;

    private Date date;
    
    private String user;
    
    private String context;
    
    private String url;
    
    private String queryString;
    
    private String post;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void populate(ActionLog log) {
        log.setId(id);
        log.setContext(context);
        log.setDate(date);
        log.setPost(post);
        log.setQueryString(queryString);
        log.setUrl(url);
        log.setUser(user);
    }
    
}
