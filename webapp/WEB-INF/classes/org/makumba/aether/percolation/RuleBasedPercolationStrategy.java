package org.makumba.aether.percolation;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.makumba.aether.AetherEvent;
import org.makumba.aether.PercolationException;
import org.makumba.aether.UserTypes;
import org.makumba.aether.model.ALE;
import org.makumba.aether.model.InitialPercolationRule;
import org.makumba.aether.model.MatchedAetherEvent;
import org.makumba.aether.model.PercolationRule;
import org.makumba.aether.model.PercolationStep;
import org.makumba.parade.aether.ActionTypes;
import org.makumba.parade.aether.ObjectTypes;

/**
 * Percolation strategy for a rule-based percolator
 * 
 * @author Manuel Gay
 * 
 */
public class RuleBasedPercolationStrategy implements PercolationStrategy {

    public void percolate(AetherEvent e, boolean virtualPercolation, SessionFactory sessionFactory)
            throws PercolationException {
        if (RuleBasedPercolator.rulesChanged) {
            configure();
        }
        RuleBasedPercolator.rulesChanged = false;

    }

    /**
     * Method to configure the percolation strategy, to be overwritten by subclasses if needed
     */
    protected void configure() {

    }

    /**
     * Builds a {@link MatchedAetherEvent} from a {@link AetherEvent} and a {@link InitialPercolationRule} it was
     * matched against.
     * 
     * @param e
     *            the {@link AetherEvent} to match
     * @param ipr
     *            the {@link InitialPercolationRule} to match it against
     * @param virtualPercolation
     *            TODO
     * @param s
     *            a hibernate {@link Session} useful to do queries
     * @return a {@link MatchedAetherEvent} if anything was matched, null otherwise
     */
    protected MatchedAetherEvent buildMatchedAetherEvent(AetherEvent e, InitialPercolationRule ipr,
            boolean virtualPercolation, Session s) {

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
        if (ipr.getUserType().equals(UserTypes.OWNER.type())) {

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
        if (ipr.getUserType().equals(UserTypes.ACTOR.type())) {
            userGroup = e.getActor();
        }

        if (userGroup.length() > 0) {
            return new MatchedAetherEvent(e, userGroup, ipr, virtualPercolation);
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
     * @param virtualPercolation
     *            TODO
     * @param percolationPath
     *            TODO
     * @return a {@link PercolationStep} corresponding to this part of the percolation
     */
    protected static PercolationStep buildPercolationStep(MatchedAetherEvent mae, String previousURL, String objectURL,
            PercolationRule pr, int level, boolean isFocusPercolation, PercolationStep previous, int percolationLevel,
            boolean virtualPercolation) {

        return new PercolationStep(previousURL, objectURL, isFocusPercolation ? mae.getActor() : mae.getUserGroup(),
                (isFocusPercolation ? level : 0), (!isFocusPercolation ? level : 0), pr, mae, previous,
                percolationLevel, virtualPercolation);

    }

    /**
     * Adds or updates the total focus of an object for a user
     * 
     * @param objectURL
     *            the URL to the object
     * @param user
     *            the user
     * @param energy
     *            the energy to be added
     * @param virtualPercolation
     *            TODO
     * @param s
     *            a Hibernate session
     */
    protected static void addOrUpdateFocus(String objectURL, String user, int energy, boolean virtualPercolation,
            Session s) {

        synchronized (RuleBasedPercolator.mutex) {

            if (!virtualPercolation) {

                int updated = s.createQuery(
                        "update ALE set focus = focus + :energy where user = :user and objectURL = :objectURL")
                        .setString("user", user).setString("objectURL", objectURL).setParameter("energy", energy)
                        .executeUpdate();
                if (updated == 0) {
                    ALE f = new ALE(objectURL, user, energy, 0);
                    s.save(f);
                }
            } else {
                int updated = s.createQuery(
                        "update ALE set virtualFocus = focus + :energy where user = :user and objectURL = :objectURL")
                        .setString("user", user).setString("objectURL", objectURL).setParameter("energy", energy)
                        .executeUpdate();
                if (updated == 0) {
                    ALE f = new ALE(objectURL, user);
                    f.setVirtualFocus(energy);
                    s.save(f);

                }
            }
            RuleBasedPercolator.mutex.notifyAll();
        }

    }

    /**
     * Adds or updates the total nimbus of an object for a user
     * 
     * @param objectURL
     *            the URL to the object
     * @param userGroup
     *            the userGroup this nimbus update affects
     * @param energy
     *            the energy to be added
     * @param virtualPercolation
     *            TODO
     * @param s
     *            a Hibernate session
     */
    protected static void addOrUpdateNimbus(String objectURL, String userGroup, int energy, boolean virtualPercolation,
            Session s) {

        synchronized (RuleBasedPercolator.mutex) {

            if (userGroup.indexOf("*") > -1) {
                String minusUser = "";
                if (userGroup.indexOf("-") > -1) {
                    minusUser = userGroup.substring(userGroup.indexOf("-") + 1);
                }

                if (!virtualPercolation) {

                    int updated = s
                            .createQuery(
                                    "update ALE set nimbus = nimbus + :energy where user != :minusUser and objectURL = :objectURL")
                            .setString("minusUser", minusUser).setString("objectURL", objectURL).setParameter("energy",
                                    energy).executeUpdate();
                    if (updated == 0) {
                        List<String> users = s.createQuery("select login from User u where u.login != :minusUser")
                                .setString("minusUser", minusUser).list();
                        for (String login : users) {
                            ALE f = new ALE(objectURL, login, 0, energy);
                            s.save(f);
                        }
                    }
                } else {
                    int updated = s
                            .createQuery(
                                    "update ALE set virtualNimbus = nimbus + :energy where user != :minusUser and objectURL = :objectURL")
                            .setString("minusUser", minusUser).setString("objectURL", objectURL).setParameter("energy",
                                    energy).executeUpdate();
                    if (updated == 0) {
                        List<String> users = s.createQuery("select login from User u where u.login != :minusUser")
                                .setString("minusUser", minusUser).list();
                        for (String login : users) {
                            ALE f = new ALE(objectURL, login);
                            f.setVirtualNimbus(energy);
                            s.save(f);
                        }
                    }
                }
            }
            RuleBasedPercolator.mutex.notifyAll();
        }
    }
}
