package org.makumba.parade.model;

import java.util.Map;

public class File {
	
	private Long id;
	
	private Long date;
	
	private Long age;
	
	private Long size;
	
	private Map filedata;
	
	private Row row;

	public Row getRow() {
		return row;
	}

	public void setRow(Row row) {
		this.row = row;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public Map getFiledata() {
		return filedata;
	}

	public void setFiledata(Map filedata) {
		this.filedata = filedata;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

}
