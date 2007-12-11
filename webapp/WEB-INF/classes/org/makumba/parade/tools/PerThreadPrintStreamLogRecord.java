package org.makumba.parade.tools;

import java.util.Date;

public class PerThreadPrintStreamLogRecord {
    private Date date;
    
    private String message;

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
}