package org.makumba.parade.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.MakumbaManager;
import org.makumba.parade.model.managers.WebappManager;
import org.makumba.parade.view.ParadeRefreshPolicy;

public class WebappController {
    
    private WebappManager webappMgr = new WebappManager();
    private MakumbaManager makMgr = new MakumbaManager();

    public Response onContextStart(String context) {
        String result = null;
        
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Row r = p.getRow(context);
        
        makMgr.softRefresh(r);
        result = webappMgr.servletContextStartRow(r);
        
        ParadeRefreshPolicy.setRowCacheStale(true);

        tx.commit();
        s.close();

        return new Response(result);
    }

    public Response onContextStop(String context) {
        String result = null;
        
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Row r = p.getRow(context);
        
        result = webappMgr.servletContextStopRow(r);
        
        ParadeRefreshPolicy.setRowCacheStale(true);

        tx.commit();
        s.close();

        return new Response(result);
    }

    public Response onContextReload(String context) {
        String result = null;
        
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Row r = p.getRow(context);
        
        makMgr.softRefresh(r);
        result = webappMgr.servletContextReloadRow(r);
        
        ParadeRefreshPolicy.setRowCacheStale(true);

        tx.commit();
        s.close();

        return new Response(result);
    }

    public Response onContextRemove(String context) {
        String result = null;
        
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Row r = p.getRow(context);
        
        result = webappMgr.servletContextRemoveRow(r);
        
        ParadeRefreshPolicy.setRowCacheStale(true);

        tx.commit();
        s.close();

        return new Response(result);
    }

    public Response onContextInstall(String context) {
        String result = null;
        
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Row r = p.getRow(context);
        
        makMgr.softRefresh(r);
        result = webappMgr.servletContextInstallRow(r);

        ParadeRefreshPolicy.setRowCacheStale(true);
        
        tx.commit();
        s.close();

        return new Response(result);
    }

    public Response onContextRedeploy(String context) {
        String result = null;
        
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));
        Row r = p.getRow(context);
        
        makMgr.softRefresh(r);
        result = webappMgr.servletContextRedeployRow(r);
        
        ParadeRefreshPolicy.setRowCacheStale(true);

        tx.commit();
        s.close();

        return new Response(result);
    }
}
