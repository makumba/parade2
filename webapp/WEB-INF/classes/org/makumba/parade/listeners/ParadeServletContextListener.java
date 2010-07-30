package org.makumba.parade.listeners;

import javax.servlet.ServletContextEvent;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

import org.makumba.parade.model.Parade;

/**
 * Servlet listener that unloads JNotify watches on parade unloading
 * 
 * @author Manuel Gay
 * 
 */
public class ParadeServletContextListener implements javax.servlet.ServletContextListener {

    public void contextDestroyed(ServletContextEvent arg0) {

        for (String key : Parade.JNotifyWatches.keySet()) {
            try {
                JNotify.removeWatch(Parade.JNotifyWatches.get(key));
            } catch (JNotifyException e) {
                // die silently
            }
        }

    }

    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Auto-generated method stub

    }

}
