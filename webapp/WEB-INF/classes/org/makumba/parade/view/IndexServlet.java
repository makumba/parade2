package org.makumba.parade.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.User;
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

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void init() {
    }

    @Override
    public void service(ServletRequest req, ServletResponse resp) throws java.io.IOException, ServletException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        String context = req.getParameter("context");
        if (context == null)
            context = (String) req.getAttribute("context");

        String display = req.getParameter("display");
        if (context == null)
            context = (String) req.getAttribute("display");

        User u = (User) ((HttpServletRequest) req).getSession(true).getAttribute("org.makumba.parade.userObject");

        String opResult = (String) req.getAttribute("result");
        Boolean successAttr = (Boolean) req.getAttribute("success");
        boolean success = true;
        if (successAttr == null)
            success = false;
        else
            success = successAttr.booleanValue();

        PrintWriter out = resp.getWriter();

        Session s = null;
        Transaction tx = null;
        Parade p = null;
        try {
            s = InitServlet.getSessionFactory().openSession();
            tx = s.beginTransaction();
            p = (Parade) s.get(Parade.class, new Long(1));

            if (u == null) {
                RequestDispatcher dispatcher = super.getServletContext().getRequestDispatcher("/servlet/user");
                dispatcher.forward(req, resp);
            } else {
                if (context != null && !display.equals("index")) {
                    RequestDispatcher dispatcher = super.getServletContext().getRequestDispatcher("/servlet/browse");
                    dispatcher.forward(req, resp);
                }
                RequestDispatcher header = super.getServletContext().getRequestDispatcher("/layout/header.jsp?class=editor&pageTitle=Welcome%20to%20ParaDe");
                RequestDispatcher footer = super.getServletContext().getRequestDispatcher("/layout/footer.jsp");
                
                header.include(req, resp);
                out.print(getView(p, context, opResult, success, !(successAttr == null), u));
                footer.include(req, resp);
            }
        } finally {
            tx.commit();
            s.close();
        }
    }

    public String getView(Parade p, String context, String opResult, boolean success, boolean displaySuccess, User user) {

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

        root.put("displaySuccess", displaySuccess);
        root.put("success", success);
        root.put("opResult", opResult);
        root.put("userNickName", user.getNickname());

        // we get the header information from each manager
        List<String> headers = new LinkedList<String>();

        rowstoreView.setParadeViewHeader(headers);
        cvsView.setParadeViewHeader(headers);
        antView.setParadeViewHeader(headers);
        webappView.setParadeViewHeader(headers);
        makView.setParadeViewHeader(headers);

        root.put("headers", headers);

        root.put("onlineUsers", getActiveUsers());

        // Iteration over the rows

        List<SimpleHash> rows = new LinkedList<SimpleHash>();

        if (p == null) {
            throw new ParadeException(
                    "Could not display index, probably the server is rebuilding it. Please come back in about 5 minutes.");
        }

        Iterator<String> rowIterator = p.getRows().keySet().iterator();
        while (rowIterator.hasNext()) {
            
            String key = rowIterator.next();
            Row r = p.getRows().get(key);

            if(!r.getModuleRow()) {
                SimpleHash rowInformation = new SimpleHash();

                // Each view manager populates the model with the information it needs
                rowstoreView.setParadeView(rowInformation, r);
                cvsView.setParadeView(rowInformation, r);
                antView.setParadeView(rowInformation, r);
                webappView.setParadeView(rowInformation, r);
                makView.setParadeView(rowInformation, r);

                rows.add(rowInformation);
            }
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
    
    /**
     * Gets the users active in the past 20 minutes
     * @return a String array containing the 
     */
    public static List<String[]> getActiveUsers() {
        List<String[]> activeUsers = new LinkedList<String[]>();
        
        Session s = null;
        try {
           s = InitServlet.getSessionFactory().openSession();
           
           Query q = s.createQuery("select u.login, u.nickname from ActionLog a, User u where a.user = u.login and a.logDate > :myDate group by u.login");
           
           Calendar cal = Calendar.getInstance();
           cal.setTime(new Date());
           cal.add(Calendar.MINUTE, -20);

           q.setTimestamp("myDate", cal.getTime());
           
           for(Object res : q.list()) {
               Object[] userInfo = (Object[]) res;
               activeUsers.add(new String[] {(String)userInfo[0], (String)userInfo[1]});
           }
           
        } finally {
            if(s!=null) {
                s.close();
            }
        }
        
        return activeUsers;
        
    }

}
