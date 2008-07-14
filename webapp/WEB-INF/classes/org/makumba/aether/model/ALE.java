package org.makumba.aether.model;

public class ALE {
    
    private long id;
    
    private String objectURL;
    
    private String user;
    
    private int focus;
    
    private int nimbus;

    public int getNimbus() {
        return nimbus;
    }

    public void setNimbus(int nimbus) {
        this.nimbus = nimbus;
    }

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

    public ALE(String objectURL, String user, int focus, int nimbus) {
        super();
        this.objectURL = objectURL;
        this.user = user;
        this.focus = focus;
        this.nimbus = nimbus;
    }

}
