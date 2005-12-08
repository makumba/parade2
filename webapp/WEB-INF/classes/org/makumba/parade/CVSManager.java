package org.makumba.parade;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.makumba.parade.ifc.DirectoryRefresher;
import org.makumba.parade.ifc.RowRefresher;
import org.makumba.parade.model.DirCVS;
import org.makumba.parade.model.File;
import org.makumba.parade.model.FileCVS;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowCVS;

public class CVSManager implements DirectoryRefresher, RowRefresher {

	public void directoryRefresh(Row row, String path) {
		
		Iterator i = row.getFiles().keySet().iterator();
		while(i.hasNext()) {
			File file = (File) row.getFiles().get(i.next());
			Map filedata = file.getFiledata();
			if(filedata == null) {
				filedata = new HashMap();
			}
			
			if(file.getIsDir()) {
				DirCVS dircvsdata = new DirCVS();
				dircvsdata.setDataType("dircvs");
				dircvsdata.setFile(file);
				
//				do something useful
				
				filedata.put("dircvs",dircvsdata);
				file.setFiledata(filedata);
				
			} else {
				FileCVS filecvsdata = new FileCVS();
				filecvsdata.setDataType("filecvs");
				filecvsdata.setFile(file);
				
//				do something useful
				
				filedata.put("filecvs",filecvsdata);
				file.setFiledata(filedata);
			}
		}
		
	}

	public void rowRefresh(Row row) {
		
		/* Some example data */
		RowCVS cvsdata = new RowCVS();
		cvsdata.setDataType("cvs");
		
		RowStoreManager rowMgr = new RowStoreManager();
		rowMgr.addManagerData(cvsdata,row);
	}

}
