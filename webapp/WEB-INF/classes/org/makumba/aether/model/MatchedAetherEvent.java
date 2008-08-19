package org.makumba.aether.model;

import org.makumba.aether.AetherEvent;

/**
 * Matched aether event that is being percolated
 * 
 * TODO add progression curve
 * 
 * @author Manuel Gay
 * 
 */
public class MatchedAetherEvent extends AetherEvent {

    private long id;

    private String userGroup;

    private String userType;

    private InitialPercolationRule initialPercolationRule;

    private boolean virtualPercolation;

    public MatchedAetherEvent(AetherEvent e, String userGroup, InitialPercolationRule ipr, boolean virtualPercolation) {
        super(e);
        this.userType = ipr.getUserType();
        this.userGroup = userGroup;
        this.initialPercolationRule = ipr;
        this.virtualPercolation = virtualPercolation;
    }

    // for hibernate
    public MatchedAetherEvent() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public InitialPercolationRule getInitialPercolationRule() {
        return initialPercolationRule;
    }

    public void setInitialPercolationRule(InitialPercolationRule initialPercolationRule) {
        this.initialPercolationRule = initialPercolationRule;
    }

    public int getInitialPercolationLevel() {
        return initialPercolationRule.getInitialLevel();
    }

    @Override
    public String toString() {
        return this.actor + " (" + this.userType + ") --(" + this.action + ")--> " + this.objectURL + " ("
                + this.objectType + ") ===> " + initialPercolationRule.getInitialLevel() + " (coef. "
                + this.initialLevelCoefficient + ") on group " + this.userGroup;
    }

    public void setVirtualPercolation(boolean virtualPercolation) {
        this.virtualPercolation = virtualPercolation;
    }

    public boolean getVirtualPercolation() {
        return virtualPercolation;
    }

}
