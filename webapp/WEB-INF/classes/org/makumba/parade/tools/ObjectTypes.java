package org.makumba.parade.tools;

/**
 * ParaDe object types, for Aether engine
 * 
 * @author Manuel Gay
 * 
 */
public enum ObjectTypes {

    FILE("file"), USER("user"), ROW("row"), CVSFILE("cvsfile");

    private String type;

    ObjectTypes(String type) {
        this.type = type;
    }

    public String type() {
        return this.type;
    }

}
