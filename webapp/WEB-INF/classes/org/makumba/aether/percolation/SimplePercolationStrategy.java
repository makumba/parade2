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
import org.makumba.parade.aether.ActionTypes;
import org.makumba.parade.aether.ObjectTypes;

public class SimplePercolationStrategy implements PercolationStrategy {

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
                    matchedEvents.add(match(e, ipr, s));
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

        List<String[]> allRelations = getAllRelations(mae, s);

        if (allRelations == null) {
            return;
        }

        for (String[] relation : allRelations) {
            System.out.println(Arrays.toString(relation));
        }

    }

    /**
     * Gets all the relations of a matched event
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
                        "SELECT r.rowname + f.path.substring(length(r.rowpath), length(f.path)), :type, f.cvsURI FROM File f JOIN f.row r WHERE (r.rowname + f.path.substring(length(r.rowpath), length(f.path))) = :fromURL")
                .setString("fromURL", mae.getObjectURL()).setParameter("type", "versionOf").list();
    }

    private MatchedAetherEvent match(AetherEvent e, InitialPercolationRule ipr, Session s) {

        String userGroup = "";

        if (ipr.getUserType().equals(UserTypes.ALL.type())) {
            userGroup = "*";
        }
        if (ipr.getUserType().equals(UserTypes.ALL_BUT_ACTOR.type())) {
            userGroup = "*,-" + e.getUser();
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
            userGroup = e.getUser();
        }

        if (userGroup.length() > 0) {
            return new MatchedAetherEvent(e, ipr.getInitialLevel(), userGroup);
        }

        return null;
    }

}
