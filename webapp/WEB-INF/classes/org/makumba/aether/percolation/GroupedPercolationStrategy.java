package org.makumba.aether.percolation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import org.apache.commons.collections.map.MultiValueMap;
import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.makumba.aether.Aether;
import org.makumba.aether.AetherEvent;
import org.makumba.aether.PercolationException;
import org.makumba.aether.model.InitialPercolationRule;
import org.makumba.aether.model.MatchedAetherEvent;
import org.makumba.aether.model.PercolationRule;
import org.makumba.aether.model.PercolationStep;
import org.makumba.aether.model.RelationQuery;
import org.makumba.parade.aether.ObjectTypes;
import org.makumba.parade.model.Row;

/**
 * Percolation strategy that tries to group as many queries together during the percolation process, in order to
 * minimise the number of executed queries.
 * 
 * @author Manuel Gay
 * 
 */
public class GroupedPercolationStrategy extends RuleBasedPercolationStrategy {

    private static Logger logger = Aether.getAetherLogger(GroupedPercolationStrategy.class.getName());

    private MultiValueMap initialPercolationRules = new MultiValueMap();

    private static MultiValueMap percolationRules = new MultiValueMap();

    private SessionFactory sessionFactory;

    public GroupedPercolationStrategy(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        configure();
    }

    /**
     * The entry point for the percolation of an {@link AetherEvent}, that matches the event against the initial
     * percolation rules
     */
    @Override
    public void percolate(AetherEvent e, boolean virtualPercolation, SessionFactory sessionFactory)
            throws PercolationException {
        int startQueries = RelationQuery.getExecutedQueries();
        super.percolate(e, virtualPercolation, sessionFactory);

        try {

            List<MatchedAetherEvent> matchedEvents = new LinkedList<MatchedAetherEvent>();

            Session s = null;
            Transaction tx = null;
            boolean shouldCloseSession = true;
            try {
                s = sessionFactory.openSession();
                tx = s.beginTransaction();

                String iprMatchKey = e.getObjectType() + "#" + e.getAction();
                Collection<InitialPercolationRule> ipr = initialPercolationRules.getCollection(iprMatchKey);
                if (ipr != null) {

                    for (InitialPercolationRule r : ipr) {
                        MatchedAetherEvent mae = buildMatchedAetherEvent(e, r, virtualPercolation, s);
                        s.save(mae);
                        matchedEvents.add(mae);
                    }

                    for (MatchedAetherEvent mae : matchedEvents) {

                        if (mae.getInitialPercolationRule().getInteractionType() == InitialPercolationRule.IMMEDIATE_INTERACTION
                                || virtualPercolation) {
                            percolateMatchedEvent(mae, virtualPercolation, s);
                            int endQueries = RelationQuery.getExecutedQueries();
                            logger.fine("Percolation of event " + e.toString() + " needed "
                                    + (endQueries - startQueries) + " queries to be executed");
                        } else if (mae.getInitialPercolationRule().getInteractionType() == InitialPercolationRule.DIFFERED_INTERACTION) {
                            shouldCloseSession = false;
                            PercolationThread t = new PercolationThread(mae, s, tx, startQueries);
                            t.start();

                        }
                    }
                }

                if (shouldCloseSession) {
                    tx.commit();
                }

            } finally {
                if (s != null && shouldCloseSession) {
                    s.flush();
                    s.close();
                }
            }

        } catch (Exception ex) {
            throw new PercolationException(ex);
        }

    }

    public class PercolationThread extends Thread {

        private MatchedAetherEvent mae;

        private Session s;

        private Transaction tx;

        private int startQueries;

        @Override
        public void run() {

            percolateMatchedEvent(mae, false, s);
            tx.commit();
            s.flush();
            s.close();

        }

        public PercolationThread(MatchedAetherEvent mae, Session s, Transaction tx, int startQueries) {
            this.mae = mae;
            this.s = s;
            this.tx = tx;
            this.startQueries = startQueries;

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
     * @param virtualPercolation
     *            TODO
     * @param s
     *            a Hibernate {@link Session}
     */
    private static void percolateMatchedEvent(MatchedAetherEvent mae, boolean virtualPercolation, Session s) {
        logger.fine("Starting " + (virtualPercolation ? "virtual " : "") + "percolation of matched event \""
                + mae.toString() + "\"");

        List<Object[]> initialRelations = new LinkedList<Object[]>();

        for (RelationQuery query : mae.getInitialPercolationRule().getRelationQueries()) {
            Map<String, String> arguments = new HashMap<String, String>();
            addSetArgument(arguments, "fromURLSet", mae.getObjectURL());
            addSetArgument(arguments, "cvsURLSet", fileToCVSURL(mae.getObjectURL()));
            addSetArgument(arguments, "rowNameSet", ObjectTypes.rowNameFromURL(mae.getObjectURL()));
            addSetArgument(arguments, "percolationPathSet", "");
            addSetArgument(arguments, "fromURLAndTraversedCVSSet", mae.getObjectURL() + "False");
            addSetArgument(arguments, "actorSet", mae.getActor());

            initialRelations.addAll(query.execute(arguments, "fromURLSet", s));
        }

        logger.fine("Going to percolate along " + initialRelations.size() + " initial relations");

        Map<String, NodePercolationStatus> nodePercolationStatuses = new HashMap<String, NodePercolationStatus>();
        Vector<PercolationStep> steps = new Vector<PercolationStep>();
        Hashtable<String, Integer> nimbusContributions = new Hashtable<String, Integer>();

        int initialEnergy = new Long(Math.round(mae.getInitialPercolationLevel()
                * (0.5 + mae.getInitialLevelCoefficient()))).intValue();

        if (mae.getInitialLevelCoefficient() == 0.00)
            initialEnergy = 0;

        NodePercolationStatus init = new NodePercolationStatus(mae.getObjectURL(), initialEnergy, 0, null);
        nodePercolationStatuses.put(mae.getObjectURL(), init);

        if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.FOCUS_PERCOLATION) {
            percolate(mae, s, true, System.currentTimeMillis(), initialRelations, nodePercolationStatuses, steps,
                    nimbusContributions, virtualPercolation);
        } else if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.NIMBUS_PERCOLATION) {
            percolate(mae, s, false, System.currentTimeMillis(), initialRelations, nodePercolationStatuses, steps,
                    nimbusContributions, virtualPercolation);
        } else if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.FOCUS_NIMBUS_PERCOLATION) {
            percolate(mae, s, true, System.currentTimeMillis(), initialRelations, nodePercolationStatuses, steps,
                    nimbusContributions, virtualPercolation);
            percolate(mae, s, false, System.currentTimeMillis(), initialRelations, nodePercolationStatuses, steps,
                    nimbusContributions, virtualPercolation);
        }

        // save all the percolation steps
        s.setCacheMode(CacheMode.IGNORE);
        for (int i = 0; i < steps.size(); i++) {
            PercolationStep ps = steps.get(i);
            s.save(ps);

            if ( i % 50 == 0 ) { //20, same as the JDBC batch size
                //flush a batch of inserts and release memory:
                s.flush();
                s.clear();
            }
        }

        // save all the nimbus contributions
        Iterator<String> ni = nimbusContributions.keySet().iterator();
        while (ni.hasNext()) {
            String objectURL = ni.next();
            addOrUpdateNimbus(objectURL, mae.getUserGroup(), nimbusContributions.get(objectURL), virtualPercolation, s);
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
     * @param isFocusPercolation
     *            whether this is focus or nimbus percolation
     * @param startTime
     *            TODO
     * @param relations
     *            the relations (fromURL, type, toURL) to percolate through
     * @param steps
     *            a Vector that will get filled with the {@link PercolationStep}-s that are generated along the
     *            percolation
     * @param nimbusContributions
     *            TODO
     * @param virtualPercolation
     *            <code>true</code> if this is a virtual relation
     * @param groupedPercolationInfo
     *            TODO
     */
    private static void percolate(MatchedAetherEvent mae, Session s, boolean isFocusPercolation, long startTime,
            List<Object[]> relations, Map<String, NodePercolationStatus> previousNodePercolationStatuses,
            Vector<PercolationStep> steps, Hashtable<String, Integer> nimbusContributions, boolean virtualPercolation) {

        if (isFocusPercolation) {

            // for focus percolation, we actually don't percolate, but we just set the focus of the user on the object
            // he / she touched
            // IF there are any PercolationRules that deal with this
            // those focus-setting PercolationRules look like "USER saved FILE" or the like

            logger.fine("====== START FOCUS PERCOLATION ======");

            String percolationRuleMatchKey = ObjectTypes.USER.toString() + "#" + mae.getAction() + "#"
                    + mae.getObjectType();
            Collection<PercolationRule> prs = percolationRules.getCollection(percolationRuleMatchKey);
            if (prs != null) {

                // we need to fetch the nodePercolationStatus for this step
                // since focus percolation happens at the first step, we get the previous object directly from the
                // MatchedAetherEvent
                NodePercolationStatus nps = previousNodePercolationStatuses.get(mae.getObjectURL());

                for (PercolationRule pr : prs) {
                    logger.fine("\t == Processing percolation rule match of rule " + pr.toString());

                    // record a new percolation step
                    logger.fine("\t == Registering percolation step with level " + nps.getEnergy());

                    PercolationStep ps = buildPercolationStep(mae, mae.getObjectURL(), mae.getObjectURL(), pr, nps
                            .getEnergy(), isFocusPercolation, nps.getPreviousStep(), nps.getLevel(), virtualPercolation);
                    steps.add(ps);

                    addOrUpdateFocus(mae.getObjectURL(), mae.getActor(), nps.getEnergy(), virtualPercolation, s);
                }

                logger.fine("====== END FOCUS PERCOLATION ======");

            }

        } else {

            MultiValueMap groupedQueries = new MultiValueMap();
            Map<String, NodePercolationStatus> nodePercolationStatuses = new HashMap<String, NodePercolationStatus>();

            logger.fine("NIMBUS-PERCOLATION: Going to percolate along " + relations.size() + " discovered relations");

            // for each relation
            // = match it against the rules
            // = for each match
            // == retrieve the NodePercolationStatus of the previous node
            // == write the percolation step
            // == if enough energy is left
            // === register the relation queries to be executed together with the parameters
            // === register a new NodePercolationStatus
            // execute all the registered queries
            // percolate again

            for (Object[] relation : relations) {
                logger.fine("===== START NIMBUS PERCOLATION ====");
                logger.fine("===== " + Arrays.toString(relation));

                String percolationRuleMatchKey = ObjectTypes.typeFromURL((String) relation[0]) + "#" + relation[1]
                        + "#" + ObjectTypes.typeFromURL((String) relation[2]);

                // for each match
                Collection<PercolationRule> matchedRules = percolationRules.getCollection(percolationRuleMatchKey);
                if (matchedRules == null) {
                    matchedRules = new Vector<PercolationRule>();
                }
                for (PercolationRule pr : matchedRules) {
                    logger.fine("\t == Processing percolation rule match of rule " + pr.toString());

                    // retrieve the NodePercolationStatus of the previous node
                    NodePercolationStatus nps = previousNodePercolationStatuses.get(relation[2]);

                    if (nps == null) {
                        logger.warning("null nodePercolationStatus when trying to fetch status of relation "
                                + relation[2]);
                    } else if (nps.getEnergy() < RuleBasedPercolator.MIN_ENERGY_LEVEL) {
                        logger.fine("NIMBUS-PERCOLATION: Not percolating any further because energy too low: "
                                + nps.getEnergy());
                    } else if (nps.getLevel() == pr.getPropagationDepthLimit()) {
                        logger
                                .fine("NIMBUS-PERCOLATION: Not percolating any further because percolation depth limit of "
                                        + pr.getPropagationDepthLimit() + " was reached");

                    } else {

                        // ///// BEGIN to write the PercolationStep of the previous node //////

                        // we remove the consumption of the previous relation
                        int relationEnergy = nps.getEnergy() - pr.getConsumption();

                        if (relationEnergy < RuleBasedPercolator.MIN_ENERGY_LEVEL) {

                            logger.fine("NIMBUS-PERCOLATION: Percolation of event " + mae.toString()
                                    + " stopped after relation " + Arrays.toString(relation)
                                    + " due to insuffiscient energy");
                        } else {

                            // - record a new percolation step
                            logger.fine("\t == Registering percolation step with level " + relationEnergy);
                            PercolationStep ps = buildPercolationStep(mae, (String) relation[2], (String) relation[0],
                                    pr, relationEnergy, isFocusPercolation, nps.getPreviousStep(), nps.getLevel(),
                                    virtualPercolation);
                            steps.add(ps);

                            // add the energy to the nimbus contributions of this percolation step so we can do an
                            // update afterwards
                            Integer currentSum = nimbusContributions.get(ps.getObjectURL());
                            if (currentSum == null) {
                                nimbusContributions.put(ps.getObjectURL(), ps.getNimbus());
                            } else {
                                nimbusContributions.put(ps.getObjectURL(), currentSum + ps.getNimbus());
                            }

                            // now we can set the threadPath
                            // basically this goes like parent-threadPath + parentId + thisUID
                            ps.setPercolationPath(nps.getPreviousStep() == null ? ps.hashCode() + "_"
                                    + ps.getObjectURL() : nps.getPreviousStep().getPercolationPath() + "_"
                                    + ps.hashCode() + "_" + ps.getObjectURL());

                            // and we also set the threadRoot
                            ps.setRoot(nps.getPreviousStep() == null ? ps : nps.getPreviousStep().getRoot());

                            // ///// END to write the PercolationStep of the previous node //////

                            if (!(System.currentTimeMillis() - startTime > RuleBasedPercolator.MAX_PERCOLATION_TIME)) {

                                // register the queries and parameters to retrieve the next relations
                                for (RelationQuery rq : pr.getRelationQueries()) {
                                    groupedQueries.put(rq, ps);
                                }

                                // TODO at this level we will add the virtual relation queries
                                // we will have to provide them with the previous step, or so, in order to compute
                                // the adequate level
                                // adding a virtual query will make it possible to later on retrieve additional
                                // percolationSteps to be written to the db

                                // register the status of the node after this percolation
                                nodePercolationStatuses.put((String) relation[0], new NodePercolationStatus(
                                        (String) relation[0], relationEnergy, nps.getLevel() + 1, ps));

                            } else {
                                logger.fine("NIMBUS-PERCOLATION: Percolation of event " + mae.toString()
                                        + " stopped after relation " + Arrays.toString(relation) + " due to timeout");
                                break;
                            }
                        }
                    }
                }
                if (!(System.currentTimeMillis() - startTime > RuleBasedPercolator.MAX_PERCOLATION_TIME)) {
                    continue;
                } else {
                    logger.fine("\t Percolation of event " + mae.toString() + " stopped after relation "
                            + Arrays.toString(relation) + " due to timeout");
                    break;
                }
            }

            if (relations.size() > 0) {

                // get all the next queries
                relations = retrieveNextRelations(groupedQueries, s);

                // if something is left, move to the next step
                if (relations.size() > 0 && nodePercolationStatuses.size() > 0) {
                    percolate(mae, s, isFocusPercolation, startTime, relations, nodePercolationStatuses, steps,
                            nimbusContributions, virtualPercolation);
                }
            }
        }
    }

    /**
     * Retrieves the next relations for nimbus percolation in an optimal manner, i.e. by running one query per
     * RelationQuery
     * 
     * @param groupedQueries
     *            a MultiValueMap containing the different parameters for each RelationQuery to be executed
     * @param s
     *            a Hibernate {@link Session}
     * 
     * @return a List of relations of the kind fromURL, relationType, toURL
     */
    private static List<Object[]> retrieveNextRelations(MultiValueMap groupedQueries, Session s) {

        List<Object[]> results = new ArrayList<Object[]>();

        // for each of the RelationQueries we need to generate the arguments in such a way that they are in fact sets
        // this helps the optimisation process a lot, but makes writing queries a rather cumbersome and difficult task

        for (RelationQuery rq : (Collection<RelationQuery>) groupedQueries.keySet()) {

            Map<String, String> arguments = new HashMap<String, String>();

            Collection<PercolationStep> percolationSteps = groupedQueries.getCollection(rq);

            for (PercolationStep ps : percolationSteps) {

                addSetArgument(arguments, "fromURLSet", ps.getObjectURL());
                addSetArgument(arguments, "cvsURLSet", fileToCVSURL(ps.getObjectURL()));
                addSetArgument(arguments, "rowNameSet", ObjectTypes.rowNameFromURL(ps.getPreviousURL()));
                addSetArgument(arguments, "percolationPathSet", ps.getPercolationPath());
                addSetArgument(arguments, "fromURLAndTraversedCVSSet",
                        ps.getPercolationPath().indexOf("cvs://") > 0 ? ps.getObjectURL() + "True" : ps.getObjectURL()
                                + "False");
                addSetArgument(arguments, "actorSet", ps.getMatchedAetherEvent().getActor());
                
            }

            results.addAll(rq.execute(arguments, "fromURLSet", s));

        }

        return results;
    }

    public static String[] supportedArguments = { "fromURLSet: the set of URLs to the previous objects (from which the relation comes)",
        "cvsURLSet: the set of CVS URLs to the previous objects (as CVS URLs)",
        "rowNameSet: the name of the row the previous objects belongs to",
        "percolationPathSet: the set of percolation paths, i.e. of nodes traversed until now",
        "fromURLAndTraversedCVSSet: the set of URLs to the previous objects followed by True or False according to whether a CVS relation was traversed",
        "actorSet: the actor of the event we are propagating" };

    private static void addSetArgument(Map<String, String> arguments, String key, String value) {
        if (value == null || value.trim().length() == 0) {
            return;
        }
        String argumentSet = arguments.get(key);
        if (argumentSet == null) {
            arguments.put(key, value);
        } else {
            if (argumentSet.endsWith("'")) {
                arguments.put(key, argumentSet + "," + "'" + value + "'");
            } else {
                arguments.put(key, "'" + argumentSet + "'" + "," + "'" + value + "'");
            }
        }
    }

    /**
     * file://rudi-k/lbg/member.jsp -> cvs://karamba/lbg/member.jsp
     */

    public static String fileToCVSURL(String fileURL) {

        String module = Row.getModule(fileURL);

        if (fileURL.startsWith(ObjectTypes.FILE.prefix())) {
            return ObjectTypes.CVSFILE.prefix() + module + "/" + fileURL.substring(ObjectTypes.FILE.prefix().length());
        } else {
            return fileURL;
        }
    }

    /**
     * Loads InitialPercolationRules and PercolationRules into memory
     */
    @Override
    protected void configure() {
        Session s = null;
        Transaction tx = null;
        try {
            s = sessionFactory.openSession();
            tx = s.beginTransaction();

            initialPercolationRules.clear();
            percolationRules.clear();

            List<InitialPercolationRule> iprs = s.createCriteria(InitialPercolationRule.class).add(
                    Restrictions.ne("active", false)).list();

            for (InitialPercolationRule initialPercolationRule : iprs) {
                put(initialPercolationRules, initialPercolationRule.getObjectType() + "#"
                        + initialPercolationRule.getAction(), initialPercolationRule);
            }

            List<PercolationRule> prs = s.createCriteria(PercolationRule.class).add(Restrictions.ne("active", false))
                    .list();

            for (PercolationRule percolationRule : prs) {
                put(percolationRules, percolationRule.getSubject() + "#" + percolationRule.getPredicate() + "#"
                        + percolationRule.getObject(), percolationRule);
            }

            tx.commit();

        } finally {
            if (s != null)
                s.close();
        }
    }

    private void put(MultiValueMap map, String key, Object value) {
        Collection c = map.getCollection(key);
        if (c == null) {
            c = new Vector();
        }
        c.add(value);
        map.putAll(key, c);
    }
}
