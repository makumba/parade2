package org.makumba.parade.view.interfaces;

import java.util.List;

import org.makumba.parade.model.Row;

import freemarker.template.SimpleHash;

public interface ParadeView {

    public void setParadeViewHeader(List<String> headers);

    public void setParadeView(SimpleHash rowInformation, Row r);

}
