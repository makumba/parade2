package org.makumba.parade.model;

import java.util.HashMap;
import java.util.Map;

public class Row {

    public static final int AUTO_CVS_UPDATE_DISABLED = 10;

    public static final int AUTO_CVS_UPDATE_ENABLED = 20;
    
    private Long id;

    private String rowname;

    private String rowpath;

    private String description;

    private Map<String, File> files = new HashMap<String, File>();

    private Map<String, RowData> rowdata = new HashMap<String, RowData>();

    private Parade parade;

    private Application application;

    private User user;
    
    // 10 = No, 20 = Yes
    private int automaticCvsUpdate = AUTO_CVS_UPDATE_DISABLED;
    
    private boolean watchedByJNotify = true;
    
    private boolean moduleRow = false;

    
    public boolean getModuleRow() {
        return moduleRow;
    }

    public void setModuleRow(boolean moduleRow) {
        this.moduleRow = moduleRow;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void addManagerData(RowData data) {

        data.setRow(this);
        getRowdata().put(data.getDataType(), data);
    }

    public static Row getRow(Parade p, String context) {

        Row r = p.getRows().get(context);
        if (r == null)
            return null;
        else
            return r;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, File> getFiles() {
        return this.files;
    }

    public void setFiles(Map<String, File> files) {
        this.files = files;
    }

    public String getRowname() {
        return rowname;
    }

    public void setRowname(String rowname) {
        this.rowname = rowname;
    }

    public String getRowpath() {
        return rowpath.replace('/', java.io.File.separatorChar);
    }

    public void setRowpath(String rowpath) {
        this.rowpath = rowpath.replace(java.io.File.separatorChar, '/');
    }

    public Parade getParade() {
        return parade;
    }

    public void setParade(Parade parade) {
        this.parade = parade;
    }

    public Map<String, RowData> getRowdata() {
        return rowdata;
    }

    public void setRowdata(Map<String, RowData> rowdata) {
        this.rowdata = rowdata;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAutomaticCvsUpdate() {
        return automaticCvsUpdate;
    }

    public void setAutomaticCvsUpdate(int automaticCvsUpdate) {
        this.automaticCvsUpdate = automaticCvsUpdate;
    }

    public boolean getWatchedByJNotify() {
        return watchedByJNotify;
    }

    public void setWatchedByJNotify(boolean watchedByJNotify) {
        this.watchedByJNotify = watchedByJNotify;
    }
    
    public String toString() {
        return getRowname() + " - " + getDescription();
    }

}