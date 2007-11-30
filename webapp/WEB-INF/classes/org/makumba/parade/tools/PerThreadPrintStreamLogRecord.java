package org.makumba.parade.tools;

import java.util.Date;

public class PerThreadPrintStreamLogRecord {
    private Date date;
    
    private String message;
    
    private boolean notThroughAccess;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isNotThroughAccess() {
        return notThroughAccess;
    }

    public void setNotThroughAccess(boolean notThroughAccess) {
        this.notThroughAccess = notThroughAccess;
    }
}