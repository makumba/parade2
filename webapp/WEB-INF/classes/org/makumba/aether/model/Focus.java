package org.makumba.aether.model;

public class Focus {
    
    private long id;
    
    private String objectURL;
    
    private String user;
    
    private int focus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getObjectURL() {
        return objectURL;
    }

    public void setObjectURL(String objectURL) {
        this.objectURL = objectURL;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Focus(String objectURL, String user, int focus) {
        super();
        this.objectURL = objectURL;
        this.user = user;
        this.focus = focus;
    }

}
