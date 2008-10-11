package org.makumba.parade.view.managers;

import java.util.List;

import org.makumba.parade.model.Row;
import org.makumba.parade.view.interfaces.ParadeView;

import freemarker.template.SimpleHash;

public class MakumbaViewManager implements ParadeView {

    public void setParadeViewHeader(List<String> headers) {
        headers.add("Makumba version");
    }

    public void setParadeView(SimpleHash rowInformation, Row r) {
        SimpleHash makModel = new SimpleHash();

        makModel.put("version", r.getVersion());
        makModel.put("versionError", r.getVersion().startsWith("Error"));
        makModel.put("database", r.getDb());
        rowInformation.put("mak", makModel);
    }

}
