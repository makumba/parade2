package org.makumba.aether.percolation;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.makumba.aether.AetherEvent;
import org.makumba.aether.PercolationException;
import org.makumba.aether.UserTypes;
import org.makumba.aether.model.InitialPercolationRule;
import org.makumba.aether.model.MatchedAetherEvent;
import org.makumba.aether.model.PercolationRule;
import org.makumba.parade.aether.ActionTypes;
import org.makumba.parade.aether.ObjectTypes;

/**
 * A rule-based percolator.<br>
 * This percolator uses 4 tables in order to perform percolation:
 * <ul>
 * <li>initial percolation rule table: set of rules to match an event and attribute an initial strength to it</li>
 * <li>percolation rule table: set of rules for that processes relations a {@link MatchedAetherEvent} can propagate
 * through, i.e. calculates consumption</li>
 * <li>percolation step table: stores each step of one event percolation</li>
 * <li>relation table: table holding relations between objects</li>
 * </ul>
 * 
 * @author Manuel Gay
 * 
 */
public class RuleBasedPercolator implements Percolator {

    private Logger logger = Logger.getLogger(RuleBasedPercolator.class);

    private String databaseName;

    private SessionFactory sessionFactory;

    private PercolationStrategy strategy;

    public void percolate(AetherEvent e) throws PercolationException {
        strategy.percolate(e, sessionFactory);
    }

    private void checkInitialPercolationRules() {
        logger.info("Checking initial percolation rules...");

        Session s = null;
        Transaction tx = null;
        try {
            s = sessionFactory.openSession();
            tx = s.beginTransaction();

            List<InitialPercolationRule> rules = s.createQuery("from InitialPercolationRule r").list();
            logger.debug("Found " + rules.size() + " initial percolation rules");
            for (InitialPercolationRule r : rules) {
                logger.debug(r.toString());

                if (!ActionTypes.getActions().contains(r.getAction())) {
                    logger.warn("Initial percolation rule \"" + r.toString() + "\" has invalid action " + r.getAction()
                            + ". It will be ignored.");
                    r.setActive(false);
                }

                if (!UserTypes.getUserTypes().contains(r.getUserType())) {
                    logger.warn("Initial percolation rule \"" + r.toString() + "\" has invalid user type "
                            + r.getUserType() + ". It will be ignored.");
                    r.setActive(false);
                }

                if (!ObjectTypes.getObjectTypes().contains(r.getObjectType())) {
                    logger.warn("Initial percolation rule \"" + r.toString() + "\" has invalid object type "
                            + r.getObjectType() + ". It will be ignored.");
                    r.setActive(false);
                }

            }

            tx.commit();

        } finally {
            if (s != null) {
                s.close();
            }
        }

    }

    private void checkPercolationRules() {
        logger.info("Checking percolation rules...");

        Session s = null;
        Transaction tx = null;
        try {
            s = sessionFactory.openSession();
            tx = s.beginTransaction();

            List<PercolationRule> rules = s.createQuery("from PercolationRule r").list();
            logger.debug("Found " + rules.size() + " percolation rules");
            for (PercolationRule r : rules) {
                logger.debug(r.toString());

                if (!ObjectTypes.getObjectTypes().contains(r.getObject())) {
                    logger.warn("Initial percolation rule \"" + r.toString() + "\" has invalid object type "
                            + r.getObject() + ". It will be ignored.");
                    r.setActive(false);
                }

                if (!ObjectTypes.getObjectTypes().contains(r.getSubject())) {
                    logger.warn("Initial percolation rule \"" + r.toString() + "\" has invalid subject type "
                            + r.getObject() + ". It will be ignored.");
                    r.setActive(false);
                }

            }

            tx.commit();

        } finally {
            if (s != null) {
                s.close();
            }
        }

    }

    public void configure(String databaseName, SessionFactory sessionFactory) {
        this.databaseName = databaseName;
        this.sessionFactory = sessionFactory;
        this.strategy = new SimplePercolationStrategy();

        logger.info("Starting initialisation of Rule-based percolator");

        checkInitialPercolationRules();
        checkPercolationRules();

        logger.info("Finished initialisation of Rule-based percolator");
    }

    public int getALE(String objectURL, String user) {

        Session s = null;
        Transaction tx = null;
        try {
            s = sessionFactory.openSession();
            tx = s.beginTransaction();
            
            String query = "select sum(ps.focus), sum(ps.nimbus) from PercolationStep ps where ps.objectURL = :objectURL and ps.userGroup like '%*%' and ps.userGroup not like :minusUser";
            
            Query q = s.createQuery(query).setString("objectURL", objectURL).setString("minusUser", "%-"+user+"%");

            List<Object> list = q.list();
            
            for (Object object : list) {

                Object[] pair = (Object[])object;
                
                if(pair[0] == null || pair[1] == null) {
                    return 0;
                }
                
                Long nimbus = (Long)pair[0];
                Long focus = (Long)pair[1];
                
                if(nimbus != 0 && focus != 0) {
                    return new Long(nimbus + focus).intValue();
                } else {
                    return 0;
                }
                
            }
            
            tx.commit();

        } finally {
            if (s != null) {
                s.close();
            }
        }

        return 0;
    }

}
