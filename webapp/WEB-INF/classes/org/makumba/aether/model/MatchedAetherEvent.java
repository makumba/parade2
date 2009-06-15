package org.makumba.aether.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.makumba.aether.AetherEvent;

/**
 * Matched aether event that is being percolated
 * 
 * @author Manuel Gay
 * 
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
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

    @Id @GeneratedValue
    @Column(name="matchedaetherevent")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    @Column
    public String getUserGroup() {
        return userGroup;
    }

    @ManyToOne
    @ForeignKey(name="INITIALPERCOLATIONRULE")
    public InitialPercolationRule getInitialPercolationRule() {
        return initialPercolationRule;
    }

    public void setInitialPercolationRule(InitialPercolationRule initialPercolationRule) {
        this.initialPercolationRule = initialPercolationRule;
    }

    @Transient
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

    @Column
    public boolean getVirtualPercolation() {
        return virtualPercolation;
    }

}
