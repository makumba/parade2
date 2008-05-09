package org.makumba.aether.percolation;

import org.hibernate.SessionFactory;
import org.makumba.aether.AetherEvent;
import org.makumba.aether.PercolationException;

/**
 * A percolator, that  given an event performs percolation
 * 
 * @author Manuel Gay
 *
 */
public interface Percolator {
    
    public void configure(String databaseName, SessionFactory sessionFactory);
    
    public void percolate(AetherEvent e) throws PercolationException;
    
    
}
