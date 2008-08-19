package org.makumba.parade.aether;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ParaDe action types, for the Aether engine
 * 
 * @author Manuel Gay
 * 
 */
public enum ActionTypes {

    LOGIN("login", "logged in"),
    VIEW("view", "looked at"),
    EXECUTE("execute", "executed"),
    EDIT("edit", "edited"),
    SAVE("save", "saved"),
    DELETE("delete", "deleted"),
    CREATE("create", "created"),
    CVS_CHECK("cvsCheck", "checked the CVS status"),
    CVS_UPDATE_DIR_LOCAL("cvsUpdateDirLocal", "performed a local CVS update"),
    CVS_UPDATE_DIR_RECURSIVE("cvsUpdateDirRecursive", "performed a recursive CVS update"),
    CVS_COMMIT("commit", "commited"),
    CVS_DIFF("cvsDiff", "made a CVS diff"),
    CVS_ADD("cvsAdd", "added to CVS"),
    CVS_UPDATE_FILE("cvsUpdateFile", "CVS updated"),
    CVS_OVERRIDE_FILE("cvsOverrideFile", "overrode"),
    CVS_DELETE_FILE("cvsDeleteFile", "deleted from CVS"),
    WEBAPP_INSTALL("webappInstall", "installed"),
    WEBAPP_UNINSTALL("webappUninstall", "uninstalled"),
    WEBAPP_REDEPLOY("webappRedeploy", "redeployed"),
    WEBAPP_RELOAD("webappReload", "reloaded"),
    WEBAPP_STOP("webappStop", "stopped"),
    WEBAPP_START("webappStart", "started");

    private String action;

    private String readableAction;

    ActionTypes(String action, String readableAction) {
        this.action = action;
        this.readableAction = readableAction;
    }

    public String action() {
        return this.action;
    }

    public String readableAction() {
        return this.readableAction;
    }

    public static Set<String> getActions() {
        ActionTypes[] v = values();
        Set<String> res = new HashSet<String>();
        for (ActionTypes element : v) {
            res.add(element.action());
        }
        return res;
    }

    private static Map<String, String> actionToReadableAction;

    private static Map<String, String> getActionToReadableAction() {

        if (actionToReadableAction == null) {

            ActionTypes[] v = values();
            Map<String, String> res = new HashMap<String, String>();
            for (ActionTypes element : v) {
                res.put(element.action(), element.readableAction());
            }

            actionToReadableAction = res;

        }
        return actionToReadableAction;
    }

    public static boolean isFileAction(String a) {
        return a.equals(VIEW.action()) || a.equals(EDIT.action()) || a.equals(SAVE.action())
                || a.equals(DELETE.action());
    }

    public static String getReadableAction(String action) {
        return getActionToReadableAction().get(action);
    }

}
