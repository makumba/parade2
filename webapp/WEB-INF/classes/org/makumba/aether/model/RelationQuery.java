package org.makumba.aether.model;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.makumba.providers.QueryAnalysisProvider;
import org.makumba.providers.QueryProvider;

public class RelationQuery {
    
    private Logger logger = Logger.getLogger(RelationQuery.class);
    
    private static int executedQueries = 0;
    
    private long id;
    
    private String query;
    
    private String description;
    
    private String arguments;
    
    private QueryAnalysisProvider qap = QueryProvider.getQueryAnalzyer("hql");
    
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
        return niceQuery(query);
    }

    public static String niceQuery(String query) {
        String niceQuery = "";
        String localQuery = query;
        if(localQuery.toLowerCase().startsWith("select")) {
            niceQuery += "SELECT \n" +
                         "       ";
            localQuery = localQuery.substring("select ".length());
            String[] from = localQuery.split("[ ][f|F][r|R][o|O][m|M][ ]");
            niceQuery += from[0] + "\n";
            niceQuery += "FROM \n" + 
                         "     ";
            localQuery = from[1];
            String[] where = localQuery.split("[ ][w|W][h|H][e|E][r|R][e|E][ ]");
            niceQuery += where[0] + "\n";
            niceQuery += "WHERE \n" +
                         "      ";
            niceQuery += where[1];
        }
        return niceQuery;
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

    public List<String[]> execute(Map<String, String> arguments, String defaultArgumentName, Session s) {
        String queryArguments = defaultArgumentName;
        if (getArguments().length() > 0) {
            queryArguments = getArguments();
        }
        
        //inline all makumba query functions
        String localQuery = qap.inlineFunctions(query);
    
        String args = ""; // for debug
        StringTokenizer st = new StringTokenizer(queryArguments, ",");
        while (st.hasMoreTokens()) {
    
            String t = st.nextToken().trim();
    
            String value = arguments.get(t);
            if (value != null) {
                // we replace the values by hand since hibernate fucks up for IN() queries
                localQuery = localQuery.replaceAll(":"+t, value.startsWith("'") ? value : "'" + value + "'");
//                q.setString(t, value);
                args += t + "=" + value;
                if (st.hasMoreTokens())
                    args += ", ";
            }
        }
        
        Query q = s.createQuery(localQuery);
        
        logger.debug("Executing relation query:\n\n" + this + "\n\nwith arguments " + args);
        executedQueries++;
        
        List result = q.list();
        logger.debug("Got "+result.size()+" result(s)");
    
        return result;
    }

    public static int getExecutedQueries() {
        return executedQueries;
    }

}
