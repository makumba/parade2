package org.makumba.aether.percolation;

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
    
    private int initialPercolationLevel;
    private String userGroup;
    

    public MatchedAetherEvent(String objectURL, String objectType, String user, String userType, String action, int initialPercolationLevel, String userGroup) {
        super(objectURL, objectType, user, userType, action);
        this.initialPercolationLevel = initialPercolationLevel;
        this.userGroup = userGroup;
    }
    
    public MatchedAetherEvent(AetherEvent e, int initialPercolationLevel, String userGroup) {
        super(e);
        this.initialPercolationLevel = initialPercolationLevel;
        this.userGroup = userGroup;
    }


    public int getInitialPercolationLevel() {
        return initialPercolationLevel;
    }


    public String getUserGroup() {
        return userGroup;
    }

}
