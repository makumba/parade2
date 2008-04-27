package org.makumba.parade.tools;

/**
 * This exception occurs when a user logs in and the system can't figure out who it is because it finds multiple
 * records.
 * 
 * @author Manuel Gay
 * 
 */
public class MultipleUsersException extends ParadeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MultipleUsersException(String msg) {
        super(msg);
    }

}
