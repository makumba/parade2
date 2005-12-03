package org.makumba.parade.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.model.Row;

public class RowDAO extends BaseDAO {
	
private Session s = null;
	
	public RowDAO() {
		super();
	}
	
	public void save(Row r) {
		s = getSession();
		Transaction tx = s.beginTransaction();
		
		s.save(r);
		
		tx.commit();
		s.flush();
		s.close();
		
	}
	
	public void update(Row r) {
		s = getSession();
		Transaction tx = s.beginTransaction();
		
		s.update(r);
		
		tx.commit();
		s.flush();
		s.close();
		
	}

}
