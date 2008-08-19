package org.makumba.aether;

import java.util.HashSet;
import java.util.Set;

public enum UserTypes {

    NONE("none"), ALL("all"), ACTOR("actor"), OWNER("owner"), ALL_BUT_OWNER("all_but_owner"), ALL_BUT_ACTOR(
            "all_but_actor");

    private String type;

    UserTypes(String type) {
        this.type = type;
    }

    public String type() {
        return this.type;
    }

    public static Set<String> getUserTypes() {
        UserTypes[] v = values();
        Set<String> res = new HashSet<String>();
        for (UserTypes element : v) {
            res.add(element.type());
        }
        return res;
    }

}
