package org.makumba.aether.percolation;

import org.hibernate.SessionFactory;
import org.makumba.aether.AetherEvent;
import org.makumba.aether.PercolationException;

public interface PercolationStrategy {

    public void percolate(AetherEvent e, boolean virtualPercolation, SessionFactory sessionFactory)
            throws PercolationException;

}
