package org.makumba.parade.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.AntManager;
import org.makumba.parade.model.managers.FileManager;

public class AntController {
    
    static Logger logger = Logger.getLogger(InitServlet.class.getName());
    
    public Object[] onAntAction(String context, String op) {
        logger.debug("ANT Controller: running operation "+op+" on context "+context);
        
        AntManager antMgr = new AntManager();
        FileManager fileMgr = new FileManager();
        
        String opResult = "";
        
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));
        
        Row entryRow = null;
        
        if (context != null)
            entryRow = Row.getRow(p, context);

        opResult = antMgr.executeAntCommand(entryRow, op);
        
        //fileMgr.rowRefresh(entryRow);

        tx.commit();
        s.close();
        
        Object[] res = { opResult, new Boolean(true) };
        
        return res;
    }
    
    
    

}
