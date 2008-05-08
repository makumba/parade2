package org.makumba.aether.model;

/**
 * Percolation step, containing focus and nimbus for each percolation step
 * 
 * @author Manuel Gay
 * 
 */
public class PercolationStep {

    private long id;

    private long object;

    private String userGroup;

    private int focus;

    private int nimbus;

    private long percolationId;

    private PercolationStep previous;

    public PercolationStep() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getObject() {
        return object;
    }

    public void setObject(long object) {
        this.object = object;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public int getNimbus() {
        return nimbus;
    }

    public void setNimbus(int nimbus) {
        this.nimbus = nimbus;
    }

    public long getPercolationId() {
        return percolationId;
    }

    public void setPercolationId(long percolationId) {
        this.percolationId = percolationId;
    }

    public PercolationStep getPrevious() {
        return previous;
    }

    public void setPrevious(PercolationStep previous) {
        this.previous = previous;
    }

}
