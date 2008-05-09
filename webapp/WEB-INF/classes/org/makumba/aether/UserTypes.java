package org.makumba.aether;

import java.util.HashSet;
import java.util.Set;

import org.makumba.parade.aether.ActionTypes;

public enum UserTypes {
    
    ALL(10),
    OWNER(20),
    ALL_BUT_OWNER(30);
    
    private int type;

    UserTypes(int type) {
        this.type = type;
    }
    
    public int type() {
        return this.type;
    }

    public static Set<Integer> getUserTypes() {
        UserTypes[] v = values();
        Set<Integer> res = new HashSet<Integer>();
        for (int i = 0; i < v.length; i++) {
            res.add(v[i].type());
        }
        return res;
    }
    
}
