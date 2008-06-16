package org.makumba.parade.model.interfaces;

import org.makumba.parade.model.Row;

/**
 * Interface for modules that work on row data (File, CVS, Makumba, Webapp, Ant, ...)
 * 
 * @author Manuel Gay
 * 
 */
public interface RowRefresher {

    /**
     * Performs hard refresh, at deep level
     * 
     * @param row
     *            the {@link Row} to refresh
     */
    public void hardRefresh(Row row);

    /**
     * Performs soft refresh, for light data refresh
     * 
     * @param row
     *            the {@link Row} to refresh
     */
    public void softRefresh(Row row);

}
