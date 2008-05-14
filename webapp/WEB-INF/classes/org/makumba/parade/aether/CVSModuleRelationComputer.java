package org.makumba.parade.aether;

import org.makumba.aether.RelationComputationException;
import org.makumba.aether.RelationComputer;
import org.makumba.parade.model.Row;

/**
 * Computes relations between files of a Makumba-enabled CVS module
 * @author Manuel Gay
 *
 */
public class CVSModuleRelationComputer extends MakumbaContextRelationComputer implements RelationComputer {

    public CVSModuleRelationComputer(Row r) {
        super(r);
    }

    public void computeRelations() throws RelationComputationException {
        // TODO Auto-generated method stub

    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public void updateRelation(String objectURL) throws RelationComputationException {
        // TODO Auto-generated method stub

    }

}
