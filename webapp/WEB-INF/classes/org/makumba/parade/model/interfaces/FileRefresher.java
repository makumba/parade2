package org.makumba.parade.model.interfaces;

import org.makumba.parade.model.File;
import org.makumba.parade.model.Row;

/**
 * Refreshes the cache of {@link File}-s
 * 
 * @author Manuel Gay
 * 
 */
public interface FileRefresher {

    /**
     * Refreshes the cache for a given directory
     * 
     * @param row
     *            the absolute {@link Row} this directory belongs to
     * @param path
     *            the path to the directory
     * @param local
     *            <code>true</code> if the changes should be local, <code>false</code> if they should spread to
     *            sub-directories
     */
    public void directoryRefresh(Row row, String path, boolean local);

    /**
     * Refreshes the cache for a single file
     * 
     * @param row
     *            the {@link Row} this file belongs to
     * @param path
     *            the absolute path to the file
     */
    public void fileRefresh(Row row, String path);

}
