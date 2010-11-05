package org.makumba.parade.controller;

import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.AntManager;
import org.makumba.parade.tools.ParadeLogger;

public class AntController {

    private AntManager antMgr = new AntManager();
    
    static Logger logger = ParadeLogger.getParadeLogger(InitServlet.class.getName());

    public Response onAntAction(String context, String op) {
        logger.fine("ANT Controller: running operation " + op + " on context " + context);

        String result = null;

        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));

        Row entryRow = null;

        if (context != null)
            entryRow = p.getRow(context);

        result = antMgr.executeAntCommand(entryRow, op);

        // fileMgr.rowRefresh(entryRow);

        tx.commit();
        s.close();

        return new Response(result);
    }
}
