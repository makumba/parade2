package org.makumba.parade.view;

import com.opensymphony.oscache.base.CacheEntry;

/**
 * ParaDe cache refresh policy for view cache.<br>
 * All the actions that change the row data should call the <{@link ParadeRefreshPolicy#setRowCacheStale(boolean)}
 * method
 * 
 * 
 * @author Manuel Gay
 * 
 */
public class ParadeRefreshPolicy implements com.opensymphony.oscache.web.WebEntryRefreshPolicy {

    private static final long serialVersionUID = 1L;

    private static boolean rowCacheStale = false;

    public static final String ROW_CACHE_KEY = "org.makumba.parade.rowCache";

    public static void setRowCacheStale(boolean stale) {
        rowCacheStale = stale;
    }

    public void init(String arg0, String arg1) {

    }

    public boolean needsRefresh(CacheEntry arg0) {

        if (arg0.getKey().equals(ROW_CACHE_KEY)) {
            if (rowCacheStale) {
                rowCacheStale = false;
                return true;
            }
        }

        return false;
    }

}