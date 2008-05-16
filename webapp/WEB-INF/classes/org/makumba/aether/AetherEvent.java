package org.makumba.aether;

/**
 * A aether event that can be processed by the percolation engine
 * @author Manuel Gay
 *
 */
public class AetherEvent {
    
    protected String objectURL;
    protected String objectType;
    protected String user;
    protected String userType;
    protected String action;
    
    public String getObjectURL() {
        return objectURL;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getUser() {
        return user;
    }

    public String getUserType() {
        return userType;
    }

    public String getAction() {
        return action;
    }

    public AetherEvent(String objectURL, String objectType, String user, String userType, String action) {
        super();
        this.objectURL = objectURL;
        this.objectType = objectType;
        this.user = user;
        this.userType = userType;
        this.action = action;
    }
    
    // for MatchedAetherEvent
    protected AetherEvent(AetherEvent e) {
        this.objectURL = e.getObjectURL();
        this.objectType = e.getObjectType();
        this.user = e.getUser();
        this.userType = e.getUserType();
        this.action = e.getAction();
    }
    
    public String toString() {
        return this.user + " ("+this.userType+") --(" + this.action + ")--> " + this.objectURL + " ("+this.objectType + ")";
    }

}
