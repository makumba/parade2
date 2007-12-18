package org.makumba.parade.auth;

/**
 * An {@link Authorizer} that stores a database name. Meant to be overriden by a mechanism that uses the database name
 * 
 * @author Cristian Bogdan
 * 
 */
public class DatabaseAuthorizer implements Authorizer {

    String database;

    public void setDatabase(String s) {
        database = s;
    }

    public String getDatabase() {
        return database;
    }

    public boolean auth(String username, String pass) {
        return true;
    }
}
