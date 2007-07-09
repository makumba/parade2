package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.makumba.parade.init.ParadeProperties;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowAnt;
import org.makumba.parade.view.interfaces.ParadeView;
import org.makumba.parade.view.interfaces.HeaderView;

import freemarker.template.SimpleHash;

public class AntViewManager implements ParadeView, HeaderView {

    public void setParadeViewHeader(List headers) {
        headers.add("Ant buildfile");
    }

    public void setParadeView(SimpleHash rowInformation, Row r) {
        SimpleHash antModel = new SimpleHash();
        RowAnt antdata = (RowAnt) r.getRowdata().get("ant");
        antModel.put("buildfile", antdata.getBuildfile());
        antModel.put("targets", antdata.getAllowedOperations());
        rowInformation.put("ant", antModel);
    }

    public void setHeaderView(SimpleHash root, Row r) {
        List allowedOps = ((RowAnt) r.getRowdata().get("ant")).getAllowedOperations();
        root.put("antTargets", allowedOps);
    }

}
