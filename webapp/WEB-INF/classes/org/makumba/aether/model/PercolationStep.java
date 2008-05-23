package org.makumba.aether.model;

/**
 * Percolation step, containing focus and nimbus for each percolation step
 * 
 * @author Manuel Gay
 * 
 */
public class PercolationStep {

    private long id;

    private String objectURL;

    private String userGroup;

    private int focus;

    private int nimbus;

    private PercolationRule percolationRule;
    
    private MatchedAetherEvent matchedAetherEvent;

    private PercolationStep previous;

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

    public PercolationStep(String object, String userGroup, int focus, int nimbus, PercolationRule percolationRule,
            MatchedAetherEvent matchedAetherEvent, PercolationStep previous) {
        super();
        this.objectURL = object;
        this.userGroup = userGroup;
        this.focus = focus;
        this.nimbus = nimbus;
        this.percolationRule = percolationRule;
        this.matchedAetherEvent = matchedAetherEvent;
        this.previous = previous;
    }

    public String getObjectURL() {
        return objectURL;
    }

    public void setObjectURL(String objectURL) {
        this.objectURL = objectURL;
    }

}
