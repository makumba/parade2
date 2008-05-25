package org.makumba.aether.percolation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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

        // first let's get the queries from the MatchedEvent
        List<RelationQuery> queries = mae.getInitialPercolationRule().getRelationQueries();

        List<Object[]> res = new LinkedList<Object[]>();

        for (RelationQuery query : queries) {
            Map<String, Object> arguments = new HashMap<String, Object>();
            arguments.put("fromURL", mae.getObjectURL());
            arguments.put("percolationPath", "");
            res.addAll(executeRelationQueries(query, arguments, s));
        }

        List<Object[]> initialRelations = res;

        if (initialRelations == null) {
            return;
        }

        logger.debug("Going to percolate along " + initialRelations.size() + " initial relations");

        if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.FOCUS_PERCOLATION) {
            percolate(mae, s, initialRelations, mae.getInitialPercolationLevel(), true, null, 0);
        } else if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.NIMBUS_PERCOLATION) {
            percolate(mae, s, initialRelations, mae.getInitialPercolationLevel(), false, null, 0);
        } else if (mae.getInitialPercolationRule().getPercolationMode() == InitialPercolationRule.FOCUS_NIMBUS_PERCOLATION) {
            percolate(mae, s, initialRelations, mae.getInitialPercolationLevel(), true, null, 0);
            percolate(mae, s, initialRelations, mae.getInitialPercolationLevel(), false, null, 0);
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

    // fix the focus issue (focus is not to be percolated)
    // fix problem: queries should be able to give params to set or so

    private void percolate(MatchedAetherEvent mae, Session s, List<Object[]> relations, int energy,
            boolean isFocusPercolation, PercolationStep previousStep, int threadLevel) {

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
                logger.debug("\t Recording percolation step with level " + energy);

                PercolationStep ps = buildPercolationStep(mae, mae.getObjectURL(), mae.getObjectURL(), pr, energy,
                        isFocusPercolation, previousStep, threadLevel);
                s.save(ps);
            }

            logger.debug("====== END FOCUS PERCOLATION ======");

        } else {

            logger.debug("Going to percolate along " + relations.size() + " discovered relations");

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
                    PercolationStep ps = buildPercolationStep(mae, (String) relation[0], (String) relation[2], pr,
                            relationEnergy, isFocusPercolation, previousStep, threadLevel);
                    s.save(ps);

                    // now we can set the threadPath
                    // basically this goes like parent-threadPath + parentId + thisUID
                    ps.setPercolationPath(previousStep == null ? ps.getId() + "_" + ps.getObjectURL() : previousStep.getPercolationPath() + "_"
                            + ps.getId() + "_" + ps.getObjectURL());

                    // and we also set the threadRoot
                    ps.setRoot(previousStep == null ? ps : previousStep.getRoot());

                    if (relationEnergy < MIN_ENERGY_LEVEL) {

                        logger.debug("\t Percolation of event " + mae.toString() + " stopped after relation "
                                + Arrays.toString(relation) + " due to insuffiscient energy");

                    } else {

                        // - get the next relations (use a RelationQuery for this)
                        List<Object[]> nextRelations = getNextRelations(relation, pr, ps, s);

                        // do it again, again, again
                        percolate(mae, s, nextRelations, relationEnergy, isFocusPercolation, ps, threadLevel + 1);

                    }
                }
            }
        }
    }

    /**
     * TODO refactor this in order to use the PercolationStep instead of the relation object...anyway previous relations
     * should be matched in a better fashion that there are now, like using fromURL+type+toURL and not a stupid
     * Integer...
     * 
     * @param relation
     * @param pr
     * @param ps
     * @param s
     * @return
     */
    private List<Object[]> getNextRelations(Object[] relation, PercolationRule pr, PercolationStep ps, Session s) {

        List<Object[]> res = new LinkedList<Object[]>();

        if (pr.getRelationQueries().size() > 0) {

            Map<String, Object> arguments = new HashMap<String, Object>();
            arguments.put("fromURL", ps.getObjectURL());
            arguments.put("rowName", getRowNameFromURL(ps.getPreviousURL()));
            arguments.put("percolationPath", ps.getPercolationPath());

            for (RelationQuery query : pr.getRelationQueries()) {
                res.addAll(executeRelationQueries(query, arguments, s));
            }

        } else {
            res
                    .addAll(s
                            .createQuery(
                                    "SELECT r.fromURL as fromURL, r.type as type, r.toURL as toURL, r.id as relationId from org.makumba.devel.relations.Relation r where r.fromURL = :fromURL")
                            .setString("fromURL", (String) relation[2]).list());
        }

        return res;
    }

    private List<String[]> executeRelationQueries(RelationQuery query, Map<String, Object> arguments, Session s) {
        String queryArguments = "fromURL";

        if (query.getArguments().length() > 0) {
            queryArguments = query.getArguments();
        }

        Query q = s.createQuery(query.getQuery());

        String args = ""; // for debug
        StringTokenizer st = new StringTokenizer(queryArguments, ",");
        while (st.hasMoreTokens()) {

            String t = st.nextToken().trim();

            Object value = arguments.get(t);
            if (value != null) {
                q.setParameter(t, value);
                args += t + "=" + value;
                if (st.hasMoreTokens())
                    args += ", ";
            }
        }

        logger.debug("Executing relation query: " + query + " with arguments " + args);
        return q.list();
    }

    /**
     * Gets all the relations of a matched event
     * 
     * @deprecated
     * 
     * TODO implement other types than just FILE
     * 
     * @param mae
     *            the {@link MatchedAetherEvent}
     * @param s
     *            a hibernate {@link Session}
     * @return a String array containing as fields: the fromURL, type and toURL of the relation
     */
    @Deprecated
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

    /**
     * @deprecated
     */
    @Deprecated
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
     * 
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
     * @param percolationLevel
     *            TODO
     * @param percolationPath
     *            TODO
     * @return a {@link PercolationStep} corresponding to this part of the percolation
     */
    private PercolationStep buildPercolationStep(MatchedAetherEvent mae, String previousURL, String objectURL,
            PercolationRule pr, int level, boolean isFocusPercolation, PercolationStep previous, int percolationLevel) {

        return new PercolationStep(previousURL, objectURL, mae.getUserGroup(), (isFocusPercolation ? level : 0),
                (!isFocusPercolation ? level : 0), pr, mae, previous, percolationLevel);

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
