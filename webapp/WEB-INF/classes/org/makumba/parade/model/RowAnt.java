package org.makumba.parade.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.makumba.parade.init.ParadeProperties;

public class RowAnt extends AbstractRowData implements RowData {

    private Long id;

    private Long lastmodified;

    private String buildfile = "";

    private List targets = new LinkedList();

    public String getBuildfile() {
        return buildfile;
    }

    public void setBuildfile(String buildfile) {
        this.buildfile = buildfile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(Long lastmodified) {
        this.lastmodified = lastmodified;
    }

    public List getTargets() {
        return targets;
    }

    public void setTargets(List targets) {
        this.targets = targets;
    }
    
    public List getAllowedOperations() {
        List allowedTargets = new LinkedList();
        
        for (Iterator i = ParadeProperties.getElements("ant.displayedOps").iterator(); i.hasNext();) {
            String allowed = (String) i.next();
            for (Iterator j = getTargets().iterator(); j.hasNext();) {
                String target = (String) j.next();
                if (target.startsWith("#"))
                    target = target.substring(1);
                if (!target.equals(allowed))
                    continue;
                allowedTargets.add(target);
            }
        }
        return allowedTargets;
    }

}
