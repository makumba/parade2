package org.makumba.parade.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.aether.ObjectTypes;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.init.ParadeProperties;

/**
 * Representation of a ParaDe row, with loads of data.
 * 
 * @author Manuel Gay
 * 
 */
public class Row {

    public static final int AUTO_CVS_UPDATE_DISABLED = 10;

    public static final int AUTO_CVS_UPDATE_ENABLED = 20;

    public static String getModule(String fileURL) {
        // fetch cvs module of row
        Session s = null;
        String module = "";
        try {
            s = InitServlet.getSessionFactory().openSession();
            Transaction tx = s.beginTransaction();

            module = (String) s.createQuery("select module from Row where rowname = :context").setString(
                    "context", ObjectTypes.rowNameFromURL(fileURL)).uniqueResult();

            tx.commit();
        } finally {
            if (s != null)
                s.close();
        }
        return module;
    }

    public static Row getRow(Parade p, String context) {

        Row r = p.getRows().get(context);
        if (r == null)
            return null;
        else
            return r;
    }

    private Application application;

    // 10 = No, 20 = Yes
    private int automaticCvsUpdate = AUTO_CVS_UPDATE_DISABLED;

    private String branch;

    private String buildfile = "";

    private String contextname;

    private String cvsuser;

    private String db;

    private String description;

    private User externalUser;

    private Map<String, File> files = new HashMap<String, File>();

    private boolean hasMakumba;

    private Long id;

    private String module;

    private boolean moduleRow = false;

    private Parade parade;

    private String rowname;

    private String rowpath;

    private Integer status;

    private List<AntTarget> targets = new LinkedList<AntTarget>();

    private User user;

    private String version;

    private boolean watchedByJNotify = false;

    private String webappPath;

    public List<String> getAllowedOperations() {
        List<String> allowedTargets = new LinkedList<String>();

        for (String allowed : ParadeProperties.getElements("ant.displayedOps")) {
            for (AntTarget target : getTargets()) {
                if (target.getTarget().startsWith("#"))
                    target.setTarget(target.getTarget().substring(1));
                if (target.getTarget().equals(allowed)) {
                    allowedTargets.add(target.getTarget());
                }
            }
        }
        return allowedTargets;
    }

    public Application getApplication() {
        return application;
    }

    public int getAutomaticCvsUpdate() {
        return automaticCvsUpdate;
    }

    public String getBranch() {
        return branch;
    }

    public String getBuildfile() {
        return buildfile;
    }

    public String getContextname() {
        return contextname;
    }

    public String getCvsuser() {
        return cvsuser;
    }

    public String getDb() {
        return db;
    }

    public String getDescription() {
        return description;
    }

    public User getExternalUser() {
        return externalUser;
    }

    public Map<String, File> getFiles() {
        return this.files;
    }

    public boolean getHasMakumba() {
        return hasMakumba;
    }

    public Long getId() {
        return id;
    }

    public String getModule() {
        return module;
    }

    public boolean getModuleRow() {
        return moduleRow;
    }

    public Parade getParade() {
        return parade;
    }

    public String getRowname() {
        return rowname;
    }

    public String getRowpath() {
        return rowpath.replace('/', java.io.File.separatorChar);
    }

    public Integer getStatus() {
        return status;
    }

    public List<AntTarget> getTargets() {
        return targets;
    }

    public User getUser() {
        return user;
    }

    public String getVersion() {
        return version;
    }

    public boolean getWatchedByJNotify() {
        return this.watchedByJNotify;
    }

    public String getWebappPath() {
        return webappPath;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setAutomaticCvsUpdate(int automaticCvsUpdate) {
        this.automaticCvsUpdate = automaticCvsUpdate;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setBuildfile(String buildfile) {
        this.buildfile = buildfile;
    }

    public void setContextname(String name) {
        this.contextname = name;
    }

    public void setCvsuser(String cvsuser) {
        this.cvsuser = cvsuser;
    }

    public void setDb(String database) {
        this.db = database;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExternalUser(User externalUser) {
        this.externalUser = externalUser;
    }

    public void setFiles(Map<String, File> files) {
        this.files = files;
    }

    public void setHasMakumba(boolean hasMakumba) {
        this.hasMakumba = hasMakumba;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setModuleRow(boolean moduleRow) {
        this.moduleRow = moduleRow;
    }

    public void setParade(Parade parade) {
        this.parade = parade;
    }

    public void setRowname(String rowname) {
        this.rowname = rowname;
    }

    public void setRowpath(String rowpath) {
        this.rowpath = rowpath.replace(java.io.File.separatorChar, '/');
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setTargets(List<AntTarget> targets) {
        this.targets = targets;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setWatchedByJNotify(boolean watchedByJNotify) {
        this.watchedByJNotify = watchedByJNotify;
    }

    public void setWebappPath(String webappPath) {
        this.webappPath = webappPath;
    }

    @Override
    public String toString() {
        return getRowname() + " - " + getDescription();
    }

}