package org.makumba.parade.model;

public class AbstractFileData {

	private Long id;
	
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
	
}
