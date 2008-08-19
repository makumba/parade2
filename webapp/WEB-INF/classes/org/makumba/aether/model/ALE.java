package org.makumba.aether.model;

public class ALE {

    private long id;

    private String objectURL;

    private String user;

    private int focus;

    private int nimbus;

    private int virtualFocus;

    public int getVirtualFocus() {
        return virtualFocus;
    }

    public void setVirtualFocus(int virtualFocus) {
        this.virtualFocus = virtualFocus;
    }

    public int getVirtualNimbus() {
        return virtualNimbus;
    }

    public void setVirtualNimbus(int virtualNimbus) {
        this.virtualNimbus = virtualNimbus;
    }

    private int virtualNimbus;

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

    public ALE(String objectURL, String user) {
        this.objectURL = objectURL;
        this.user = user;
    }

    public ALE(String objectURL, String user, int focus, int nimbus) {
        super();
        this.objectURL = objectURL;
        this.user = user;
        this.focus = focus;
        this.nimbus = nimbus;
    }

    public ALE() {

    }

}
