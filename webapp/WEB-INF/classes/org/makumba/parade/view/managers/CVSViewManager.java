package org.makumba.parade.view.managers;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Hibernate;
import org.makumba.parade.init.ParadeProperties;
import org.makumba.parade.model.File;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.CVSManager;
import org.makumba.parade.tools.CVSRevisionComparator;
import org.makumba.parade.tools.ParadeLogger;
import org.makumba.parade.view.interfaces.ParadeView;

import freemarker.template.SimpleHash;

public class CVSViewManager implements ParadeView {

    static Logger logger = ParadeLogger.getParadeLogger(CVSViewManager.class.getName());

    private CVSRevisionComparator c = new CVSRevisionComparator();

    public void setParadeViewHeader(List<String> headers) {
        headers.add("CVS user");
        headers.add("module");
        headers.add("branch");
    }

    public void setParadeView(SimpleHash rowInformation, Row r) {
        SimpleHash cvsModel = new SimpleHash();

        cvsModel.put("user", r.getCvsuser() == null ? "" : r.getCvsuser());
        cvsModel.put("module", r.getModule() == null ? "" : r.getModule());
        cvsModel.put("branch", r.getBranch() == null ? "" : r.getBranch());

        rowInformation.put("cvs", cvsModel);

    }

    public void setFileView(SimpleHash fileView, Row r, String path, File f) {

        String cvsweb = ParadeProperties.getParadeProperty("cvs.site");
        String webPath = f.getPath().substring(r.getRowpath().length() + 1).replace(java.io.File.separatorChar, '/');
        String cvswebLink = cvsweb + r.getModule() + "/" + webPath;

        // populating model
        fileView.put("cvsWebLink", cvswebLink);
        fileView.put("cvsIsNull", f.getCvsStatus() == null);
        fileView.put("isConflictBackup", f.getName().startsWith(".#"));
        fileView.put("cvsRevision", f.getCvsRevision() == null ? "" : f.getCvsRevision());
        fileView.put("cvsCheckedOutRevision", f.getCvsCheckedOutRevision() == null ? "" : f.getCvsCheckedOutRevision());
        fileView.put("cvsStatus", f.getCvsStatus() == null ? "" : f.getCvsStatus());

        // let's see if there's a newer version of this on the repository
        boolean newerExists = false;
        String repositoryRevision = "", rowRevision = "";

        if (r.getApplication() != null) {
            Hibernate.initialize(r.getApplication().getCvsfiles());
            repositoryRevision = r.getApplication().getCvsfiles().get(f.getCvsPath());
            rowRevision = f.getCvsRevision();

            if (repositoryRevision != null && rowRevision != null) {

                if (repositoryRevision.equals("1.1.1.1")) {
                    repositoryRevision = "1.1";
                }
                if (rowRevision.equals("1.1.1.1")) {
                    rowRevision = "1.1";
                }

                newerExists = c.compare(repositoryRevision, rowRevision) == 1;

            } else {
                fileView.put("cvsNewerExists", false);
            }
        }

        fileView.put("cvsNewerExists", newerExists);

        if (newerExists) {
            fileView.put("cvsConflictOnUpdate", CVSManager.cvsConflictOnUpdate(f));
            fileView.put("cvsNewRevision", repositoryRevision == null ? "" : repositoryRevision);
        }
    }
}