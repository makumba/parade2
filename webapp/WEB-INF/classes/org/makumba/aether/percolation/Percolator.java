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
    
    public void configure(SessionFactory sessionFactory);
    
    public void percolate(AetherEvent e, boolean virtualPercolation) throws PercolationException;
    
    public int getALE(String objectURL, String user);
    
    public void cleanVirtualPercolations();
    
    
}
