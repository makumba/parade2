package org.makumba.parade.model.interfaces;

import java.util.Map;

import org.makumba.parade.model.Row;

public interface ParadeManager {

    /**
     * Performs actions needed for a new {@link Row}
     * 
     * @param name
     *            the name of the row
     * @param r
     *            the {@link Row} to be registered or otherwise populated
     * @param m
     *            a Map containing the row definitions from the rows.properties file ({@see RowProperties})
     * 
     */
    public void newRow(String name, Row r, Map<String, String> m);

}
