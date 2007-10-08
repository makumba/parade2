package org.makumba.parade.view.managers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Row;
import org.makumba.parade.view.interfaces.HeaderView;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class HeaderDisplay {

    // this is the beginning of the header
    public String getHeaderView(Row r, String path) {
        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);
        
        Template temp = null;
        try {
            temp = InitServlet.getFreemarkerCfg().getTemplate("header.ftl");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Creating the data model
        SimpleHash root = new SimpleHash();
        root.put("rowName", r.getRowname());
        
        List rows = new LinkedList();
        
        for (Iterator i = r.getParade().getRows().keySet().iterator(); i.hasNext();) {
            Row currentRow = (Row) r.getParade().getRows().get(i.next());
            String displayName = currentRow.getRowname();
            if (currentRow.getRowname() == "")
                displayName = "(root)";
            
            rows.add(displayName);
        }
        
        root.put("rows", rows);

        AntViewManager antV = new AntViewManager();
        WebappViewManager webappV = new WebappViewManager();
        
        antV.setHeaderView(root, r, path);
        webappV.setHeaderView(root, r, path);
        
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
        
        out.flush();
        
        return result.toString();
    }

}