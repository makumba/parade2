package org.makumba.parade.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.makumba.parade.init.ParadeProperties;

public class RowAnt extends AbstractRowData implements RowData {

    private Long id;

    private Long lastmodified;

    private String buildfile = "";

    private List<String> targets = new LinkedList<String>();

    public String getBuildfile() {
        return buildfile;
    }

    public void setBuildfile(String buildfile) {
        this.buildfile = buildfile;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(Long lastmodified) {
        this.lastmodified = lastmodified;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public List<String> getAllowedOperations() {
        List<String> allowedTargets = new LinkedList<String>();

        for (Iterator<String> i = ParadeProperties.getElements("ant.displayedOps").iterator(); i.hasNext();) {
            String allowed = i.next();
            for (Iterator<String> j = getTargets().iterator(); j.hasNext();) {
                String target = j.next();
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
