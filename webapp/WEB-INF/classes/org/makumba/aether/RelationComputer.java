package org.makumba.aether;

/**
 * A relation computer is capable of computing relations between objects, once at start and for single objects on
 * demand. It should be smart enough not to recompute all the relations if they already exist.
 * 
 * @author Manuel Gay
 * 
 */
public interface RelationComputer {

    public String getName();
    
    public void computeRelations() throws RelationComputationException;
    
    public void updateRelation(String objectURL);
    
}
