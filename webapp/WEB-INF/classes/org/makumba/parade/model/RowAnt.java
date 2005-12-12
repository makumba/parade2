package org.makumba.parade.model;

import java.util.ArrayList;


import org.makumba.parade.ifc.RowData;

public class RowAnt extends AbstractRowData implements RowData {

	private Long id;
	
	private Long lastmodified;
	
	private ArrayList topTargets = new ArrayList();
	
	private ArrayList subTargets = new ArrayList();
	
	public ArrayList getSubTargets() {
		return subTargets;
	}

	public void setSubTargets(ArrayList subTargets) {
		this.subTargets = subTargets;
	}

	public ArrayList getTopTargets() {
		return topTargets;
	}

	public void setTopTargets(ArrayList topTargets) {
		this.topTargets = topTargets;
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
