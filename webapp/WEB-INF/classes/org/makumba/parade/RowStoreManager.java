package org.makumba.parade;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.makumba.parade.dao.ParadeDAO;
import org.makumba.parade.dao.RowDAO;
import org.makumba.parade.ifc.ParadeRefresher;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;


public class RowStoreManager implements ParadeRefresher {
	
	static Logger logger = Logger.getLogger(RowStoreManager.class.getName());
	
	private RowDAO rDAO = new RowDAO(); 
	private ParadeDAO pDAO = new ParadeDAO();
	
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
		Map result = createRows(rowstore, rows);
		
		pDAO.saveRows(result,p);
		
	}
	
	/* Creates/updates rows */
    private Map createRows(Map changedRows, Map rows) {
    	
    	Map result = new HashMap();
    	
    	Iterator i = changedRows.keySet().iterator();
	   
    	while(i.hasNext()) {
    		Map row = (Map) changedRows.get((String) i.next());
    		String rowname = (String) row.get("name");
    		
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
				
				rDAO.update(storedRow);
				
				result.put(rowname,storedRow);
    		
    		} else {
    			
    			// creating Row object and passing the information
	            Row r = new Row();
	            r.setRowname((String)row.get("name"));
	            r.setRowpath((String) row.get("path"));
	            r.setDescription((String)row.get("desc"));
	            r.setParade(pDAO.getParade());
	            rows.put((String)row.get("name"),r);
	            
	            rDAO.save(r);
	            
	            logger.warn("Created new row "+rowname);
	            
	            result.put(rowname,r);
				
    		}
    	}
    	return result;
    }
    
    /*
    public String view(String rowname) {
    	Map row = (Map) .getRowstore().get(rowname);
    	return ((String) row.get("name")+" "+((File)row.get("path")).getPath());
    }
    */
	
}
