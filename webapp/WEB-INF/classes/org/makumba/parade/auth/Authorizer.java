package org.makumba.parade.auth;

public interface Authorizer {
    boolean auth(String username, String password);
}
