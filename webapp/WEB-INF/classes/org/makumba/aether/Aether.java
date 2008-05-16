package org.makumba.aether;

import java.util.List;

import org.apache.log4j.Logger;
import org.makumba.aether.percolation.Percolator;
import org.makumba.aether.percolation.RuleBasedPercolator;

/**
 * The access point to the Aether engine
 * 
 * @author Manuel Gay
 * 
 */
public class Aether {

    private Logger logger = Logger.getLogger(Aether.class);

    private static Aether instance;

    public static Aether getAether(AetherContext ctx) {
        if (instance == null) {
            instance = new Aether(ctx);
        }
        return instance;
    }

    private AetherContext ctx;

    private Percolator p;

    private Aether(AetherContext ctx) {
        this.ctx = ctx;
        startup();
    }

    /**
     * Starts the Aether engine up:
     * <ul>
     * <li>computes all relations using the relation computers</li>
     * <li>initialises the percolator</li>
     * </ul>
     */
    private void startup() {
        logger.info("AETHER-INIT: Starting Aether engine at " + new java.util.Date());
        long start = System.currentTimeMillis();

        p = new RuleBasedPercolator();
        p.configure(ctx.getDatabaseName(), ctx.getSessionFactory());
        computeAllRelations();

        logger.info("AETHER-INIT: Launching Aether finished at " + new java.util.Date());
        long end = System.currentTimeMillis();
        long refresh = end - start;
        logger.info("AETHER-INIT: Initialisation took " + refresh + " ms");

    }

    /**
     * Computes all the relations using the relation computers
     */
    private void computeAllRelations() {
        List<RelationComputer> computers = ctx.getRelationComputers();
        for (RelationComputer relationComputer : computers) {
            try {
                relationComputer.computeRelations();
            } catch (RelationComputationException e) {
                logger.error("Could not compute relations of relation computer " + relationComputer.getName() + ": "
                        + e.getMessage());
            }
        }
    }

    public void registerEvent(AetherEvent e) {
        logger.debug("Registering new Aether event \"" + e.toString()+"\"");
        try {
            p.percolate(e);
        } catch (PercolationException e1) {
            logger.warn("Exception while percolating event \""+e.toString()+"\": "+e1.getMessage());
        }

    }
}
