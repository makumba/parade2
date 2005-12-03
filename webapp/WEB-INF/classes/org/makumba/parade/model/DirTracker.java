package org.makumba.parade.model;

import org.makumba.parade.ifc.Directory;

public class DirTracker extends AbstractFileData implements Directory {
	
	private Long id;
	
	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
