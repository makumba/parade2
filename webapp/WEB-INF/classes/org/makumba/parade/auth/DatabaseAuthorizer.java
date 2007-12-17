package org.makumba.parade.auth;


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
