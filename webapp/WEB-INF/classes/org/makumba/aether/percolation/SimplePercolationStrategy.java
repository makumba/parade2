package org.makumba.aether.percolation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.makumba.aether.AetherEvent;
import org.makumba.aether.PercolationException;
import org.makumba.aether.model.InitialPercolationRule;
import org.makumba.aether.model.MatchedAetherEvent;
import org.makumba.aether.model.PercolationRule;
import org.makumba.aether.model.PercolationStep;
import org.makumba.aether.model.RelationQuery;
import org.makumba.parade.aether.ObjectTypes;

/**
 * Simple rule-based percolation strategy, capable of performing percolation inside of a virtual semantic network.<br>
 * The network does not exist when percolation starts, it is built according to the rules that match the event at hand,
 * which provide queries in order to determine the next relations to percolate through.
 * 
 * @author Manuel Gay
 * 
 */
public class SimplePercolationStrategy extends RuleBasedPercolationStrategy {

    private Logger logger = Logger.getLogger(SimplePercolationStrategy.class);
    
    /**
     * The entry point for the percolation of an {@link AetherEvent}, that matches the event against the initial
     * percolation rules
     */
    @Override
    public void percolate(AetherEvent e, boolean virtualPercolation, SessionFactory sessionFactory) throws PercolationException {
        
        try {

            List<MatchedAetherEvent> matchedEvents = new LinkedList<MatchedAetherEvent>();

            Session s = null;
            Transaction tx = null;
            try {
                s = sessionFactory.openSession();
                tx = s.beginTransaction();

                Query q = s
                        .createQuery("SELECT r from InitialPercolationRule r where r.objectType = :objectType and r.action = :action");
                q.setParameter("objectType", e.getObjectType());
                q.setParameter("action", e.getAction());

                List<InitialPercolationRule> iprs = q.list();
                for (InitialPercolationRule ipr : iprs) {
                    MatchedAetherEvent mae = buildMatchedAetherEvent(e, ipr, false, s);
                    s.save(mae);
                    matchedEvents.add(mae);
                }

                for (MatchedAetherEvent mae : matchedEvents) {
                    percolateMatchedEvent(mae, s);
                }

                tx.commit();

            } finally {
                if (s != null) {
                    s.close();
                }
            }

        } catch (Exception ex) {
            throw new PercolationException(ex);
        }
        


    }

    /**
     * Percolates a {@link MatchedAetherEvent}:
     * <ul>
     * <li>grab all the relations of the object provided by the {@link MatchedAetherEvent}</li>
     * <li>for each relation, match it against the percolation rules</li>
     * <li>write the percolation step, that includes F/N</li>
     * <li>if the energy is high enough, start again from 7</li>
     * </ul>
     * 
     * @param mae
     *            the {@link MatchedAetherEvent} that should be percolated
     * @param s
     *            a Hibernate {@link Session}
     */
    private void percolateMatchedEvent(MatchedAetherEvent mae, Session s) {
        logger.debug("Starting percolation of matched event \"" + mae.toString() + "\"");

        // first let's get the queries from the MatchedEvent
        List<RelationQuery> queries = mae.getInitialPercolationRule().getRelationQueries();

        List<Object[]> res = new LinkedList<Object[]>();

        for (RelationQuery query : queries) {
            Map<String, String> arguments = new HashMap<String, String>();
            arguments.put("fromURL", mae.getObjectURL());
            arguments.put("percolationPath", "");
            res.addAll(query.execute(arguments, "fromURL", s));
        }

        List<Object[]> initialRelations = res;

        if (initialRelations == null) {
            return;
        }

        logger.debug("Going to percolate along " + initialRelations.size() + " initial relations");

        int initialEnergy = new Long(Math.round(mae.getInitialPercolationLevel()
                * (0.5 + mae.getInitialLevelCoefficient()))).intValue();

        if (mae.getInitialLevelCoefficient() == 0.00)
            initialEnergy = 0;

        if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.FOCUS_PERCOLATION) {
            percolate(mae, s, initialRelations, initialEnergy, true, null, 0);
        } else if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.NIMBUS_PERCOLATION) {
            percolate(mae, s, initialRelations, initialEnergy, false, null, 0);
        } else if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.FOCUS_NIMBUS_PERCOLATION) {
            percolate(mae, s, initialRelations, initialEnergy, true, null, 0);
            percolate(mae, s, initialRelations, initialEnergy, false, null, 0);
        }

    }

    /**
     * The main percolation algorithm, taking care of both focus and nimbus percolation. In case of nimbus percolation,
     * leads to recursive calls until either no more energy is left, or the percolation process timed out
     * 
     * @param mae
     *            the {@link MatchedAetherEvent} to percolate
     * @param s
     *            a Hibernate {@link Session}
     * @param relations
     *            the relations (fromURL, type, toURL) to percolate through
     * @param energy
     *            the energy on the current branch
     * @param isFocusPercolation
     *            whether this is focus or nimbus percolation
     * @param previousStep
     *            the previous {@link PercolationStep}, needed to get the next relations (in order not to percolate
     *            through the same relations again)
     * @param threadLevel
     *            the thread level of the percolation, meaning how "deep" the percolation is
     */
    private void percolate(MatchedAetherEvent mae, Session s, List<Object[]> relations, int energy,
            boolean isFocusPercolation, PercolationStep previousStep, int threadLevel) {

        long start = System.currentTimeMillis();

        if (isFocusPercolation) {

            logger.debug("====== START FOCUS PERCOLATION ======");

            // we figure the percolation rules that may apply to this kind of event

            Query q = s
                    .createQuery("SELECT r from PercolationRule r where r.subject = :subjectType and r.predicate = :predicate and r.object = :objectType and r.active = true");
            q.setParameter("subjectType", ObjectTypes.USER.toString());
            q.setParameter("predicate", mae.getAction());
            q.setParameter("objectType", mae.getObjectType());

            // for each match
            for (PercolationRule pr : (List<PercolationRule>) q.list()) {
                logger.debug("\t == Processing percolation rule match of rule " + pr.toString());

                // record a new percolation step
                logger.debug("\t == Recording percolation step with level " + energy);

                PercolationStep ps = buildPercolationStep(mae, mae.getObjectURL(), mae.getObjectURL(), pr, energy,
                        isFocusPercolation, previousStep, threadLevel, false);
                s.save(ps);
                
                addOrUpdateFocus(mae.getObjectURL(), mae.getActor(), energy, false, s);
                
            }

            logger.debug("====== END FOCUS PERCOLATION ======");

        } else {

            if (energy < RuleBasedPercolator.MIN_ENERGY_LEVEL) {
                logger.debug("NIMBUS-PERCOLATION: Not percolating any further because energy too low: " + energy);
            } else {

                logger.debug("NIMBUS-PERCOLATION: Going to percolate along " + relations.size()
                        + " discovered relations");

                for (Object[] relation : relations) {
                    logger.debug("===== START NIMBUS PERCOLATION ====");
                    logger.debug("===== " + Arrays.toString(relation));

                    Query q = s
                            .createQuery("SELECT r from PercolationRule r where r.subject = :subjectType and r.predicate = :predicate and r.object = :objectType and r.active = true");
                    q.setParameter("subjectType", ObjectTypes.typeFromURL((String) relation[0]));
                    q.setParameter("predicate", relation[1]);
                    q.setParameter("objectType", ObjectTypes.typeFromURL((String) relation[2]));

                    // for each match
                    for (PercolationRule pr : (List<PercolationRule>) q.list()) {
                        logger.debug("\t == Processing percolation rule match of rule " + pr.toString());

                        // we remove the consumption of the previous relation
                        int relationEnergy = energy - pr.getConsumption();

                        // - record a new percolation step
                        logger.debug("\t == Recording percolation step with level " + relationEnergy);
                        PercolationStep ps = buildPercolationStep(mae, (String) relation[2], (String) relation[0], pr,
                                relationEnergy, isFocusPercolation, previousStep, threadLevel, false);
                        s.save(ps);

                        // now we can set the threadPath
                        // basically this goes like parent-threadPath + parentId + thisUID
                        ps.setPercolationPath(previousStep == null ? ps.getId() + "_" + ps.getObjectURL()
                                : previousStep.getPercolationPath() + "_" + ps.getId() + "_" + ps.getObjectURL());

                        // and we also set the threadRoot
                        ps.setRoot(previousStep == null ? ps : previousStep.getRoot());

                        if (relationEnergy < RuleBasedPercolator.MIN_ENERGY_LEVEL) {

                            logger.debug("NIMBUS-PERCOLATION: Percolation of event " + mae.toString()
                                    + " stopped after relation " + Arrays.toString(relation)
                                    + " due to insuffiscient energy");

                        } else {

                            long now = System.currentTimeMillis();
                            if (!(now - start > RuleBasedPercolator.MAX_PERCOLATION_TIME)) {
                                // - get the next relations (use a RelationQuery for this)
                                List<Object[]> nextRelations = getNextRelations(pr, ps, s);

                                // do it again, again, again
                                percolate(mae, s, nextRelations, relationEnergy, isFocusPercolation, ps,
                                        threadLevel + 1);
                            } else {
                                logger.debug("NIMBUS-PERCOLATION: Percolation of event " + mae.toString()
                                        + " stopped after relation " + Arrays.toString(relation) + " due to timeout");
                                break;
                            }
                        }
                    }
                    long now = System.currentTimeMillis();
                    if (!(now - start > RuleBasedPercolator.MAX_PERCOLATION_TIME)) {
                        continue;
                    } else {
                        logger.debug("\t Percolation of event " + mae.toString() + " stopped after relation "
                                + Arrays.toString(relation) + " due to timeout");
                        break;
                    }
                }
            }
        }
    }

    /**
     * Retrieves the next relations for nimbus percolation
     * @param pr
     *            the {@link PercolationRule} having the queries for the next relations
     * @param ps
     *            the previous {@link PercolationStep}
     * @param s
     *            a Hibernate {@link Session}
     * 
     * @return a List of relations, of the kind fromURL, relationType, toURL
     */
    private List<Object[]> getNextRelations(PercolationRule pr, PercolationStep ps, Session s) {

        List<Object[]> res = new LinkedList<Object[]>();

        if (pr.getRelationQueries().size() > 0) {

            Map<String, String> arguments = new HashMap<String, String>();
            arguments.put("fromURL", ps.getObjectURL());
            arguments.put("cvsURL", GroupedPercolationStrategy.fileToCVSURL(ps.getObjectURL()));
            arguments.put("rowName", ObjectTypes.rowNameFromURL(ps.getPreviousURL()));
            arguments.put("percolationPath", ps.getPercolationPath());

            for (RelationQuery query : pr.getRelationQueries()) {
                res.addAll(query.execute(arguments, "fromURL", s));
            }

        } else {
            res
                    .addAll((Collection<? extends Object[]>) s
                            .createQuery(
                                    "SELECT r.fromURL as fromURL, r.type as type, r.toURL as toURL, r.id as relationId from org.makumba.devel.relations.Relation r where r.fromURL = :fromURL")
                            .setString("fromURL", ps.getPreviousURL()));
        }

        return res;
    }

}
