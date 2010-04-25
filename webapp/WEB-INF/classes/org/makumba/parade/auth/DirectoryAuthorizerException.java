package org.makumba.parade.auth;

/**
 * An exception that characterises problems with the directory authorisation mechanism
 * 
 * @author Manuel Gay
 * 
 */
public class DirectoryAuthorizerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DirectoryAuthorizerException(String msg) {
        super(msg);
    }

}
