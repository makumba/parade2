package org.makumba.parade.model.interfaces;

import org.makumba.parade.model.Row;

public interface CacheRefresher {

    public void directoryRefresh(Row row, String path, boolean local);

    public void fileRefresh(Row row, String path);

}
