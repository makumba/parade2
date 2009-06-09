package org.makumba.aether.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ALE {

    private long id;

    private String objectURL;

    private String user;

    private int focus;

    private int nimbus;

    private int virtualFocus;

    @Column(columnDefinition="int(11) default '0'")
    public int getVirtualFocus() {
        return virtualFocus;
    }

    public void setVirtualFocus(int virtualFocus) {
        this.virtualFocus = virtualFocus;
    }

    @Column(columnDefinition="int(11) default '0'")
    public int getVirtualNimbus() {
        return virtualNimbus;
    }

    public void setVirtualNimbus(int virtualNimbus) {
        this.virtualNimbus = virtualNimbus;
    }

    private int virtualNimbus;

    @Column(columnDefinition="int(11) default '0'")
    @Index(name="IDX_NIMBUS")
    public int getNimbus() {
        return nimbus;
    }

    public void setNimbus(int nimbus) {
        this.nimbus = nimbus;
    }

    @Id @GeneratedValue
    @Column(name="ale")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    @Index(name="IDX_OBJECTURL")
    public String getObjectURL() {
        return objectURL;
    }

    public void setObjectURL(String objectURL) {
        this.objectURL = objectURL;
    }

    @Column(columnDefinition="int(11) default '0'")
    @Index(name="IDX_FOCUS")
    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    @Column
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
