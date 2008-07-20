package org.makumba.aether.model;

import java.util.Date;

/**
 * Percolation step, containing focus and nimbus for each percolation step
 * 
 * @author Manuel Gay
 * 
 */
public class PercolationStep {

    private long id;
    
    private Date created;
    
    private Date lastModified;
    
    private String previousURL;

    private String objectURL;

    private String userGroup;
    
    private int initialFocus;
    
    private int initialNimbus;

    private int focus;

    private int nimbus;

    private PercolationRule percolationRule;
    
    private MatchedAetherEvent matchedAetherEvent;

    private PercolationStep previous;
    
    private PercolationStep root;
    
    private String percolationPath;
    
    private int percolationLevel;
    
    private boolean virtualPercolation;

    public PercolationStep() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public PercolationStep getPrevious() {
        return previous;
    }

    public void setPrevious(PercolationStep previous) {
        this.previous = previous;
    }

    public MatchedAetherEvent getMatchedAetherEvent() {
        return matchedAetherEvent;
    }

    public void setMatchedAetherEvent(MatchedAetherEvent matchedAetherEvent) {
        this.matchedAetherEvent = matchedAetherEvent;
    }

    public PercolationRule getPercolationRule() {
        return percolationRule;
    }

    public void setPercolationRule(PercolationRule percolationRule) {
        this.percolationRule = percolationRule;
    }

    public String getPercolationPath() {
        return percolationPath;
    }

    public void setPercolationPath(String percolationPath) {
        this.percolationPath = percolationPath;
    }

    public int getPercolationLevel() {
        return percolationLevel;
    }

    public void setPercolationLevel(int percolationLevel) {
        this.percolationLevel = percolationLevel;
    }

    public PercolationStep getRoot() {
        return root;
    }

    public void setRoot(PercolationStep root) {
        this.root = root;
    }

    public String getPreviousURL() {
        return previousURL;
    }

    public void setPreviousURL(String previousURL) {
        this.previousURL = previousURL;
    }

    public String getObjectURL() {
        return objectURL;
    }

    public void setObjectURL(String objectURL) {
        this.objectURL = objectURL;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public int getInitialFocus() {
        return initialFocus;
    }

    public void setInitialFocus(int initialFocus) {
        this.initialFocus = initialFocus;
    }

    public int getInitialNimbus() {
        return initialNimbus;
    }

    public void setInitialNimbus(int initialNimbus) {
        this.initialNimbus = initialNimbus;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public PercolationStep(String previousURL, String objectURL, String userGroup, int focus, int nimbus, PercolationRule percolationRule,
            MatchedAetherEvent matchedAetherEvent, PercolationStep previous, int percolationLevel, boolean virtualPercolation) {
        super();
        this.previousURL = previousURL;
        this.objectURL = objectURL;
        this.userGroup = userGroup;
        this.initialFocus = focus;
        this.initialNimbus = nimbus;
        this.focus = focus;
        this.nimbus = nimbus;
        this.percolationRule = percolationRule;
        this.matchedAetherEvent = matchedAetherEvent;
        this.previous = previous;
        this.percolationLevel = percolationLevel;
        this.created = new Date();
        this.lastModified = new Date();
        this.setVirtualPercolation(virtualPercolation);
    }

    public void setVirtualPercolation(boolean virtualPercolation) {
        this.virtualPercolation = virtualPercolation;
    }

    public boolean getVirtualPercolation() {
        return virtualPercolation;
    }
}
