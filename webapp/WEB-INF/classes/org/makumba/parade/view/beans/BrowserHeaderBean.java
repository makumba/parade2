package org.makumba.parade.view.beans;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.Pointer;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.ServletContainer;

public class BrowserHeaderBean {

    private Pointer rowId = null;

    public void setRowId(String rowId) {
        this.rowId = new Pointer("Row", rowId);
    }

    public List<String> getAntOperations() {
        // Fetching the necessary data from the Hibernate model. This can't be done yet by Makumba, since the model uses
        // inheritance, and this is not supported by Makumba.
        Session s = null;
        try {
            // Creating a new Hibernate Session, which needs to be closed in any case at the end (in the finally block)
            s = InitServlet.getSessionFactory().openSession();
            // Starting a new transaction
            Transaction tx = s.beginTransaction();

            // We fetch the row
            Row r = (Row) s.get(Row.class, new Long(rowId.getId()));
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

    public int getWebappStatus() {
        Session s = null;
        try {
            // Creating a new Hibernate Session, which needs to be closed in any case at the end (in the finally block)
            s = InitServlet.getSessionFactory().openSession();
            // Starting a new transaction
            Transaction tx = s.beginTransaction();

            // We fetch the row
            Row r = (Row) s.get(Row.class, new Long(rowId.getId()));

            // Webapp status data
            int status = r.getStatus().intValue();

            if (r.getRowpath().equals(r.getParade().getBaseDir())) {
                r.setStatus(new Integer(ServletContainer.RUNNING));
                status = ServletContainer.RUNNING;
            }

            // we commit the transaction
            tx.commit();

            return status;

        } finally {
            if (s != null)
                s.close();
        }

    }

}
