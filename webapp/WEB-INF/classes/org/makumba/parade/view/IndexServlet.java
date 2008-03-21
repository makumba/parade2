package org.makumba.parade.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.tools.ParadeException;
import org.makumba.parade.view.managers.AntViewManager;
import org.makumba.parade.view.managers.CVSViewManager;
import org.makumba.parade.view.managers.MakumbaViewManager;
import org.makumba.parade.view.managers.RowStoreViewManager;
import org.makumba.parade.view.managers.WebappViewManager;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class IndexServlet extends HttpServlet {

    public void init() {
    }

    public void service(ServletRequest req, ServletResponse resp) throws java.io.IOException, ServletException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        String context = req.getParameter("context");
        if (context == null)
            context = (String) req.getAttribute("context");
        
        String display = req.getParameter("display");
        if (context == null)
            context = (String) req.getAttribute("display");
        
        String opResult = (String) req.getAttribute("result");
        Boolean successAttr = (Boolean) req.getAttribute("success");
        boolean success = true;
        if (successAttr == null)
            success = false;
        else
            success = successAttr.booleanValue();
        
        PrintWriter out = resp.getWriter();

        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));

        if (context != null && !display.equals("index")) {
            RequestDispatcher dispatcher = super.getServletContext().getRequestDispatcher("/servlet/browse");
            dispatcher.forward(req, resp);
        }

        out.print(getView(p, context, opResult, success));

        tx.commit();

        s.close();

    }
    
    public String getView(Parade p, String context, String opResult, boolean success) {

        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);

        RowStoreViewManager rowstoreView = new RowStoreViewManager();
        CVSViewManager cvsView = new CVSViewManager();
        AntViewManager antView = new AntViewManager();
        WebappViewManager webappView = new WebappViewManager();
        MakumbaViewManager makView = new MakumbaViewManager();
        
        Template temp = null;
        try {
            temp = InitServlet.getFreemarkerCfg().getTemplate("index.ftl");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Creating the data model
        SimpleHash root = new SimpleHash();
        
        if (opResult == null) {
            success = false;
            opResult = "";
        }
        
        root.put("success", success);
        root.put("opResult", opResult);
                
        // we get the header information from each manager
        List headers = new LinkedList();

        rowstoreView.setParadeViewHeader(headers);
        cvsView.setParadeViewHeader(headers);
        antView.setParadeViewHeader(headers);
        webappView.setParadeViewHeader(headers);
        makView.setParadeViewHeader(headers);
        
        root.put("headers", headers);
        
        // Iteration over the rows
        
        List rows = new LinkedList();
        
        if(p == null) {
            throw new ParadeException("Could not display index, probably the server is rebuilding it. Please come back in about 5 minutes.");
        }
        
        Iterator rowIterator = p.getRows().keySet().iterator();
        while (rowIterator.hasNext()) {
            String key = (String) rowIterator.next();
            Row r = (Row) p.getRows().get(key);
            
            SimpleHash rowInformation = new SimpleHash();
            
            // Each view manager populates the model with the information it needs
            rowstoreView.setParadeView(rowInformation, r);
            cvsView.setParadeView(rowInformation, r);
            antView.setParadeView(rowInformation, r);
            webappView.setParadeView(rowInformation, r);
            makView.setParadeView(rowInformation, r);
            
            rows.add(rowInformation);
        }
        
        root.put("rows", rows);
        
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
