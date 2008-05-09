package org.makumba.aether;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.SessionFactory;

/**
 * A context needed to configure the aether engine
 * 
 * @author Manuel Gay
 * 
 */
public class AetherContext {
    
    private String databaseName;
    
    private SessionFactory sessionFactory;

    private List<RelationComputer> relationComputers = new LinkedList<RelationComputer>();

    public AetherContext(String databaseName, SessionFactory sessionFactory) {
        super();
        this.sessionFactory = sessionFactory;
        this.databaseName = databaseName;
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

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
