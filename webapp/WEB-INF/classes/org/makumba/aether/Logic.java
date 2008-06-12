package org.makumba.aether;

import java.util.Dictionary;

import org.makumba.Attributes;
import org.makumba.Database;
import org.makumba.LogicException;
import org.makumba.Pointer;
import org.makumba.aether.percolation.RuleBasedPercolator;

public class Logic {

    public void on_newInitialPercolationRule(Dictionary d, Attributes a, Database db) throws LogicException {
    }

    public void after_newInitialPercolationRule(Pointer p, Dictionary d, Attributes a, Database db)
            throws LogicException {
        RuleBasedPercolator.rulesChanged = true;
    }

    public void on_editInitialPercolationRule(Pointer p, Dictionary d, Attributes a, Database db) throws LogicException {
        RuleBasedPercolator.rulesChanged = true;
    }

    public void on_deleteInitialPercolationRule(Pointer p, Attributes a, Database db) throws LogicException {
    }

    public void after_deleteInitialPercolationRule(Pointer p, Attributes a, Database db) throws LogicException {
        RuleBasedPercolator.rulesChanged = true;
    }

    public void on_newPercolationRule(Dictionary d, Attributes a, Database db) throws LogicException {
    }

    public void after_newPercolationRule(Pointer p, Dictionary d, Attributes a, Database db) throws LogicException {
        RuleBasedPercolator.rulesChanged = true;
    }

    public void on_editPercolationRule(Pointer p, Dictionary d, Attributes a, Database db) throws LogicException {
    }

    public void on_deletePercolationRule(Pointer p, Attributes a, Database db) throws LogicException {
    }

    public void after_deletePercolationRule(Pointer p, Attributes a, Database db) throws LogicException {
        RuleBasedPercolator.rulesChanged = true;
    }
    
    public void on_newRelationQuery(Dictionary d, Attributes a, Database db) throws LogicException {
    }

    public void after_newRelationQuery(Pointer p, Dictionary d, Attributes a, Database db)
            throws LogicException {
        RuleBasedPercolator.rulesChanged = true;
    }
    
    public void on_editRelationQuery(Pointer p, Dictionary d, Attributes a, Database db) throws LogicException {
        RuleBasedPercolator.rulesChanged = true;
    }
    
    public void on_deleteRelationQuery(Pointer p, Attributes a, Database db) throws LogicException {
    }

    public void after_deleteRelationQuery(Pointer p, Attributes a, Database db) throws LogicException {
        RuleBasedPercolator.rulesChanged = true;
    }

}
