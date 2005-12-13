package org.makumba.parade.managers;



import java.util.Map;
import org.makumba.parade.ifc.DirectoryRefresher;
import org.makumba.parade.model.File;
import org.makumba.parade.model.FileTracker;
import org.makumba.parade.model.Row;


public class TrackerManager implements DirectoryRefresher {

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
