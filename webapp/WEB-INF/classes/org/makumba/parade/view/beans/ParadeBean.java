package org.makumba.parade.view.beans;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.Pointer;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Row;

public class ParadeBean {

    protected Row row;

    public ParadeBean() {
        super();
    }

    public void setContext(String context) {
    
        Session s = null;
        try {
            s = InitServlet.getSessionFactory().openSession();
            Transaction tx = s.beginTransaction();
            row = (Row) s.createQuery("from Row r where r.rowname = :context").setString("context", context)
                    .uniqueResult();
            if (row == null) {
                throw new RuntimeException("Could not find row " + context);
            }
            tx.commit();
    
        } finally {
            if (s != null)
                s.close();
        }
    }

    public List<String> getAntOperations(Pointer rowPointer) {
        // Fetching the necessary data from the Hibernate model. This can't be done yet by Makumba, since the model uses
        // inheritance, and this is not supported by Makumba.
        Session s = null;
        try {
            // Creating a new Hibernate Session, which needs to be closed in any case at the end (in the finally block)
            s = InitServlet.getSessionFactory().openSession();
            // Starting a new transaction
            Transaction tx = s.beginTransaction();
    
            // We fetch the row
            Row r = (Row) s.get(Row.class, new Long(rowPointer.getId()));
            // Ant data - allowed operations
            List<String> allowedOps = r.getAllowedOperations();
            // we commit the transaction
            tx.commit();
    
            return allowedOps;
    
        } finally {
            if (s != null)
                s.close();
        }
    
    }

}