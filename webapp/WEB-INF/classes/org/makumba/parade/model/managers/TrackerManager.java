package org.makumba.parade.model.managers;

import java.util.Map;

import org.makumba.parade.model.AbstractFileData;
import org.makumba.parade.model.File;
import org.makumba.parade.model.FileTracker;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.interfaces.FileRefresher;
import org.makumba.parade.model.interfaces.ParadeManager;

public class TrackerManager implements FileRefresher, ParadeManager {

    // "Specification"=0,"DesignApproved"=1,"Started"=2,"Developing"=3,"Stable"=4,"Frozen"=5,"Dropped"=6

    static Integer SPECIFICATION = new Integer(0);

    static Integer DESIGNAPPROVED = new Integer(1);

    static Integer STARTED = new Integer(2);

    static Integer DEVELOPING = new Integer(3);

    static Integer STABLE = new Integer(4);

    static Integer FROZEN = new Integer(5);

    static Integer DROPPED = new Integer(6);

    public void directoryRefresh(Row row, String path, boolean local) {

        File currFile = row.getFiles().get(path);

        if (!(currFile == null)) {

            Map<String, AbstractFileData> filedata = currFile.getFiledata();
            FileTracker filetrackerdata = (FileTracker) filedata.get("tracker");
            if (filetrackerdata == null) {
                filetrackerdata = new FileTracker();
                filetrackerdata.setDataType("tracker");
                filetrackerdata.setFile(currFile);
                filetrackerdata.setTracked(false);
            }

            filedata.put("filetracker", filetrackerdata);
            currFile.setFiledata(filedata);
        }
    }

    public void fileRefresh(Row row, String path) {
        // TODO Auto-generated method stub

    }

    public void newRow(String name, Row r, Map<String, String> m) {
        // TODO Auto-generated method stub

    }

}
