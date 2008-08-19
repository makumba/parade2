package org.makumba.aether;

/**
 * A relation computer is capable of computing relations between objects, once at start and for single objects on
 * demand. It should be smart enough not to recompute all the relations if they already exist.
 * 
 * @author Manuel Gay
 * 
 */
public interface RelationComputer {

    /**
     * Gets the name of this relation computer
     * 
     * @return a name that identifies this relation computer
     */
    public String getName();

    /**
     * Computes all the relations
     * 
     * @throws RelationComputationException
     *             when a computation exception occurs
     */
    public void computeRelations() throws RelationComputationException;

    /**
     * Updates the relations of an object
     * 
     * @param object
     *            the object of which to update the relations (path, URL)
     * @throws RelationComputationException
     *             when a computation exception occurs
     */
    public void updateRelation(String object) throws RelationComputationException;

    /**
     * Deletes the relations of an object
     * 
     * @param object
     *            the object of which to delete the relations (path, URL)
     * @throws RelationComputationException
     *             when a computation exception occurs
     */
    public void deleteRelation(String object) throws RelationComputationException;

}
