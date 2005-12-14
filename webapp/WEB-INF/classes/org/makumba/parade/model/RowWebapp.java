package org.makumba.parade.model;

import org.makumba.parade.ifc.RowData;

public class RowWebapp extends AbstractRowData implements RowData {
	
	private Long id;
	
	private String path;
	
	private String name;
	
	private Integer status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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
