package org.makumba.aether.percolation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.makumba.aether.AetherEvent;
import org.makumba.aether.PercolationException;
import org.makumba.aether.model.InitialPercolationRule;
import org.makumba.aether.model.MatchedAetherEvent;
import org.makumba.aether.model.PercolationRule;
import org.makumba.aether.model.PercolationStep;
import org.makumba.aether.model.RelationQuery;
import org.makumba.parade.aether.ObjectTypes;

/**
 * Percolation strategy that tries to group as many queries together during the percolation process, in order to
 * minimise the number of executed queries.
 * 
 * @author Manuel Gay
 * 
 */
public class GroupedPercolationStrategy extends RuleBasedPercolationStrategy {
    
    private Logger logger = Logger.getLogger(GroupedPercolationStrategy.class);

    private MultiValueMap initialPercolationRules = new MultiValueMap();

    private MultiValueMap percolationRules = new MultiValueMap();

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
    public void percolate(AetherEvent e, SessionFactory sessionFactory) throws PercolationException {
        super.percolate(e, sessionFactory);

        try {

            List<MatchedAetherEvent> matchedEvents = new LinkedList<MatchedAetherEvent>();

            Session s = null;
            Transaction tx = null;
            try {
                s = sessionFactory.openSession();
                tx = s.beginTransaction();

                String iprMatchKey = e.getObjectType() + "#" + e.getAction();
                Collection<InitialPercolationRule> ipr = initialPercolationRules.getCollection(iprMatchKey);
                if(ipr != null) {
                    
                for (InitialPercolationRule r : ipr) {
                    MatchedAetherEvent mae = buildMatchedAetherEvent(e, r, s);
                    s.save(mae);
                    matchedEvents.add(mae);
                }

                for (MatchedAetherEvent mae : matchedEvents) {
                    percolateMatchedEvent(mae, s);
                }

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

        List<Object[]> initialRelations = new LinkedList<Object[]>();

        for (RelationQuery query : mae.getInitialPercolationRule().getRelationQueries()) {
            Map<String, String> arguments = new HashMap<String, String>();
            addSetArgument(arguments, "fromURLSet", mae.getObjectURL());
            addSetArgument(arguments, "cvsURLSet", ObjectTypes.fileToCVSURL(mae.getObjectURL()));
            addSetArgument(arguments, "rowNameSet", ObjectTypes.getRowNameFromURL(mae.getObjectURL()));
            addSetArgument(arguments, "percolationPathSet", "");
            addSetArgument(arguments, "fromURLAndTraversedCVSSet", mae.getObjectURL() + "False");
            
            initialRelations.addAll(query.execute(arguments, s));
        }

        if (initialRelations.size() == 0) {
            return;
        }

        logger.debug("Going to percolate along " + initialRelations.size() + " initial relations");

        Map<String, NodePercolationStatus> nodePercolationStatuses = new HashMap<String, NodePercolationStatus>();

        int initialEnergy = new Long(Math.round(mae.getInitialPercolationLevel()
                * (0.5 + mae.getInitialLevelCoefficient()))).intValue();

        if (mae.getInitialLevelCoefficient() == 0.00)
            initialEnergy = 0;

        NodePercolationStatus init = new NodePercolationStatus(mae.getObjectURL(), initialEnergy, 0, null);
        nodePercolationStatuses.put(mae.getObjectURL(), init);

        if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.FOCUS_PERCOLATION) {
            percolate(mae, s, true, initialRelations, nodePercolationStatuses);
        } else if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.NIMBUS_PERCOLATION) {
            percolate(mae, s, false, initialRelations, nodePercolationStatuses);
        } else if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.FOCUS_NIMBUS_PERCOLATION) {
            percolate(mae, s, true, initialRelations, nodePercolationStatuses);
            percolate(mae, s, false, initialRelations, nodePercolationStatuses);
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
     * @param relations
     *            the relations (fromURL, type, toURL) to percolate through
     * @param groupedPercolationInfo
     *            TODO
     */
    private void percolate(MatchedAetherEvent mae, Session s, boolean isFocusPercolation, List<Object[]> relations,
            Map<String, NodePercolationStatus> previousNodePercolationStatuses) {
        
        System.out.println("********************************************* relations: "+relations.size());
        System.out.println("********************************************* previousNodePercolationStatuses: "+previousNodePercolationStatuses.size());

        long start = System.currentTimeMillis();

        if (isFocusPercolation) {

            // for focus percolation, we actually don't percolate, but we just set the focus of the user on the object
            // he / she touched
            // IF there are any PercolationRules that deal with this
            // those focus-setting PercolationRules look like "USER saved FILE" or the like

            logger.debug("====== START FOCUS PERCOLATION ======");

            String percolationRuleMatchKey = ObjectTypes.USER.toString() + "#" + mae.getAction() + "#"
                    + mae.getObjectType();
            Collection<PercolationRule> prs = percolationRules.getCollection(percolationRuleMatchKey);
            if(prs != null) {

                // we need to fetch the nodePercolationStatus for this step
                // since focus percolation happens at the first step, we get the previous object directly from the
                // MatchedAetherEvent
                NodePercolationStatus nps = previousNodePercolationStatuses.get(mae.getObjectURL());

                for (PercolationRule pr : prs) {
                    logger.debug("\t == Processing percolation rule match of rule " + pr.toString());

                    // record a new percolation step
                    logger.debug("\t == Recording percolation step with level " + nps.getEnergy());

                    PercolationStep ps = buildPercolationStep(mae, mae.getObjectURL(), mae.getObjectURL(), pr, nps
                            .getEnergy(), isFocusPercolation, nps.getPreviousStep(), nps.getLevel());
                    s.save(ps);

                    addOrUpdateFocus(mae.getObjectURL(), mae.getActor(), nps.getEnergy(), s);

                }

                logger.debug("====== END FOCUS PERCOLATION ======");
                
            }


        } else {

            MultiValueMap groupedQueries = new MultiValueMap();
            Map<String, NodePercolationStatus> nodePercolationStatuses = new HashMap<String, NodePercolationStatus>();

            logger.debug("NIMBUS-PERCOLATION: Going to percolate along " + relations.size() + " discovered relations");

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
                logger.debug("===== START NIMBUS PERCOLATION ====");
                logger.debug("===== " + Arrays.toString(relation));

                String percolationRuleMatchKey = ObjectTypes.typeFromURL((String) relation[0]) + "#" + relation[1] + "#"
                        + ObjectTypes.typeFromURL((String) relation[2]);

                // for each match
                Collection<PercolationRule> matchedRules = (Collection<PercolationRule>) percolationRules.getCollection(percolationRuleMatchKey);
                if(matchedRules == null) {
                    matchedRules = new Vector<PercolationRule>();
                }
                for (PercolationRule pr : matchedRules) {
                    logger.debug("\t == Processing percolation rule match of rule " + pr.toString());

                    // retrieve the NodePercolationStatus of the previous node
                    NodePercolationStatus nps = previousNodePercolationStatuses.get(relation[2]);
                    if(nps == null) {
                        System.out.println("yagadou");
                    }

                    if (nps.getEnergy() < RuleBasedPercolator.MIN_ENERGY_LEVEL) {
                        logger.debug("NIMBUS-PERCOLATION: Not percolating any further because energy too low: "
                                + nps.getEnergy());
                    } else {

                        // ///// BEGIN to write the PercolationStep of the previous node //////

                        // we remove the consumption of the previous relation
                        int relationEnergy = nps.getEnergy() - pr.getConsumption();

                        // - record a new percolation step
                        logger.debug("\t == Recording percolation step with level " + relationEnergy);
                        PercolationStep ps = buildPercolationStep(mae, (String) relation[2], (String) relation[0], pr,
                                relationEnergy, isFocusPercolation, nps.getPreviousStep(), nps.getLevel());
                        s.save(ps);

                        // now we can set the threadPath
                        // basically this goes like parent-threadPath + parentId + thisUID
                        ps.setPercolationPath(nps.getPreviousStep() == null ? ps.getId() + "_" + ps.getObjectURL()
                                : nps.getPreviousStep().getPercolationPath() + "_" + ps.getId() + "_"
                                        + ps.getObjectURL());

                        // and we also set the threadRoot
                        ps.setRoot(nps.getPreviousStep() == null ? ps : nps.getPreviousStep().getRoot());

                        // ///// END to write the PercolationStep of the previous node //////

                        if (relationEnergy < RuleBasedPercolator.MIN_ENERGY_LEVEL) {

                            logger.debug("NIMBUS-PERCOLATION: Percolation of event " + mae.toString()
                                    + " stopped after relation " + Arrays.toString(relation)
                                    + " due to insuffiscient energy");

                        } else {

                            long now = System.currentTimeMillis();
                            if (!(now - start > RuleBasedPercolator.MAX_PERCOLATION_TIME)) {

                                // register the queries and parameters to retrieve the next relations
                                for (RelationQuery rq : pr.getRelationQueries()) {
                                    groupedQueries.put(rq, ps);
                                }

                                // register the status of the node after this percolation
                                nodePercolationStatuses.put((String) relation[0], new NodePercolationStatus(
                                        (String) relation[0], relationEnergy, nps.getLevel() + 1, ps));

                            } else {
                                logger.debug("NIMBUS-PERCOLATION: Percolation of event " + mae.toString()
                                        + " stopped after relation " + Arrays.toString(relation) + " due to timeout");
                                break;
                            }
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

            // get all the next queries
            relations = retrieveNextRelations(groupedQueries, s);

            // if something is left, move to the next step
            if(relations.size() > 0 && nodePercolationStatuses.size() > 0) {
                percolate(mae, s, isFocusPercolation, relations, nodePercolationStatuses);
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
    private List<Object[]> retrieveNextRelations(MultiValueMap groupedQueries, Session s) {

        List<Object[]> results = new ArrayList<Object[]>();

        // for each of the RelationQueries we need to generate the arguments in such a way that they are in fact sets
        // this helps the optimisation process a lot, but makes writing queries a rather cumbersome and difficult task

        for (RelationQuery rq : (Collection<RelationQuery>) groupedQueries.keySet()) {

            Map<String, String> arguments = new HashMap<String, String>();

            Collection<PercolationStep> percolationSteps = groupedQueries.getCollection(rq);

            for (PercolationStep ps : percolationSteps) {

                addSetArgument(arguments, "fromURLSet", ps.getObjectURL());
                addSetArgument(arguments, "cvsURLSet", ObjectTypes.fileToCVSURL(ps.getObjectURL()));
                addSetArgument(arguments, "rowNameSet", ObjectTypes.getRowNameFromURL(ps.getPreviousURL()));
                addSetArgument(arguments, "percolationPathSet", ps.getPercolationPath());
                addSetArgument(arguments, "fromURLAndTraversedCVSSet", ps.getPercolationPath().indexOf("cvs://") > 0 ? ps.getObjectURL() + "True" : ps.getObjectURL() + "False");

            }

            results.addAll(rq.execute(arguments, s));

        }

        return results;
    }
    
    public static String[] supportedArguments = {"fromURLSet", "cvsURLSet", "rowNameSet", "percolationPathSet", "fromURLAndTraversedCVSSet"};

    private void addSetArgument(Map<String, String> arguments, String key, String value) {
        if(value == null || value.trim().length() == 0) {
            return;
        }
        String argumentSet = arguments.get(key);
        if (argumentSet == null) {
            arguments.put(key, value);
        } else {
            if(argumentSet.endsWith("'")) {
                arguments.put(key, argumentSet + "," + "'" + value + "'");
            } else {
                arguments.put(key, "'" + argumentSet + "'" + "," + "'" + value + "'");
            }
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

    public class NodePercolationStatus {

        private String key;

        private int energy;

        private int level;

        private PercolationStep previousStep;

        public String getKey() {
            return key;
        }

        public int getEnergy() {
            return energy;
        }

        public int getLevel() {
            return level;
        }

        public PercolationStep getPreviousStep() {
            return previousStep;
        }

        public NodePercolationStatus(String key, int energy, int level, PercolationStep previousStep) {
            super();
            this.key = key;
            this.energy = energy;
            this.level = level;
            this.previousStep = previousStep;
        }

    }
}
