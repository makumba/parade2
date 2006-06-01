package org.makumba.parade.model;

import java.util.Date;

public class Log {
    
    private Long id;
    
    public Long identifier;
    public Date date;
    public String priority;
    public String category;
    public String thread;
    public String message;
    public String throwable;
    public String ndc;
    public String mdc;
    public String mdc2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
 

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getMdc() {
        return mdc;
    }

    public void setMdc(String mdc) {
        this.mdc = mdc;
    }

    public String getMdc2() {
        return mdc2;
    }

    public void setMdc2(String mdc2) {
        this.mdc2 = mdc2;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNdc() {
        return ndc;
    }

    public void setNdc(String ndc) {
        this.ndc = ndc;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getThrowable() {
        return throwable;
    }

    public void setThrowable(String throwable) {
        this.throwable = throwable;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    

}
