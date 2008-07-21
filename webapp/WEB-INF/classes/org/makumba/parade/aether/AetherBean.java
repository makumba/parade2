package org.makumba.parade.aether;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowWebapp;
import org.makumba.parade.model.managers.ServletContainer;

public class AetherBean {

    public String getResourceLink(String actionObject, boolean editLink) {

        String resourceLink = "";
        String context = ObjectTypes.rowNameFromURL(actionObject);
        String relativePath = ObjectTypes.pathFromFileOrDirURL(actionObject);

        
        switch (ObjectTypes.getObjectType(actionObject)) {

        case FILE:

            String webappPath = "";
            int status = 0;
            Session s = null;
            try {
                s = InitServlet.getSessionFactory().openSession();
                Transaction tx = s.beginTransaction();

                Row r = (Row) s.createQuery("from Row r where r.rowname = :context").setString("context",
                        context).uniqueResult();

                webappPath = r.getWebappPath();

                RowWebapp data = (RowWebapp) r.getRowdata().get("webapp");

                status = data.getStatus().intValue();

                tx.commit();

            } finally {
                if (s != null)
                    s.close();
            }

            boolean isContextRunning = status == ServletContainer.RUNNING;

            if (isContextRunning && !editLink) {

                if (actionObject.endsWith(".mdd")) {
                    resourceLink = "/"
                            + context
                            + (webappPath.length() > 0 ? "/" + webappPath : "")
                            + "/dataDefinitions/"
                            + ObjectTypes.objectNameFromURL(actionObject).substring(0,
                                    ObjectTypes.objectNameFromURL(actionObject).indexOf("."));
                } else if (actionObject.endsWith(".java")) {
                    resourceLink = "/" + context
                            + (webappPath.length() > 0 ? "/" + webappPath : "") + "/classes/"
                            + ObjectTypes.fileOrDirPathFromFileOrDirURL(actionObject).substring(0, "WEB-INF/classes/".length());

                } else {
                    resourceLink = "/" + context + "/"
                            + ObjectTypes.fileOrDirPathFromFileOrDirURL(actionObject)
                            + (actionObject.endsWith(".jsp") ? "x" : "");
                }
            } else {
                
                resourceLink = "/File.do?op=editFile&context=" + context + "&path="
                        + (webappPath.length() > 0 ? webappPath + "/" : "") + relativePath
                        + "&file=" + ObjectTypes.objectNameFromURL(actionObject) + "&editor=codepress";
            }

            break;

        case DIR:
            resourceLink = "/File.do?browse&display=file&context="+context + "&path=" + relativePath;
            break;

        case ROW:
            resourceLink = "/browse.jsp?context="+context;
            break;

        }
        return resourceLink;
    }

}
