package org.makumba.parade;

import java.util.HashMap;
import java.util.Map;

public class RowProperties {
	
	public Long id;
	
	public Map rowDefinitions = new HashMap();
	
	public RowProperties() {
		
		this.addRowDefinition("test-k","../../../my projects/hibernateExample", "the ultimate parade2 test row");
		this.addRowDefinition("test2-k","e:/my projects/hibernateExample", "the ultimate parade2 test2 row");
		//this.addRowDefinition("vilius-k","E:/My Projects/ITC/merger/vilius","vilius playing with PA Guidelines");
	}
	
	
	
	/* Get Row definitions */
	public Map getRowDefinitions() {
		return this.rowDefinitions;
		
	}
	
	/* Add a row definition */
	public void addRowDefinition(String name, String path, String description) {
		Map row = new HashMap();
		row.put("name",name);
		row.put("path",path);
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
