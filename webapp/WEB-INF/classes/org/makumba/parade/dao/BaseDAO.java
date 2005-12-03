package org.makumba.parade.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.makumba.parade.InitServlet;

public class BaseDAO {
	
	private SessionFactory sf = null;
	
	public BaseDAO() {
		sf = InitServlet.getSessionFactory();
	}
	
	public Session getSession() {
		return sf.openSession();
	}

}
