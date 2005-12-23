package org.makumba.parade.model.managers;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.makumba.parade.init.RowProperties;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowData;
import org.makumba.parade.model.interfaces.ParadeRefresher;


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

    	Iterator i = rowstore.keySet().iterator();
    	Map row = new HashMap();
    	String rowname = "";
    	
    	while(i.hasNext()) {
    		row = (Map) rowstore.get((String) i.next());
    		rowname = ((String) row.get("name")).trim();
    		
    		// looks if the row with the same name already exists and updates if necessary
    		if(p.getRows().containsKey(rowname)) {
    			
    			Row storedRow = (Row) p.getRows().get(rowname);
				
				if(!((String) row.get("path")).trim().equals(storedRow.getRowpath())) {
					storedRow.setRowpath((String)row.get("path"));
					logger.warn("The path of row "+rowname+" was updated to "+(String)row.get("path"));
				}
				if(!((String)row.get("desc")).trim().equals(storedRow.getDescription())) {
					storedRow.setDescription((String)row.get("desc"));
					logger.warn("The description of row "+rowname+" was updated to "+(String)row.get("desc"));
				}
				
				p.addRow(storedRow);
				
    		} else {
    			
    			// creating Row object and passing the information
    			Row r = new Row();
	            String name = ((String) row.get("name")).trim();
	            r.setRowname(name);
	            String relativePath = (((String) row.get("path")).trim());
	            r.setRowpath(relativePath);
	            r.setDescription((String)row.get("desc"));

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
