package org.makumba.parade.aether;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.sun.accessibility.internal.resources.accessibility;

/**
 * ParaDe action types, for the Aether engine
 * 
 * @author Manuel Gay
 * 
 */
public enum ActionTypes {

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
