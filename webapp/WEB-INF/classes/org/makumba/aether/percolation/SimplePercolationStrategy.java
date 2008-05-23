package org.makumba.aether.percolation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.makumba.aether.AetherEvent;
import org.makumba.aether.PercolationException;
import org.makumba.aether.UserTypes;
import org.makumba.aether.model.InitialPercolationRule;
import org.makumba.aether.model.MatchedAetherEvent;
import org.makumba.aether.model.PercolationRule;
import org.makumba.aether.model.PercolationStep;
import org.makumba.aether.model.RelationQuery;
import org.makumba.parade.aether.ActionTypes;
import org.makumba.parade.aether.ObjectTypes;

public class SimplePercolationStrategy implements PercolationStrategy {

    private static final int MIN_ENERGY_LEVEL = 21;

    private Logger logger = Logger.getLogger(SimplePercolationStrategy.class);

    /**
     * <ul>
     * <li>matches the event against the initial percolation rules</li>
     * <li>for each match, percolate this matched event:</li>
     * <ul>
     * <li>grab all the relations of the object</li>
     * <li>for each relation, match it against the percolation rules</li>
     * <li>write the percolation step, that includes F/N</li>
     * <li>if the energy is high enough, start again from 7</li>
     * </ul>
     * </ul>
     * 
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

    private void percolateMatchedEvent(MatchedAetherEvent mae, Session s) {
        logger.debug("Starting percolation of matched event \"" + mae.toString() + "\"");

        List<Object[]> initialRelations = getRelationsForMatchedEvent(mae, s);

        if (initialRelations == null) {
            return;
        }
        
        logger.debug("Going to percolate along "+initialRelations.size()+" initial relations");

        if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.FOCUS_PERCOLATION) {
            percolate(mae, s, initialRelations, mae.getInitialPercolationLevel(), true, null);
        } else if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.NIMBUS_PERCOLATION) {
            percolate(mae, s, initialRelations, mae.getInitialPercolationLevel(), false, null);
        } else if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.FOCUS_NIMBUS_PERCOLATION) {
            percolate(mae, s, initialRelations, mae.getInitialPercolationLevel(), true, null);
            percolate(mae, s, initialRelations, mae.getInitialPercolationLevel(), false, null);
        }

    }

    // match against percolation rules
    // there are two kind of percolation rules: focus percolation and nimbus percolation
    // this is actually something that should be given by the initial percolation rule
    // so for each of the matches, we do focus or nimbus percolation, that happen a bit differently

    // - iterate
    // ==> this is going to be a nice recursive algorithm
    // invariants along percolation: MatchedAetherEvent
    // variants along percolation: previous step (null at beginning), F/N level, percolation rule that lead to match,
    // relations to explore
    private void percolate(MatchedAetherEvent mae, Session s, List<Object[]> relations, int level,
            boolean isFocusPercolation, PercolationStep previousStep) {

        logger.debug("Going to percolate along "+relations.size()+" discovered relations");
        

        for (Object[] relation : relations) {
            logger.debug((isFocusPercolation ? "FOCUS PERCOLATION" : "NIMBUS PERCOLATION") + "\t"
                    + Arrays.toString(relation));

            if (isFocusPercolation) {

                // we figure the percolation rules that may apply to this kind of event

                Query q = s
                        .createQuery("SELECT r from PercolationRule r where r.subject = :subjectType and r.predicate = :predicate and r.object = :objectType");
                q.setParameter("subjectType", ObjectTypes.USER.toString());
                q.setParameter("predicate", mae.getAction());
                q.setParameter("objectType", mae.getObjectType());

                // for each match
                for (PercolationRule pr : (List<PercolationRule>) q.list()) {
                    logger.debug("\t == Processing percolation rule match of rule "+pr.toString());

                    // record a new percolation step
                    logger.debug("\t Recording percolation step with level " + level);

                    PercolationStep ps = buildPercolationStep(mae, (String) relation[2], pr, level, isFocusPercolation,
                            previousStep);
                    s.save(ps);
                }

            } else {

                Query q = s
                        .createQuery("SELECT r from PercolationRule r where r.subject = :subjectType and r.predicate = :predicate and r.object = :objectType");
                q.setParameter("subjectType", ObjectTypes.typeFromURL((String) relation[0]));
                q.setParameter("predicate", relation[1]);
                q.setParameter("objectType", ObjectTypes.typeFromURL((String) relation[2]));

                // for each match
                for (PercolationRule pr : (List<PercolationRule>) q.list()) {
                    logger.debug("\t == Processing percolation rule match of rule "+pr.toString());

                    // we remove the consumption of the previous relation
                    int relationLevel = level - pr.getConsumption();

                    // - record a new percolation step (object, userGroup, focus/nimbus, matched aether event, previous
                    // (if any))
                    logger.debug("\t Recording percolation step with level " + relationLevel);
                    PercolationStep ps = buildPercolationStep(mae, (String) relation[2], pr, relationLevel, isFocusPercolation,
                            previousStep);
                    s.save(ps);

                    if (relationLevel < MIN_ENERGY_LEVEL) {

                        logger.debug("\t Percolation of event "+mae.toString()+" stopped after relation "+Arrays.toString(relation)+" due to insuffiscient energy");
                        
                    } else {

                        // - get the next relations (use a RelationQuery for this)
                        List<Object[]> nextRelations = getNextRelations(relation, pr, s);

                        // do it again, again, again
                        percolate(mae, s, nextRelations, relationLevel, isFocusPercolation, ps);

                    }
                }
            }
        }
    }

    /**
     * Gets all the relations for an initially matched event
     * 
     * @param mae
     *            the {@link MatchedAetherEvent}
     * @param s
     *            a Hibernate {@link Session}
     * @return fromURL type toURL
     */
    private List<Object[]> getRelationsForMatchedEvent(MatchedAetherEvent mae, Session s) {

        // first let's get the queries from the MatchedEvent
        List<RelationQuery> queries = mae.getInitialPercolationRule().getRelationQueries();

        List<Object[]> res = new LinkedList<Object[]>();

        for (RelationQuery query : queries) {
            logger.debug("Executing relation query: " + query+ " with parameter fromURL="+mae.getObjectURL());
            res.addAll(s.createQuery(query.getQuery()).setString("fromURL", mae.getObjectURL()).list());
        }
        return res;
    }

    private List<Object[]> getNextRelations(Object[] relation, PercolationRule pr, Session s) {

        List<Object[]> res = new LinkedList<Object[]>();

        if (pr.getRelationQueries().size() > 0) {

            for (RelationQuery query : pr.getRelationQueries()) {
                logger.debug("Executing relation query: " + query+ " with parameter fromURL="+(String)relation[2]);
                res.addAll(s.createQuery(query.getQuery()).setString("fromURL", (String) relation[2]).list());
            }

        } else {
            res
                    .addAll(s
                            .createQuery(
                                    "SELECT r.fromURL as fromURL, r.type as type, r.toURL as toURL from org.makumba.devel.relations.Relation r where r.fromURL = :fromURL")
                            .setString("fromURL", (String) relation[2]).list());
        }

        return res;
    }

    /**
     * Gets all the relations of a matched event
     * 
     * TODO implement other types than just FILE
     * 
     * @param mae
     *            the {@link MatchedAetherEvent}
     * @param s
     *            a hibernate {@link Session}
     * @return a String array containing as fields: the fromURL, type and toURL of the relation
     */
    private List<String[]> getAllRelations(MatchedAetherEvent mae, Session s) {

        List<String[]> res = null;

        if (mae.getObjectType().equals(ObjectTypes.FILE.toString())) {
            res = getDependsOnRelations(mae, s);
            res.addAll(getVersionOfRelations(mae, s));
        }

        if (mae.getObjectType().equals(ObjectTypes.ROW.toString())) {

        }

        if (mae.getObjectType().equals(ObjectTypes.USER.toString())) {

        }

        if (mae.getObjectType().equals(ObjectTypes.DIR.toString())) {

        }

        return res;
    }

    private List<String[]> getDependsOnRelations(MatchedAetherEvent mae, Session s) {
        return s
                .createQuery(
                        "SELECT r.fromURL as fromURL, r.type as type, r.toURL as toURL from org.makumba.devel.relations.Relation r where r.fromURL = :fromURL")
                .setParameter("fromURL", mae.getObjectURL()).list();
    }

    private List<String[]> getVersionOfRelations(MatchedAetherEvent mae, Session s) {
        return s
                .createQuery(
                        "SELECT concat('file://', concat(r.rowname, substring(f.path, length(r.rowpath) + 1, length(f.path)))) as fromURL, 'versionOf' as type, f.cvsURI as toURL FROM File f JOIN f.row r WHERE concat('file://', concat(r.rowname, substring(f.path, length(r.rowpath) + 1, length(f.path)))) = :fromURL AND f.cvsURI is not null")
                .setString("fromURL", mae.getObjectURL()).list();
    }

    /**
     * Builds a {@link MatchedAetherEvent} from a {@link AetherEvent} and a {@link InitialPercolationRule} it was
     * matcehd against
     * 
     * @param e
     *            the {@link AetherEvent} to match
     * @param ipr
     *            the {@link InitialPercolationRule} to match it against
     * @param s
     *            a hibernate {@link Session} useful to do queries
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
     * @return a {@link PercolationStep} corresponding to this part of the percolation
     */
    private PercolationStep buildPercolationStep(MatchedAetherEvent mae, String objectURL, PercolationRule pr,
            int level, boolean isFocusPercolation, PercolationStep previous) {

        return new PercolationStep(objectURL, mae.getUserGroup(), (isFocusPercolation ? level : 0),
                (!isFocusPercolation ? level : 0), pr, mae, previous);

    }

}
