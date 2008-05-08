package org.makumba.aether.model;

/**
 * Percolation rule for rule-based percolation.
 * 
 * @author Manuel Gay
 * 
 */
public class PercolationRule {

    private long id;

    private String subject;

    private String predicate;

    private String object;

    private int consumption;

    private String description;

    private String relationQuery;

    public PercolationRule() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelationQuery() {
        return relationQuery;
    }

    public void setRelationQuery(String relationQuery) {
        this.relationQuery = relationQuery;
    }

}
