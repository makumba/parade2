package org.makumba.parade;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.makumba.parade.ifc.DirectoryRefresher;
import org.makumba.parade.ifc.RowRefresher;
import org.makumba.parade.model.File;
import org.makumba.parade.model.Row;

public class FileManager implements RowRefresher, DirectoryRefresher {

	static Logger logger = Logger.getLogger(FileManager.class.getName());
	
	private FileFilter filter = new SimpleFileFilter();
	
	/* Creates a first File for the row which is its root dir 
	 * and invokes its refresh() method
	 */
	public void rowRefresh(Row row) {
		
		File root = new File();
		
		try {
			java.io.File f = new java.io.File(row.getRowpath());
			root.setName("_root_");
			root.setPath(f);
			root.setRow(row);
			root.setDate(new Long(new java.util.Date().getTime()));
			root.setAge(new Long(0));
			root.setFiledata(new HashMap());
			root.setSize(new Long(0));
			root.setId(row.getId());
			
			Map files = row.getFiles();
			if(files == null) {
				files = new HashMap();
			}
			files.clear();
			row.setFiles(files);
			files.put("_root_",root);
			row.setFiles(files);
			
		} catch(Throwable t) {
			logger.error("Couldn't access row path of row "+row.getRowname(),t);
		}
		
		root.refresh();
		
	}

	public void directoryRefresh(Row row, java.io.File currDir) {
		
		if(currDir.isDirectory()) {
			
			java.io.File[] dir = currDir.listFiles();
	        for (int i = 0; i < dir.length; i++) {
	            if (filter.accept(dir[i]) && !(dir[i].getName() == null)) {
	            
	            	java.io.File file = dir[i];
	            
		            if(file.isDirectory()) {
		            	File dirData = setFileData(row, file, true);
		            	addFile(row, dirData);
		            
		            	dirData.refresh();
		            	
		            } else if(file.isFile()) {
		            	File fileData = setFileData(row, file, false);
		            	addFile(row, fileData);
	            	}	
	            }
	        }
		}
	}
	
	
	/* adding file to Row files */
	private void addFile(Row row, File fileData) {
		Map files = row.getFiles();
		String key = fileData.getPath() + fileData.getName();
		files.put(key,fileData);
		row.setFiles(files);
		
		logger.warn("Added file: "+fileData.getName());
	}

	/* setting File informations */
	private File setFileData(Row row, java.io.File file, boolean isDir) {
		File dirData = new File();
		dirData.setIsDir(isDir);
		dirData.setRow(row);
		dirData.setPath(file);
		dirData.setName(file.getName());
		dirData.setDate(new Long(file.lastModified()));
		dirData.setAge(new Long((new Date()).getTime() - file.lastModified()));
		dirData.setSize(new Long(file.length()));
		dirData.setChildren(new ArrayList());
		return dirData;
	}
	
	
		
	/* adding file to parent children, if it's not the root 
	private void setParentChildren(Row row, File fileData) {
		File parent = (File) row.getFiles().get(fileData.getPath());
		if(!(parent == null)) {
			ArrayList children = parent.getChildren();
			if(children == null) {
				children = new ArrayList();
			}
			children.add(fileData);
			parent.setChildren(children);
			}
		}

	
	*/
	
	/* getting a file from a complete path
	private File getFile(ArrayList children, String path) {
		
		path.replaceAll("\\",java.io.File.pathSeparator);
		StringTokenizer st = new StringTokenizer(path,java.io.File.pathSeparator);
		if(!st.hasMoreTokens()) {
			return (File)children.get("path");
		} else {
			while(st.hasMoreTokens()) {
				String next = st.nextToken();
				File subdir = (File) children.get(next);
				String subpath = path.substring(next.length()+1);
				getFile(subdir.getChildren(),subpath);
			}
		}
		return null;
		
	}
	*/
	
	/* printing the Tree 
	public void printTree(StringBuffer sb, List files) {
		Iterator i = files.iterator();
		int j =0;
		while(i.hasNext() && j<files.size()) {
			File f = (File) files.get(j++);
			if(!f.getIsDir()) return;
			sb.append(f.getName());
			logger.warn("Appended "+f.getName());
			sb.append("\n");
			sb.append("  ");
			printTree(sb, f.getChildren());
		}
	}
	
	*/

}
