package org.makumba.parade.model;


public class RowAnt extends AbstractRowData implements RowData {

	private Long id;
	
	private Long lastmodified;
	
	private String buildfile="";
	
	public String getBuildfile() {
		return buildfile;
	}

	public void setBuildfile(String buildfile) {
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
