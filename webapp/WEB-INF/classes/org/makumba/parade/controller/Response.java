package org.makumba.parade.controller;

/**
 * 
 * @author Joao Andrade
 * 
 *         Responsible for outputting correctly formated messages to the ActionForward layer
 * 
 */
public class Response {

    private String message;

    public Response(String message) {
        this.setMessage(message);
    }

    public Boolean isSuccess() {
        return !getMessage().startsWith("Error");
    }

    private void setMessage(String message) {
        this.message = message;
    }
    
    public void appendMessage(String message) {
        this.message += message;
    }

    public String getMessage() {
        return message;
    }
}
