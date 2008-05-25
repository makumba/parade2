package org.makumba.aether.model;

import java.util.List;

/**
 * Initial percolation rule for rule-based percolation
 * 
 * TODO introduce curve
 * 
 * @author Manuel Gay
 * 
 */
public class InitialPercolationRule implements AetherRule {
    
    public static final int NO_PERCOLATION = 0;

    public static final int FOCUS_PERCOLATION = 10;

    public static final int NIMBUS_PERCOLATION = 20;

    public static final int FOCUS_NIMBUS_PERCOLATION = 30;

    private long id;

    private String objectType;

    private String action;

    private String userType;

    private int percolationMode;

    private int initialLevel;

    private List<RelationQuery> relationQueries;

    private boolean active = true;

    public InitialPercolationRule() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getInitialLevel() {
        return initialLevel;
    }

    public void setInitialLevel(int initialLevel) {
        this.initialLevel = initialLevel;
    }

    public List<RelationQuery> getRelationQueries() {
        return relationQueries;
    }

    public void setRelationQueries(List<RelationQuery> relationQuery) {
        this.relationQueries = relationQuery;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getPercolationMode() {
        return percolationMode;
    }

    public void setPercolationMode(int percolationMode) {
        this.percolationMode = percolationMode;
    }

    public String toString() {
        return "oType: " + objectType + " action: " + action + " uType: " + userType + " initLevel: " + initialLevel
                + " percolationMode: " + percolationMode(this.percolationMode);
    }

    private static String percolationMode(int n) {
        switch (n) {
        case FOCUS_PERCOLATION:
            return "Focus";
        case NIMBUS_PERCOLATION:
            return "Nimbus";
        case FOCUS_NIMBUS_PERCOLATION:
            return "Focus/Nimbus";
        default:
            return "Should not be here";
        }
    }

}
