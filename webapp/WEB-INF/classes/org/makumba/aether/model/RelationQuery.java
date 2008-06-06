package org.makumba.aether.model;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.makumba.aether.percolation.SimplePercolationStrategy;

public class RelationQuery {
    
    private Logger logger = Logger.getLogger(RelationQuery.class);
    
    private long id;
    
    private String query;
    
    private String description;
    
    private String arguments;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    
    public RelationQuery() {
        
    }
    
    public String toString() {
        return query;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public List<String[]> execute(SimplePercolationStrategy simplePercolationStrategy, Map<String, Object> arguments, Session s) {
        String queryArguments = "fromURL";
    
        if (getArguments().length() > 0) {
            queryArguments = getArguments();
        }
    
        Query q = s.createQuery(getQuery());
    
        String args = ""; // for debug
        StringTokenizer st = new StringTokenizer(queryArguments, ",");
        while (st.hasMoreTokens()) {
    
            String t = st.nextToken().trim();
    
            Object value = arguments.get(t);
            if (value != null) {
                q.setParameter(t, value);
                args += t + "=" + value;
                if (st.hasMoreTokens())
                    args += ", ";
            }
        }
        
        logger.debug("Executing relation query: " + this + " with arguments " + args);
    
        return q.list();
    }

}
