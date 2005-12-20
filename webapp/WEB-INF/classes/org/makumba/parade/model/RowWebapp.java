package org.makumba.parade.model;

import org.makumba.parade.ifc.RowData;

public class RowWebapp extends AbstractRowData implements RowData {
	
	private Long id;
	
	private String contextpath;
	
	private String contextname;
	
	private Integer status;

	public String getContextname() {
		return contextname;
	}

	public void setContextname(String name) {
		this.contextname = name;
	}

	public String getContextpath() {
		return contextpath;
	}

	public void setContextpath(String path) {
		this.contextpath = path;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
