package org.makumba.parade.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.tools.HtmlUtils;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.AntManager;
import org.makumba.parade.model.managers.FileManager;

public class AntController {
    
    public Object[] onAntAction(String context, String op) {
        
        AntManager antMgr = new AntManager();
        FileManager fileMgr = new FileManager();
        
        String opResult = "";
        
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));
        
        Row entryRow = null;
        
        if (context != null)
            entryRow = Row.getRow(p, context);

        try {
            opResult = antMgr.executeAntCommand(entryRow, op);
        } catch (IOException e) {
            opResult = "Internal ParaDe error: error during execution of Ant Command:\n";
            opResult+= e.getMessage();
        }
        
        fileMgr.rowRefresh(entryRow);

        tx.commit();
        s.close();
        
        Object[] res = { opResult, new Boolean(true) };
        
        return res;
    }
    
    
    

}
