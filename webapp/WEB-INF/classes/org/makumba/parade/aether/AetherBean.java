package org.makumba.parade.aether;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowWebapp;
import org.makumba.parade.model.managers.ServletContainer;

public class AetherBean {

    public boolean isContextRunning(String context) {
        int status = 0;
        Session s = null;
        try {
            s = InitServlet.getSessionFactory().openSession();
            Transaction tx = s.beginTransaction();

            Row r = (Row) s.createQuery("from Row r where r.rowname = :context").setString("context", context)
                    .uniqueResult();

            RowWebapp data = (RowWebapp) r.getRowdata().get("webapp");

            status = data.getStatus().intValue();

            tx.commit();

        } finally {
            if (s != null)
                s.close();
        }

        return status == ServletContainer.RUNNING;
    }

    public String getResourceLink(String actionObject, boolean editLink) {
        String resourceLink = "";
        if (isContextRunning(ObjectTypes.rowNameFromURL(actionObject)) && !editLink) {

            if (actionObject.endsWith(".mdd")) {
                resourceLink = "/"
                        + ObjectTypes.rowNameFromURL(actionObject)
                        + "/dataDefinitions/"
                        + ObjectTypes.objectNameFromURL(actionObject).substring(0,
                                ObjectTypes.objectNameFromURL(actionObject).indexOf("."));
            } else if (actionObject.endsWith(".java")) {
                resourceLink = "/" + ObjectTypes.rowNameFromURL(actionObject) + "/classes/"
                        + ObjectTypes.filePathFromFileURL(actionObject).substring(0, "WEB-INF/classes/".length());

            } else {
                resourceLink = "/" + ObjectTypes.rowNameFromURL(actionObject) + "/"
                        + ObjectTypes.filePathFromFileURL(actionObject) + (actionObject.endsWith(".jsp") ? "x" : "");
            }
        } else {
            resourceLink = "/File.do?op=editFile&context=" + ObjectTypes.rowNameFromURL(actionObject) + "&path="
                    + ObjectTypes.pathFromFileURL(actionObject) + "&file="
                    + ObjectTypes.objectNameFromURL(actionObject) + "&editor=codepress";
        }

        return resourceLink;
    }

}
