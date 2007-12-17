package org.makumba.parade.tools;

/**
 * This dummy {@link Authorizer} makes it possible to log-in with any username and password combination.
 * 
 * @author Manuel Gay
 * 
 */
public class DummyAuthorizer implements Authorizer {

    public boolean auth(String username, String password) {

        return true;

    }

}
