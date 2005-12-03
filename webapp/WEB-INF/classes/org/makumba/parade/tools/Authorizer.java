package org.makumba.parade.tools;

public interface Authorizer {
    boolean auth(String username, String password);
}
