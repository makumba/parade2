package org.makumba.parade.access;

import java.util.Date;

import org.makumba.aether.UserTypes;
import org.makumba.parade.aether.ObjectTypes;
import org.makumba.parade.model.ActionLog;
import org.makumba.parade.tools.TriggerFilter;

/**
 * A simple Data Transfer Object of an {@link ActionLog}. We need this class because the {@link TriggerFilter} and all
 * other classes that are loaded tomcat-wide shouldn't know anything about Hibernate.
 * 
 * @author Manuel Gay
 * 
 */
public class ActionLogDTO {

    private Long id;

    private Date date;

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
    
    private UserTypes userType;

    public ObjectTypes getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectTypes objectType) {
        this.objectType = objectType;
    }

    public UserTypes getUserType() {
        return userType;
    }

    public void setUserType(UserTypes userType) {
        this.userType = userType;
    }

    public String getParadecontext() {
        return paradecontext;
    }

    public void setParadecontext(String paradecontext) {
        this.paradecontext = paradecontext;
    }

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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    /**
     * Populates an ActionLog
     * 
     * @param log
     *            the ActionLog to be populated by this DTO
     */
    public void populate(ActionLog log) {
        log.setId(id);
        log.setContext(context);
        log.setlogDate(date);
        log.setPost(post);
        log.setQueryString(queryString);
        log.setUrl(url);
        log.setUser(user);
        log.setAction(action);
        log.setOrigin(origin);
        log.setParadecontext(paradecontext);
        log.setFile(file);
        log.setUserType(userType);
        log.setObjectType(objectType);
    }

    public ActionLogDTO(ActionLog log) {
        this.id = log.getId();
        this.context = log.getContext();
        this.date = log.getlogDate();
        this.post = log.getPost();
        this.queryString = log.getQueryString();
        this.url = log.getUrl();
        this.user = log.getUser();
        this.action = log.getAction();
        this.origin = log.getOrigin();
        this.paradecontext = log.getParadecontext();
        this.file = log.getFile();
        this.userType = log.getUserType();
        this.objectType = log.getObjectType();
    }

    public ActionLogDTO() { // empty constructor for TriggerFilter

    }

}
