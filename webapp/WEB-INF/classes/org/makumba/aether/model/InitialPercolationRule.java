package org.makumba.aether.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

/**
 * Initial percolation rule for rule-based percolation
 * 
 * @author Manuel Gay
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InitialPercolationRule implements AetherRule {

    public static final int NO_PERCOLATION = 0;

    public static final int FOCUS_PERCOLATION = 10;

    public static final int NIMBUS_PERCOLATION = 20;

    public static final int FOCUS_NIMBUS_PERCOLATION = 30;

    public static final int IMMEDIATE_INTERACTION = 10;

    public static final int DIFFERED_INTERACTION = 20;

    private long id;

    private String objectType;

    private String action;

    private String userType;

    private int percolationMode;

    private int initialLevel;

    private String focusProgressionCurve;

    private String nimbusProgressionCurve;

    private List<RelationQuery> relationQueries;

    private int interactionType;

    private boolean active = true;

    private String description;

    public InitialPercolationRule() {

    }

    @Id
    @GeneratedValue
    @Column(name = "initialpercolationrule")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    @Column
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Column
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Column
    public int getInitialLevel() {
        return initialLevel;
    }

    public void setInitialLevel(int initialLevel) {
        this.initialLevel = initialLevel;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "InitialPercolationRule__relationQueries")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Column(name = "initialPercolationRule")
    public List<RelationQuery> getRelationQueries() {
        return relationQueries;
    }

    public void setRelationQueries(List<RelationQuery> relationQuery) {
        this.relationQueries = relationQuery;
    }

    @Column
    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Column(columnDefinition = "int(11) default '0'")
    public int getPercolationMode() {
        return percolationMode;
    }

    public void setPercolationMode(int percolationMode) {
        this.percolationMode = percolationMode;
    }

    @Column
    public String getFocusProgressionCurve() {
        return focusProgressionCurve;
    }

    public void setFocusProgressionCurve(String focusProgressionCurve) {
        this.focusProgressionCurve = focusProgressionCurve;
    }

    @Column
    public String getNimbusProgressionCurve() {
        return nimbusProgressionCurve;
    }

    public void setNimbusProgressionCurve(String nimbusProgressionCurve) {
        this.nimbusProgressionCurve = nimbusProgressionCurve;
    }

    @Column
    public int getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(int interactionType) {
        this.interactionType = interactionType;
    }

    @Override
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

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
    public String getDescription() {
        return description;
    }

}
