package org.makumba.aether;

/**
 * A percolator, that performs percolation given an event
 * @author Manuel Gay
 *
 */
public interface Percolator {

    public void percolate(AetherEvent e) throws PercolationException;
}
