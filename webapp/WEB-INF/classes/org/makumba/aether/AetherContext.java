package org.makumba.aether;

import java.util.LinkedList;
import java.util.List;

/**
 * A context needed to configure the aether engine
 * 
 * @author Manuel Gay
 * 
 */
public class AetherContext {
    
    private Enum actionTypes;
    
    private Enum objectTypes;
    
    private List<RelationComputer> relationComputers = new LinkedList<RelationComputer>();

    public AetherContext(Enum actionTypes, Enum objectTypes) {
        super();
        this.actionTypes = actionTypes;
        this.objectTypes = objectTypes;
    }

    public Enum getActionTypes() {
        return actionTypes;
    }

    public void setActionTypes(Enum actionTypes) {
        this.actionTypes = actionTypes;
    }

    public Enum getObjectTypes() {
        return objectTypes;
    }

    public void setObjectTypes(Enum objectTypes) {
        this.objectTypes = objectTypes;
    }

    public List<RelationComputer> getRelationComputers() {
        return relationComputers;
    }

    public void setRelationComputers(List<RelationComputer> relationComputers) {
        this.relationComputers = relationComputers;
    }
    
    public void addRelationComputer(RelationComputer rc) {
        this.relationComputers.add(rc);
    }
    

}
