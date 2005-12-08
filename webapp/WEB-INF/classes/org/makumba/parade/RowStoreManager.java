package org.makumba.parade;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.makumba.parade.ifc.ParadeRefresher;
import org.makumba.parade.ifc.RowData;
import org.makumba.parade.ifc.RowRefresher;
import org.makumba.parade.model.AbstractRowData;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;


public class RowStoreManager implements ParadeRefresher {
	
	static Logger logger = Logger.getLogger(RowStoreManager.class.getName());
	
	/* Reads the row definitions of RowProperties,
	 * compares if they're changes to the current rowstore
	 * and creates the Rows
	 */
	public void paradeRefresh(Parade p) {
		
		Map rows = p.getRows();
		
		Map rowstore = (new RowProperties()).getRowDefinitions();
		if(rowstore.isEmpty()) {
    		logger.warn("No row definitions found, check RowProperties");
		}
		createRows(rowstore, rows, p);
		
	}
	
	/* Creates/updates rows */
    private void createRows(Map rowstore, Map rows, Parade p) {
    	
    	Iterator i = rowstore.keySet().iterator();
	   
    	while(i.hasNext()) {
    		Map row = (Map) rowstore.get((String) i.next());
    		String rowname = (String) row.get("name");
    		logger.warn(("Now going through row: "+rowname));
    		
    		// looks if the row with the same name already exists and updates if necessary
    		if(rows.containsKey(rowname)) {
    			
    			Row storedRow = (Row) rows.get(rowname);
				
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
	            Row r = new Row();
	            r.setRowname((String)row.get("name"));
	            String relativePath = ((String) row.get("path"));
	            r.setRowpath(relativePath);
	            r.setDescription((String)row.get("desc"));
	            r.setParade(p);
	            	            
	            logger.warn("Created new row "+rowname);
	            
	            p.getRows().put(rowname,r);
				
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
