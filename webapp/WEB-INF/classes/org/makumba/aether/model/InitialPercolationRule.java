package org.makumba.aether.model;

import java.util.List;
import java.util.Set;

/**
 * Initial percolation rule for rule-based percolation
 * 
 * TODO introduce curve
 * 
 * @author Manuel Gay
 * 
 */
public class InitialPercolationRule implements AetherRule {

    private long id;

    private String objectType;

    private String action;

    private String userType;

    private int initialLevel;
    
    private List<RelationQuery> relationQueries;

    
    private transient boolean active = true;

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
    
    public String toString() {
        return "oType: "+objectType + " action: "+action + " uType: "+userType + " initLevel: "+initialLevel;
    }

    /* (non-Javadoc)
     * @see org.makumba.aether.model.AetherRule#isActive()
     */
    public boolean isActive() {
        return active;
    }

    /* (non-Javadoc)
     * @see org.makumba.aether.model.AetherRule#setActive(boolean)
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}
