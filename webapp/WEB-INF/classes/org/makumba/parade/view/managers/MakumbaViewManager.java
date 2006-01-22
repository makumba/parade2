package org.makumba.parade.view.managers;

import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowMakumba;
import org.makumba.parade.view.interfaces.ParadeView;

public class MakumbaViewManager implements ParadeView {

    public String getParadeViewHeader() {
        String header = "<b>Makumba version</b>";
        return header;
    }

    public String getParadeView(Row r) {
        RowMakumba makdata = (RowMakumba) r.getRowdata().get("makumba");

        String view = makdata.getVersion();
        return view;
    }

}
