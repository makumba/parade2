package org.makumba.parade.model;

import java.util.ArrayList;
import java.util.Map;

import org.makumba.parade.CVSManager;
import org.makumba.parade.FileManager;
import org.makumba.parade.TrackerManager;

public class File {
	
	private Long id;
	
	private boolean isDir;
	
	private String name;
	
	private Long date;
	
	private Long age;
	
	private Long size;
	
	private Map filedata;
	
	private ArrayList children;
	
	private Row row;
	
	private java.io.File path;

	
	/* Calls the refresh() directoryRefresh() on the directory managers */
	public void refresh() {
		FileManager fileMgr = new FileManager();
		CVSManager CVSMgr = new CVSManager();
		TrackerManager trackerMgr = new TrackerManager();
		
		fileMgr.directoryRefresh(row, this.getPath());
		
		
		
	}
	
	
	
	public void setPath(java.io.File path) {
		this.path = path;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsDir() {
		return isDir;
	}

	public void setIsDir(boolean isDir) {
		this.isDir = isDir;
	}

	public java.io.File getPath() {
		return path;
	}

	public ArrayList getChildren() {
		return children;
	}

	public void setChildren(ArrayList children) {
		this.children = children;
	}

}
