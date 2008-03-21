package org.makumba.parade.tools;

/**
 * A simple ParaDe exception, used to display errors to the user
 * 
 * @author Manuel Gay
 *
 */
public class ParadeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ParadeException(String msg) {
        super(msg);
    }

}
