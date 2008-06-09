package org.makumba.aether.percolation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import org.makumba.aether.UserTypes;
import org.makumba.aether.model.Focus;
import org.makumba.aether.model.InitialPercolationRule;
import org.makumba.aether.model.MatchedAetherEvent;
import org.makumba.aether.model.PercolationRule;
import org.makumba.aether.model.PercolationStep;
import org.makumba.aether.model.RelationQuery;
import org.makumba.parade.aether.ActionTypes;
import org.makumba.parade.aether.ObjectTypes;

/**
 * Simple rule-based percolation strategy, capable of performing percolation inside of a virtual semantic network.<br>
 * The network does not exist when percolation starts, it is built according to the rules that match the event at hand,
 * which provide queries in order to determine the next relations to percolate through.
 * 
 * @author Manuel Gay
 * 
 */
public class SimplePercolationStrategy implements PercolationStrategy {

    private static final int MAX_PERCOLATION_TIME = 5000;

    private Logger logger = Logger.getLogger(SimplePercolationStrategy.class);

    /**
     * The entry point for the percolation of an {@link AetherEvent}, that matches the event against the initial
     * percolation rules
     */
    public void percolate(AetherEvent e, SessionFactory sessionFactory) throws PercolationException {

        try {

            List<MatchedAetherEvent> matchedEvents = new LinkedList<MatchedAetherEvent>();

            Session s = null;
            Transaction tx = null;
            try {
                s = sessionFactory.openSession();
                tx = s.beginTransaction();

                Query q = s
                        .createQuery("SELECT r from InitialPercolationRule r where r.objectType = :objectType and r.action = :action ");
                q.setParameter("objectType", e.getObjectType());
                q.setParameter("action", e.getAction());

                List<InitialPercolationRule> iprs = q.list();
                for (InitialPercolationRule ipr : iprs) {
                    MatchedAetherEvent mae = buildMatchedAetherEvent(e, ipr, s);
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
     *            a Hibernte {@link Session}
     */
    private void percolateMatchedEvent(MatchedAetherEvent mae, Session s) {
        logger.debug("Starting percolation of matched event \"" + mae.toString() + "\"");

        // first let's get the queries from the MatchedEvent
        List<RelationQuery> queries = mae.getInitialPercolationRule().getRelationQueries();

        List<Object[]> res = new LinkedList<Object[]>();

        for (RelationQuery query : queries) {
            Map<String, Object> arguments = new HashMap<String, Object>();
            arguments.put("fromURL", mae.getObjectURL());
            arguments.put("percolationPath", "");
            res.addAll(query.execute(this, arguments, s));
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
                        isFocusPercolation, previousStep, threadLevel);
                s.save(ps);
                
                addOrUpdateFocus(mae.getObjectURL(), mae.getActor(), energy, s);
                
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
                                relationEnergy, isFocusPercolation, previousStep, threadLevel);
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
                            if (!(now - start > MAX_PERCOLATION_TIME)) {
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
                    if (!(now - start > MAX_PERCOLATION_TIME)) {
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

            Map<String, Object> arguments = new HashMap<String, Object>();
            arguments.put("fromURL", ps.getObjectURL());
            arguments.put("cvsURL", ObjectTypes.fileToCVSURL(ps.getObjectURL()));
            arguments.put("rowName", getRowNameFromURL(ps.getPreviousURL()));
            arguments.put("percolationPath", ps.getPercolationPath());

            for (RelationQuery query : pr.getRelationQueries()) {
                res.addAll(query.execute(this, arguments, s));
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

    /**
     * Builds a {@link MatchedAetherEvent} from a {@link AetherEvent} and a {@link InitialPercolationRule} it was
     * matched against.
     * 
     * @param e
     *            the {@link AetherEvent} to match
     * @param ipr
     *            the {@link InitialPercolationRule} to match it against
     * @param s
     *            a hibernate {@link Session} useful to do queries
     * 
     * @return a {@link MatchedAetherEvent} if anything was matched, null otherwise
     */
    private MatchedAetherEvent buildMatchedAetherEvent(AetherEvent e, InitialPercolationRule ipr, Session s) {

        String userGroup = "";

        if (ipr.getUserType().equals(UserTypes.ALL.type())) {
            userGroup = "*";
        }
        if (ipr.getUserType().equals(UserTypes.ALL_BUT_ACTOR.type())) {
            userGroup = "*,-" + e.getActor();
        }
        if (ipr.getUserType().equals(UserTypes.NONE.type())) {
            userGroup = "";
        }
        if (ipr.getUserType().equals(UserTypes.ALL_BUT_OWNER.type())) {

            if (ActionTypes.isFileAction(e.getAction())) {
                List<String> own = s
                        .createQuery(
                                "SELECT r.fromURL FROM org.makumba.devel.relations.Relation r WHERE r.toURL = :toURL AND r.type = 'owns'")
                        .setParameter("toURL", ObjectTypes.fileFromRow(e.getObjectURL())).list();

                userGroup = "*";
                for (String owner : own) {
                    userGroup += ",-" + owner;
                }
            }

        }
        if (ipr.getUserType().equals(UserTypes.OWNER)) {

            if (ActionTypes.isFileAction(e.getAction())) {
                List<String> own = s
                        .createQuery(
                                "SELECT r.fromURL FROM org.makumba.devel.relations.Relation r WHERE r.toURL = :toURL AND r.type = 'owns'")
                        .setParameter("toURL", ObjectTypes.fileFromRow(e.getObjectURL())).list();

                userGroup = "";
                for (Iterator<String> iterator = own.iterator(); iterator.hasNext();) {
                    userGroup += iterator.next();
                    if (iterator.hasNext()) {
                        userGroup += ",";
                    }
                }
            }
        }
        if (ipr.getUserType().equals(UserTypes.ACTOR)) {
            userGroup = e.getActor();
        }

        if (userGroup.length() > 0) {
            return new MatchedAetherEvent(e, userGroup, ipr);
        }

        return null;
    }

    /**
     * Builds a percolation step from a {@link MatchedAetherEvent} and the {@link PercolationRule} that matched it
     * 
     * @param mae
     *            the originating {@link MatchedAetherEvent}
     * @param pr
     *            the {@link PercolationRule}
     * @param isFocusPercolation
     *            <code>true</code> if this is focus percolation, <code>false</code> if it's nimbus percolation
     * @param percolationLevel
     *            TODO
     * @param percolationPath
     *            TODO
     * @return a {@link PercolationStep} corresponding to this part of the percolation
     */
    private PercolationStep buildPercolationStep(MatchedAetherEvent mae, String previousURL, String objectURL,
            PercolationRule pr, int level, boolean isFocusPercolation, PercolationStep previous, int percolationLevel) {

        return new PercolationStep(previousURL, objectURL, isFocusPercolation ? mae.getActor() : mae.getUserGroup(), (isFocusPercolation ? level : 0),
                (!isFocusPercolation ? level : 0), pr, mae, previous, percolationLevel);

    }
    
    /**
     * Adds or updates the total focus of an object for a user
     * 
     * @param objectURL the URL to the object
     * @param user the user 
     * @param energy the energy to be added
     * @param s a Hibernate session
     */
    private void addOrUpdateFocus(String objectURL, String user, int energy, Session s) {
        int updated = s.createQuery("update Focus set focus = focus + :energy where user = :user and objectURL = :objectURL").setString("user", user).setString("objectURL", objectURL).setParameter("energy", energy).executeUpdate();
        if(updated == 0) {
            Transaction tx = s.beginTransaction();
            Focus f = new Focus(objectURL, user, energy);
            s.save(f);
            tx.commit();
        }
        
    }

    // very ugly code. but I'm too tired
    private static String getRowNameFromURL(String URL) {
        int n = URL.indexOf(":");
        if (n == -1) {
            return "";
        }
        if (!(URL.charAt(++n) == '/')) {
            return "";
        }
        if (!(URL.charAt(++n) == '/')) {
            return "";
        }
        n++;
        URL = URL.substring(n);
        return URL.substring(0, URL.indexOf("/"));
    }

}
