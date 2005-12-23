package org.makumba.parade.model;


public class RowAnt extends AbstractRowData implements RowData {

	private Long id;
	
	private Long lastmodified;
	
	private java.io.File buildfile;
	
	public java.io.File getBuildfile() {
		return buildfile;
	}

	public void setBuildfile(java.io.File buildfile) {
		this.buildfile = buildfile;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLastmodified() {
		return lastmodified;
	}

	public void setLastmodified(Long lastmodified) {
		this.lastmodified = lastmodified;
	}
	
	
}
