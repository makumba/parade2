package org.makumba.parade.model;

import java.util.Map;

public class Row {
	
	private Long id;
	
	private String rowname;
	
	private String rowpath;
	
	private String description;
	
	private Map files;
	
	//private File root;
	
	private Map rowdata;
	
	private Parade parade;
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Map getFiles() {
		return files;
	}

	public void setFiles(Map files) {
		this.files = files;
	}

	public String getRowname() {
		return rowname;
	}

	public void setRowname(String rowname) {
		this.rowname = rowname;
	}

	public String getRowpath() {
		return rowpath;
	}

	public void setRowpath(String rowpath) {
		this.rowpath = rowpath;
	}

	
	public Parade getParade() {
		return parade;
	}

	public void setParade(Parade parade) {
		this.parade = parade;
	}
	

	public Map getRowdata() {
		return rowdata;
	}

	public void setRowdata(Map rowdata) {
		this.rowdata = rowdata;
	}

	/*
	public File getRoot() {
		return root;
	}

	public void setRoot(File root) {
		this.root = root;
	}
	*/
	
}
