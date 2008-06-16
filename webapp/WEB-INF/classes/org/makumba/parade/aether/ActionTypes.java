package org.makumba.parade.aether;

import java.util.HashSet;
import java.util.Set;

/**
 * ParaDe action types, for the Aether engine
 * 
 * @author Manuel Gay
 * 
 */
public enum ActionTypes {

    LOGIN("login"),
    VIEW("view"),
    EXECUTE("execute"),
    EDIT("edit"),
    SAVE("save"),
    DELETE("delete"),
    CREATE("create"),
    CVS_CHECK("cvsCheck"),
    CVS_UPDATE_DIR_LOCAL("cvsUpdateDirLocal"),
    CVS_UPDATE_DIR_RECURSIVE("cvsUpdateDirRecursive"),
    CVS_COMMIT("paradeCvsCommit"),
    CVS_DIFF("cvsDiff"),
    CVS_ADD("cvsAdd"),
    CVS_UPDATE_FILE("cvsUpdateFile"),
    CVS_OVERRIDE_FILE("cvsOverrideFile"),
    CVS_DELETE_FILE("cvsDeleteFile"),
    WEBAPP_INSTALL("webappInstall"),
    WEBAPP_UNINSTALL("webappUninstall"),
    WEBAPP_REDEPLOY("webappRedeploy"),
    WEBAPP_RELOAD("webappReload"),
    WEBAPP_STOP("webappStop"),
    WEBAPP_START("webappStart")
    ;

    private String action;

    ActionTypes(String action) {
        this.action = action;
    }

    public String action() {
        return this.action;
    }
    
    public static Set<String> getActions() {
        ActionTypes[] v = values();
        Set<String> res = new HashSet<String>();
        for (int i = 0; i < v.length; i++) {
            res.add(v[i].action());
        }
        return res;
    }
    
    public static boolean isFileAction(String a) {
        return a.equals(VIEW.action()) || a.equals(EDIT.action()) || a.equals(SAVE.action()) || a.equals(DELETE.action());
    }

    

}
