package org.makumba.parade.dao;

import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.model.Parade;

public class ParadeDAO extends BaseDAO {
	
	private Session s = null;
	
	public ParadeDAO() {
		super();
	}
	
	public void save(Parade p) {
		s = getSession();
		Transaction tx = s.beginTransaction();
		
		s.save(p, new Long(1));
		
		tx.commit();
		s.close();
		
	}

	public void saveRows(Map rows, Parade p) {
		s = getSession();
		Transaction tx = s.beginTransaction();
		
		s.update(p);
		
		p.setRows(rows);
		
		tx.commit();
		s.close();
	}

	public Map getRows() {
		s = getSession();
		Transaction tx = s.beginTransaction();
		
		Parade p = (Parade) s.load(Parade.class,new Long(1));
		
		Map rows = p.getRows();
		
		tx.commit();
		s.close();
		
		return rows;
	}

	public Parade getParade() {
		s = getSession();
		Transaction tx = s.beginTransaction();
		
		Parade p = (Parade) s.load(Parade.class,new Long(1));
		
		tx.commit();
		s.close();
		
		return p;
	}
	
}
