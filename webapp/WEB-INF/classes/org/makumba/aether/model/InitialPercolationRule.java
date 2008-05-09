package org.makumba.aether.model;

/**
 * Initial percolation rule for rule-based percolation
 * 
 * TODO introduce curve
 * 
 * @author Manuel Gay
 * 
 */
public class InitialPercolationRule implements AetherRule {

    public final static int USER_ALL = 10;

    public final static int USER_OWNER = 20;

    public final static int USER_ALLBUTOWNER = 30;

    private long id;

    private String objectType;

    private String action;

    private int userType;

    private int initialLevel;

    private String userGroup;
    
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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getInitialLevel() {
        return initialLevel;
    }

    public void setInitialLevel(int initialLevel) {
        this.initialLevel = initialLevel;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }
    
    public String toString() {
        return "oType: "+objectType + " action: "+action + " uType: "+userType + " initLevel: "+initialLevel + " userGroup "+userGroup;
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
