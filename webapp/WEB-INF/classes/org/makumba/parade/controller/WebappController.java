package org.makumba.parade.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.WebappManager;

public class WebappController {
    
    public Object[] onWebappAction(String context, String op) {
        
        WebappManager webappMgr = new WebappManager();
        
        String opResult = "";
        
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));
        
        Row entryRow = null;
        
        if (context != null)
            entryRow = Row.getRow(p, context);

        if (op.equals("servletContextStart")) {
            opResult = webappMgr.servletContextStartRow(entryRow);
        }
        if (op.equals("servletContextStop")) {
            opResult = webappMgr.servletContextStopRow(entryRow);
        }
        if (op.equals("servletContextReload")) {
            opResult = webappMgr.servletContextReloadRow(entryRow);
        }
        if (op.equals("servletContextRemove")) {
            opResult = webappMgr.servletContextRemoveRow(entryRow);
        }
        if (op.equals("servletContextInstall")) {
            opResult = webappMgr.servletContextInstallRow(entryRow);
        }
        
        tx.commit();
        s.close();
        
        Object[] res = {opResult, new Boolean(true)};
        
        return res;
    }
    
    

}
