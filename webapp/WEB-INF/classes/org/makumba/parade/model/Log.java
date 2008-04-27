package org.makumba.parade.model;

import java.util.Date;

public class Log {

    private Long id;

    private Date date;

    private String level;

    private String message;

    private Throwable throwable;

    private String origin;

    private ActionLog actionLog;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public ActionLog getActionLog() {
        return actionLog;
    }

    public void setActionLog(ActionLog actionLog) {
        this.actionLog = actionLog;
    }

}
