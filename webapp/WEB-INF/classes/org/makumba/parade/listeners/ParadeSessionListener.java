package org.makumba.parade.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * This listener triggers an event each time a HTTP session is created or destroyed
 * 
 * TODO actually implement it
 * 
 * @author Manuel Gay
 * 
 */
public class ParadeSessionListener implements HttpSessionListener {

    private static Map<String, HttpSession> activeSessions = new HashMap<String, HttpSession>();

    public void sessionCreated(HttpSessionEvent e) {
        activeSessions.put(e.getSession().getId(), e.getSession());
    }

    public void sessionDestroyed(HttpSessionEvent e) {
        activeSessions.remove(e.getSession().getId());
    }

    public static synchronized List<HttpSession> getActiveSessions() {
        List<HttpSession> sessions = new LinkedList<HttpSession>();
        Iterator<String> it = activeSessions.keySet().iterator();
        while (it.hasNext()) {
            HttpSession s = activeSessions.get(it.next());
            try {
                s.getAttribute("org.makumba.parade.user"); // check if the session is still valid
                sessions.add(s);
            } catch (java.lang.IllegalStateException e) {
                // this session is invalidated, we need to remove it
                activeSessions.remove(s.getId());
            }

        }
        return sessions;
    }

    public static synchronized List<String[]> getActiveSessionUsers() {
        List<String[]> onlineUsers = new LinkedList<String[]>();

        // hashtable for filtering expired sessions
        Hashtable<String, String> online = new Hashtable<String, String>();

        Iterator<String> it = activeSessions.keySet().iterator();
        while (it.hasNext()) {
            HttpSession s = activeSessions.get(it.next());
            try {
                String login = (String) s.getAttribute("user_login");
                String nickName = (String) s.getAttribute("user_nickname");
                if (login != null && login.length() > 0) {
                    online.put(login, nickName );
                }
            } catch (java.lang.IllegalStateException e) {
                // this session is invalidated, we need to remove it
                activeSessions.remove(s.getId());
            }
        }

        for (String key : online.keySet()) {
            onlineUsers.add(new String[] {key, online.get(key)});
        }

        return onlineUsers;
    }
}