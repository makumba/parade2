package org.makumba.parade.init;

import java.util.HashMap;
import java.util.Map;

public class RowProperties {
	
	public Long id;
	
	public Map rowDefinitions = new HashMap();
	
	public RowProperties() {
		
		this.addRowDefinition("test-k", ".", "", "the ultimate parade2 test row");
		this.addRowDefinition("test2-k", "../parade", "",  "the old parade row");
		//this.addRowDefinition("manu-k", "F:/bundle_k/sources/karamba", "public_html", "manu messing it all up again");
	}
	
	
	
	/* Get Row definitions */
	public Map getRowDefinitions() {
		return this.rowDefinitions;
		
	}
	
	/* Add a row definition */
	public void addRowDefinition(String name, String path, String webapp, String description) {
		Map row = new HashMap();
		row.put("name",name);
		row.put("path",path);
		row.put("webapp",webapp);
		row.put("desc",description);
		
		rowDefinitions.put(name,row);
		
	}
	
	/* Delete row definition */
	public void delRowDefinition() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRowDefinitions(Map rowStoreProperties) {
		this.rowDefinitions = rowStoreProperties;
	}
	

}
