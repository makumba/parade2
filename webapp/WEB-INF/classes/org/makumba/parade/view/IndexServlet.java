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
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.listeners.ParadeSessionListener;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.User;
import org.makumba.parade.tools.MultipleUsersException;
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
    
    private static Logger logger = Logger.getLogger(IndexServlet.class);

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

        Session s = null;
        Transaction tx = null;
        User u = null;
        Parade p = null;
        try {
            s = InitServlet.getSessionFactory().openSession();
            tx = s.beginTransaction();
            p = (Parade) s.get(Parade.class, new Long(1));
    
            u = detectUser(req, s);
            
            if(u == null) {
                RequestDispatcher dispatcher = super.getServletContext().getRequestDispatcher("/servlet/user");
                dispatcher.forward(req, resp);
            } else {
                if (context != null && !display.equals("index")) {
                    RequestDispatcher dispatcher = super.getServletContext().getRequestDispatcher("/servlet/browse");
                    dispatcher.forward(req, resp);
                }
                out.print(getView(p, context, opResult, success, !(successAttr == null), u));
            }
        } catch(MultipleUsersException mue) {
            u = User.getUnknownUser();
            if (context != null && !display.equals("index")) {
                RequestDispatcher dispatcher = super.getServletContext().getRequestDispatcher("/servlet/browse");
                dispatcher.forward(req, resp);
            }
            out.print(getView(p, context, opResult, success, !(successAttr == null), u));
            
        } finally {
            tx.commit();
            s.close();
        }
    }

    private User detectUser(ServletRequest req, Session s) throws MultipleUsersException {
        String user = (String) ((HttpServletRequest)req).getSession(true).getAttribute("org.makumba.parade.user");
        Object userObject = ((HttpServletRequest)req).getSession(true).getAttribute("org.makumba.parade.userObject");
        if(userObject != null) {
            return (User) userObject;
        }
        
        // let's check if we know this user
        Transaction tx = s.getTransaction();
        
        Query q;
        q = s.createQuery("from User u where u.login = ?");
        q.setString(0, user);
        
        List<User> results = q.list();
        User u = null;
        
        if(results.size() > 1) {
            logger.error("Multiple possibilities for user "+user+". Please contact developers.");
        } else if(results.size() == 1) {
            // we know the guy, let's put more stuff in the session
            u = results.get(0);
            ((HttpServletRequest)req).getSession().setAttribute("org.makumba.parade.userObject", u);
            ((HttpServletRequest)req).getSession().setAttribute("user.name", u.getName());
            ((HttpServletRequest)req).getSession().setAttribute("user.surname", u.getSurname());
            ((HttpServletRequest)req).getSession().setAttribute("user.nickname", u.getNickname());
            ((HttpServletRequest)req).getSession().setAttribute("user.email", u.getEmail());
            ((HttpServletRequest)req).getSession().setAttribute("user.PAptr", u.getPAptr());
        }
        
        return u;
        
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
        List headers = new LinkedList();

        rowstoreView.setParadeViewHeader(headers);
        cvsView.setParadeViewHeader(headers);
        antView.setParadeViewHeader(headers);
        webappView.setParadeViewHeader(headers);
        makView.setParadeViewHeader(headers);
        
        root.put("headers", headers);
        
        root.put("onlineUsers", ParadeSessionListener.getActiveSessionNicknames());
        
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
