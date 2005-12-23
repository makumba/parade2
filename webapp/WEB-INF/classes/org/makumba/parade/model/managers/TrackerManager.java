package org.makumba.parade.model.managers;



import java.util.Map;

import org.makumba.parade.model.DirectoryRefresher;
import org.makumba.parade.model.File;
import org.makumba.parade.model.FileTracker;
import org.makumba.parade.model.Row;


public class TrackerManager implements DirectoryRefresher {

	//"Specification"=0,"DesignApproved"=1,"Started"=2,"Developing"=3,"Stable"=4,"Frozen"=5,"Dropped"=6
	
	static Integer SPECIFICATION = new Integer(0);

    static Integer DESIGNAPPROVED = new Integer(1);

    static Integer STARTED = new Integer(2);

    static Integer DEVELOPING = new Integer(3);

    static Integer STABLE = new Integer(4);

    static Integer FROZEN = new Integer(5);

    static Integer DROPPED = new Integer(6);
	
	public void directoryRefresh(Row row, String path) {
		
		File currFile = (File) row.getFiles().get(path);
		
		if(!(currFile == null)) {
			
			Map filedata = currFile.getFiledata();
			FileTracker filetrackerdata = (FileTracker) filedata.get("tracker");
			if(filetrackerdata == null) {
				filetrackerdata = new FileTracker();
				filetrackerdata.setDataType("tracker");
				filetrackerdata.setFile(currFile);
				filetrackerdata.setTracked(false);
			}
			
			filedata.put("filetracker",filetrackerdata);
			currFile.setFiledata(filedata);
		}
	}
}
