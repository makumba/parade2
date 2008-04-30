package org.makumba.parade.view.managers;

import java.util.List;

import org.makumba.parade.model.Row;
import org.makumba.parade.view.interfaces.ParadeView;

import freemarker.template.SimpleHash;

public class RowStoreViewManager implements ParadeView {

    public void setParadeViewHeader(List<String> headers) {
        headers.add("Name, Path");
        headers.add("Description");
    }

    public void setParadeView(SimpleHash rowInformation, Row r) {
        SimpleHash rowModel = new SimpleHash();

        rowModel.put("rowname", r.getRowname());
        rowModel.put("rowpath", r.getRowpath());
        rowModel.put("rowdescription", r.getDescription());
        rowModel.put("watchedByJNotify", r.getWatchedByJNotify());

        rowInformation.put("rowstore", rowModel);

    }

}
