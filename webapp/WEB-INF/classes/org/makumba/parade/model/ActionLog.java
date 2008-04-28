package org.makumba.parade.model;

import java.util.Date;

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Date getlogDate() {
        return logDate;
    }

    public void setlogDate(Date date) {
        this.logDate = date;
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

    public String getParadecontext() {
        return paradecontext;
    }

    public void setParadecontext(String paradecontext) {
        this.paradecontext = paradecontext;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}
