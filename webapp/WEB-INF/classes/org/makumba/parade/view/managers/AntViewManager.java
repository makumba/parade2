package org.makumba.parade.view.managers;

import java.util.List;

import org.makumba.parade.model.Row;
import org.makumba.parade.view.interfaces.HeaderView;
import org.makumba.parade.view.interfaces.ParadeView;

import freemarker.template.SimpleHash;

public class AntViewManager implements ParadeView, HeaderView {

    public void setParadeViewHeader(List<String> headers) {
        headers.add("Ant buildfile");
    }

    public void setParadeView(SimpleHash rowInformation, Row r) {
        SimpleHash antModel = new SimpleHash();
        antModel.put("buildfile", r.getBuildfile());
        antModel.put("targets", r.getAllowedOperations());
        rowInformation.put("ant", antModel);
    }

    public void setHeaderView(SimpleHash root, Row r, String path) {
        List<String> allowedOps = r.getAllowedOperations();
        root.put("antTargets", allowedOps);
    }

}
