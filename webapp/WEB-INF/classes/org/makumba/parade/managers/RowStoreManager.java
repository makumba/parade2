package org.makumba.parade.managers;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.makumba.parade.RowProperties;
import org.makumba.parade.ifc.ParadeRefresher;
import org.makumba.parade.ifc.RowData;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;


public class RowStoreManager implements ParadeRefresher {
	
	static Logger logger = Logger.getLogger(RowStoreManager.class.getName());
	
	/* Reads the row definitions of RowProperties,
	 * compares if they're changes to the current rowstore
	 * and creates the Rows
	 */
	public void paradeRefresh(Parade p) {
		
		Map rowstore = (new RowProperties()).getRowDefinitions();
		if(rowstore.isEmpty()) {
    		logger.warn("No row definitions found, check RowProperties");
		}
		createRows(rowstore, p);
		
	}
	
	/* Creates/updates rows */
    private void createRows(Map rowstore, Parade p) {
    	
    	/*
    	Iterator t = p.getRows().keySet().iterator();
    	while(t.hasNext()) {
    		logger.warn("************* "+((String)t.next()));
    		
    	}
    	*/
    	
    	Iterator i = rowstore.keySet().iterator();
    	Map row = new HashMap();
    	String rowname = "";
    	Row r = new Row();
	   
    	while(i.hasNext()) {
    		row = (Map) rowstore.get((String) i.next());
    		rowname = ((String) row.get("name")).trim();
    		//logger.warn("************ "+rowname);
    		
    		// looks if the row with the same name already exists and updates if necessary
    		if(p.getRows().containsKey(rowname)) {
    			logger.warn(("Now going through row: "+rowname));
    			
    			Row storedRow = (Row) p.getRows().get(rowname);
				
				if(!((String) row.get("path")).trim().equals(storedRow.getRowpath())) {
					storedRow.setRowpath((String)row.get("path"));
					logger.warn("The path of row "+rowname+" was updated to "+(String)row.get("path"));
				}
				if(!((String)row.get("desc")).trim().equals(storedRow.getDescription())) {
					storedRow.setDescription((String)row.get("desc"));
					logger.warn("The description of row "+rowname+" was updated to "+(String)row.get("desc"));
				}
				
				p.getRows().put(rowname,storedRow);
				
    		} else {
    			
    			// creating Row object and passing the information
	            r = new Row();
	            r.setRowname(((String)row.get("name")).trim());
	            String relativePath = (((String) row.get("path")).trim());
	            r.setRowpath(relativePath);
	            r.setDescription((String)row.get("desc"));
	            	            
	            logger.warn("Created new row "+rowname);
	            p.addRow(r);
				
    		}
    	}
    }
    
    public void addManagerData(RowData data, Row r) {
    	
    	data.setRow(r);
    	r.getRowdata().put(data.getDataType(),data);
    }

    
    /*
    public String view(String rowname) {
    	Map row = (Map) .getRowstore().get(rowname);
    	return ((String) row.get("name")+" "+((File)row.get("path")).getPath());
    }
    */
	
}
