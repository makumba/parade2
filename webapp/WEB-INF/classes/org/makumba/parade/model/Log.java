package org.makumba.parade.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Log {

    private Long id;

    private Date logDate;

    private String level;

    private String message;

    private Throwable throwable;

    private String origin;

    private ActionLog actionLog;

    @Column
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Id @GeneratedValue
    @Column(name="log")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(columnDefinition="longtext")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column
    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Column
    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date date) {
        this.logDate = date;
    }
    
    @Column
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @ManyToOne(optional=false)
    public ActionLog getActionLog() {
        return actionLog;
    }

    public void setActionLog(ActionLog actionLog) {
        this.actionLog = actionLog;
    }

}
