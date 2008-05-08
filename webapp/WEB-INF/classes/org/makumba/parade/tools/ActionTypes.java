package org.makumba.parade.tools;

/**
 * ParaDe action types, for the Aether engine
 * 
 * @author Manuel Gay
 * 
 */
public enum ActionTypes {

    BROWSE_ROW("browseRow"),
    BROWSE_DIR("browseDir"),
    VIEW("view"),
    EDIT("edit"),
    SAVE("save"),
    DELETE("delete"),
    CVS_CHECK("cvsCheck"),
    CVS_UPDATE_DIR_LOCAL("cvsUpdateDirLocal"),
    CVS_UPDATE_DIR_RECURSIVE("cvsUpdateDirRecursive"),
    CVS_COMMIT("paradeCvsCommit"),
    CVS_DIFF("cvsDiff"),
    CVS_ADD("cvsAdd"),
    CVS_UPDATE_FILE("cvsUpdateFile"),
    CVS_OVERRIDE_FILE("cvsOverrideFile"),
    CVS_DELETE_FILE("cvsDeleteFile");

    private String action;

    ActionTypes(String action) {
        this.action = action;
    }

    public String action() {
        return this.action;
    }

}
