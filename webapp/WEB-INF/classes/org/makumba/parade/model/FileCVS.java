package org.makumba.parade.model;

import java.util.Date;

public class FileCVS extends AbstractFileData {
	
	private Long id;
	
	private Integer status = new Integer(0);
	
	private String revision="";
	
	private Date date;
	
	private boolean isCVSDir;
	
	
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean getIsCVSDir() {
		return isCVSDir;
	}

	public void setIsCVSDir(boolean isCVSDir) {
		this.isCVSDir = isCVSDir;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
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
