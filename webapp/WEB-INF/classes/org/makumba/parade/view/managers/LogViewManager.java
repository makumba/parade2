package org.makumba.parade.view.managers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Log;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class LogViewManager {

    public String getLogView(Session s, String context, Integer years, Integer months, Integer days) {
        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);

        Template temp = null;
        try {
            temp = InitServlet.getFreemarkerCfg().getTemplate("logs.ftl");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Creating the data model
        SimpleHash root = new SimpleHash();
        root.put("context", context);
        root.put("year", years.toString());
        root.put("month", ""+(months.intValue()+1));
        root.put("day", days.toString());
        
        Calendar cal = GregorianCalendar.getInstance();
        
        cal.clear();
        cal.set(years.intValue(), months.intValue(), days.intValue());
        
        String query = "from Log l where l.context = :context and l.date > :date";
        if(context.equals("all"))
            query = "from Log l where l.date > :date";
        
        Query q = s.createQuery(query);
        q.setCacheable(false);
        
        if(!context.equals("all"))
                q.setString("context", context);
        q.setDate("date", cal.getTime());

        List<Log> entries = q.list();
        List viewEntries = new LinkedList();
        for(int i=0; i<entries.size(); i++) {
            SimpleHash entry = new SimpleHash();
            entry.put("message", entries.get(i).getMessage());
            viewEntries.add(entry);
        }
        
        root.put("entries", viewEntries);
        
        /* Merge data model with template */
        try {
            temp.process(root, out);
        } catch (TemplateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return result.toString();
        
    }

    

}
