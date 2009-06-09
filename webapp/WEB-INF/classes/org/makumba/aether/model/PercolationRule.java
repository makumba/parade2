package org.makumba.aether.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Percolation rule for rule-based percolation.
 * 
 * @author Manuel Gay
 * 
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class PercolationRule implements AetherRule {

    private long id;

    private String subject;

    private String predicate;

    private String object;

    private int consumption;

    private int propagationDepthLimit;

    private String description;

    private boolean active = true;

    private List<RelationQuery> relationQueries;

    public PercolationRule() {

    }

    @Id @GeneratedValue
    @Column(name="percolationrule")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    @Index(name="IDX_SUBJECT")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Column
    @Index(name="IDX_PREDICATE")
    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    @Column
    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    @Column
    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    @Column
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
    @Index(name="IDX_ACTIVE")
    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @ManyToMany
    public List<RelationQuery> getRelationQueries() {
        return relationQueries;
    }

    public void setRelationQueries(List<RelationQuery> relationQueries) {
        this.relationQueries = relationQueries;
    }

    @Override
    public String toString() {
        return this.subject + " --(" + this.predicate + ")--> " + object + " (consumes " + consumption + ") - "
                + description;
    }

    public void setPropagationDepthLimit(int propagationDepthLimit) {
        this.propagationDepthLimit = propagationDepthLimit;
    }

    @Column
    public int getPropagationDepthLimit() {
        return propagationDepthLimit;
    }

}
