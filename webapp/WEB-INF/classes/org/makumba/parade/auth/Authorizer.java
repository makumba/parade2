package org.makumba.parade.auth;

/**
 * Interface for an authorizer, used by the AccessServlet to perform standard login
 * 
 * @author Cristian Bogdan
 *
 */
public interface Authorizer {
    
    /**
     * Checks whether the user is authenticated against the creditentials or not.
     * @param username the username
     * @param password the password
     * @return <code>true</code> if authenticated, <code>false</code> otherwise
     */
    boolean auth(String username, String password);
    
}
