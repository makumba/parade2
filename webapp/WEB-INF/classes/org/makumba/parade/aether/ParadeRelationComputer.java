package org.makumba.parade.aether;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.makumba.Pointer;
import org.makumba.Transaction;
import org.makumba.aether.RelationComputationException;
import org.makumba.aether.RelationComputer;
import org.makumba.db.hibernate.HibernateTransactionProvider;
import org.makumba.parade.init.ParadeProperties;
import org.makumba.providers.TransactionProvider;

/**
 * This relation computer populates the relation table with ownership relations between users and rows
 * 
 * TODO once this will be available in parade, also add the usage relations "uses" for trainees setting their mentors
 * row or so.
 * 
 * @author Manuel Gay
 * 
 */
public class ParadeRelationComputer implements RelationComputer {

    public static final String PARADE_RELATION_TABLE = "org.makumba.devel.relations.Relation";
    
    public static final String PARADE_DATABASE_NAME = ParadeProperties.getParadeProperty("aether.databaseName");

    private Logger logger = Logger.getLogger(ParadeRelationComputer.class);
    
    private TransactionProvider tp;

    public void computeRelations() throws RelationComputationException {
        
        logger.debug("Computing all relations of "+getName());

        Transaction t = tp.getConnectionTo(PARADE_DATABASE_NAME);

        // row-user relations
        // we populate the relations table with those relations
        
        // first we remove all the previous relations
        Vector<Dictionary<String, Object>> old = t.executeQuery("SELECT rel.id AS rel FROM org.makumba.devel.relations.Relation rel WHERE rel.type = 'owns'", null);
        for (Dictionary<String, Object> dictionary : old) {
            t.delete((Pointer)dictionary.get("rel"));
        }
        
        Vector<Dictionary<String, Object>> res = t.executeQuery(
                "SELECT r.rowname AS row, r.user.login AS user FROM Row r WHERE r.user != null", null);
        logger.info("Found "+res.size() + " user to row relations");
        for (Dictionary<String, Object> d : res) {
            Dictionary<String, Object> rel = buildUserRowRelation(d);
            t.insert(PARADE_RELATION_TABLE, rel);
        }

        t.close();
        
        logger.debug("Finished computing all relations of "+getName());

    }

    private Dictionary<String, Object> buildUserRowRelation(Dictionary<String, Object> d) {
        Dictionary<String, Object> rel = new Hashtable<String, Object>();
        rel.put("fromURL", "user://" + d.get("user"));
        rel.put("toURL", "row://" + d.get("row"));
        rel.put("type", "owns");
        return rel;
    }

    public String getName() {
        return "ParaDe relation computer";
    }

    public void updateRelation(String objectURL) throws RelationComputationException {
        
        logger.debug("Updating relation of object "+objectURL);

        if (objectURL.startsWith("user://")) {
            String userLogin = objectURL.substring("user://".length());

            Transaction t = tp.getConnectionTo(PARADE_DATABASE_NAME);

            Vector<Dictionary<String, Object>> res = t
                    .executeQuery(
                            "SELECT rel.id AS rel FROM org.makumba.devel.relations.Relation rel WHERE rel.fromURL = $1 AND rel.type = 'owns'",
                            new Object[] { objectURL });

            for (Dictionary<String, Object> dictionary : res) {
                Pointer relPtr = (Pointer) dictionary.get("rel");
                
                logger.debug("Deleting old relation "+relPtr.getUid());
                
                // delete old record
                t.delete(relPtr);

                // generate new record
                // we look for all the rows owned by the user to update
                Vector<Dictionary<String, Object>> res1 = t.executeQuery(
                        "SELECT r.rowname AS row, r.user.login AS user FROM Row r WHERE r.user.login = $1",
                        new Object[] { userLogin });
                for (Dictionary<String, Object> d : res1) {
                    Dictionary<String, Object> rel = buildUserRowRelation(d);
                    logger.debug("Generating new relation "+rel.get("fromURL")+" --("+rel.get("type")+")--> "+rel.get("toURL"));
                    t.insert(PARADE_RELATION_TABLE, rel);
                }
            }
        }
    }

    public ParadeRelationComputer() {
        tp = new TransactionProvider(new HibernateTransactionProvider());
    }

    public void deleteRelation(String objectURL) throws RelationComputationException {
        // TODO Auto-generated method stub
        
    }

}
