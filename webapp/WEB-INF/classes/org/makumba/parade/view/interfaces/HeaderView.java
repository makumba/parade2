package org.makumba.parade.view.interfaces;

import org.makumba.parade.model.Row;

import freemarker.template.SimpleHash;

public interface HeaderView {

    public void setHeaderView(SimpleHash root, Row r, String path);

}
