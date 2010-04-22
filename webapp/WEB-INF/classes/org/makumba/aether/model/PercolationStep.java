package org.makumba.aether.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Percolation step, containing focus and nimbus for each percolation step
 * 
 * @author Manuel Gay
 * 
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
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

    @Id @GeneratedValue
    @Column(name="percolationstep")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    @Column
    @Index(name="IDX_FOCUS")
    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    @Column
    @Index(name="IDX_NIMBUS")
    public int getNimbus() {
        return nimbus;
    }

    public void setNimbus(int nimbus) {
        this.nimbus = nimbus;
    }

    @ManyToOne
    @ForeignKey(name="FK_PREVIOUS")
    @OnDelete(action=OnDeleteAction.CASCADE)
    @JoinColumn(name="previous")
    public PercolationStep getPrevious() {
        return previous;
    }

    public void setPrevious(PercolationStep previous) {
        this.previous = previous;
    }

    @ManyToOne
    @ForeignKey(name="FK_MATCHEDAETHEREVENT")
    @OnDelete(action=OnDeleteAction.CASCADE)
    @JoinColumn(name="matchedAetherEvent")
    public MatchedAetherEvent getMatchedAetherEvent() {
        return matchedAetherEvent;
    }

    public void setMatchedAetherEvent(MatchedAetherEvent matchedAetherEvent) {
        this.matchedAetherEvent = matchedAetherEvent;
    }

    @ManyToOne
    @ForeignKey(name="FK_PERCOLATIONRULE")
    @OnDelete(action=OnDeleteAction.CASCADE)
    @JoinColumn(name="percolationRule")
    public PercolationRule getPercolationRule() {
        return percolationRule;
    }

    public void setPercolationRule(PercolationRule percolationRule) {
        this.percolationRule = percolationRule;
    }

    @Column(columnDefinition="longtext")
    public String getPercolationPath() {
        return percolationPath;
    }

    public void setPercolationPath(String percolationPath) {
        this.percolationPath = percolationPath;
    }

    @Column
    public int getPercolationLevel() {
        return percolationLevel;
    }

    public void setPercolationLevel(int percolationLevel) {
        this.percolationLevel = percolationLevel;
    }

    @ManyToOne
    @ForeignKey(name="FK_ROOT")
    @OnDelete(action=OnDeleteAction.CASCADE)
    @JoinColumn(name="root")
    public PercolationStep getRoot() {
        return root;
    }

    public void setRoot(PercolationStep root) {
        this.root = root;
    }

    @Column
    public String getPreviousURL() {
        return previousURL;
    }

    public void setPreviousURL(String previousURL) {
        this.previousURL = previousURL;
    }

    @Column
    @Index(name="IDX_OBJECTURL")
    public String getObjectURL() {
        return objectURL;
    }

    public void setObjectURL(String objectURL) {
        this.objectURL = objectURL;
    }

    @Column
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Column
    public int getInitialFocus() {
        return initialFocus;
    }

    public void setInitialFocus(int initialFocus) {
        this.initialFocus = initialFocus;
    }

    @Column
    public int getInitialNimbus() {
        return initialNimbus;
    }

    public void setInitialNimbus(int initialNimbus) {
        this.initialNimbus = initialNimbus;
    }

    @Column
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    

    public void setVirtualPercolation(boolean virtualPercolation) {
        this.virtualPercolation = virtualPercolation;
    }

    @Column
    public boolean getVirtualPercolation() {
        return virtualPercolation;
    }


    public PercolationStep(String previousURL, String objectURL, String userGroup, int focus, int nimbus,
            PercolationRule percolationRule, MatchedAetherEvent matchedAetherEvent, PercolationStep previous,
            int percolationLevel, boolean virtualPercolation) {
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
}
