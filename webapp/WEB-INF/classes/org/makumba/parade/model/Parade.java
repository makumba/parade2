package org.makumba.parade.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.makumba.parade.CVSManager;
import org.makumba.parade.FileManager;
import org.makumba.parade.ParadeProperties;
import org.makumba.parade.RowStoreManager;
import org.makumba.parade.TrackerManager;

public class Parade {
	
	private Long id;
	
	private String paradeBase = new String();
	
	private Map rows = new HashMap();

	private static Logger logger = Logger.getLogger(Parade.class.getName());
	
	
	/*
	 * 1. Calls RowStoreManager.paradeRefresh()
	 * 2. Calls for each row:
	 * - rowRefresh()
	 * - directoryRefresh()
	 */
	public void refresh() {
		
		this.paradeBase = (String) ParadeProperties.getConf().get("paradeBase");
		
		RowStoreManager rowMgr = new RowStoreManager();
		FileManager fileMgr = new FileManager();
		TrackerManager trackerMgr = new TrackerManager();
		CVSManager CVSMgr = new CVSManager();
		
		/* TODO: read in config class/file which managers are parade managers
		 * and launch paradeRefresh() for all of them
		 */
		rowMgr.paradeRefresh(this);
		
		
		/* TODO: read in config class/file which managers are row managers
		 * and and launch rowRefresh(row)) for all of them
		 */
		
		Iterator i = rows.keySet().iterator();
		while(i.hasNext()) {
			
			Row r = (Row) rows.get((String) i.next());
			
			fileMgr.rowRefresh(r);
			CVSMgr.rowRefresh(r);
			trackerMgr.rowRefresh(r);
		}
		
		
		
		
	}
	
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map getRows() {
		return rows;
	}

	public void setRows(Map rows) {
		this.rows = rows;
	}
	
	public Parade() {
		
	}


	public String getParadeBase() {
		return paradeBase;
	}


	public void setParadeBase(String paradeBase) {
		this.paradeBase = paradeBase;
	}

}
