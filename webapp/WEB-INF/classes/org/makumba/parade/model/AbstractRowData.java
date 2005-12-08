package org.makumba.parade.model;

public class AbstractRowData {
	
	private Long id;
	
	private Row row;
	
	private String dataType;
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Row getRow() {
		return row;
	}

	public void setRow(Row row) {
		this.row = row;
	}

}
