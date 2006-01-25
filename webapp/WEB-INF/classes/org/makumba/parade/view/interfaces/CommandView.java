package org.makumba.parade.view.interfaces;

import org.makumba.parade.model.Row;

public interface CommandView {

    public String getCommandView(String view, Row r, String path, String file, String opResult);

}
