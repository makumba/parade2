package org.makumba.parade.view.managers;

import org.makumba.parade.model.Row;
import org.makumba.parade.view.interfaces.ParadeView;

public class RowStoreViewManager implements ParadeView {

    public String getParadeViewHeader() {
        String header = "<th>Name, Path</th><th>Description</th>";
        return header;
    }

    public String getParadeView(Row r) {
        String view = "<a href='/browse.jsp?context=" + r.getRowname() + "'>" + r.getRowname() + "</a> " + "<a href='"
                + r.getRowname() + "'>(Surf)</a>" + "<br><font size='-1'>" + r.getRowpath() + "</font>" + "</td><td>"
                + r.getDescription();
        return view;
    }

}
