package org.makumba.parade.model;

import org.makumba.parade.ifc.RowData;

public class RowCVS extends AbstractRowData implements RowData {
	
	private Long id;
	
	private String user;
	
	private String module;
	
	private String branch;

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

}
