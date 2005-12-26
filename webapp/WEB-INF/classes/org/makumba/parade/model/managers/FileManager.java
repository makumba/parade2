package org.makumba.parade.model.managers;

import java.io.FileFilter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.makumba.parade.model.File;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.interfaces.DirectoryRefresher;
import org.makumba.parade.model.interfaces.RowRefresher;
import org.makumba.parade.tools.SimpleFileFilter;

public class FileManager implements RowRefresher, DirectoryRefresher {

	static Logger logger = Logger.getLogger(FileManager.class.getName());
	
	private FileFilter filter = new SimpleFileFilter();
	
	/* Creates a first File for the row which is its root dir 
	 * and invokes its refresh() method
	 */
	public void rowRefresh(Row row) {
		
		File root = new File();
		
		try {
			java.io.File rootPath = new java.io.File(row.getRowpath());
			root.setName("_root_");
			root.setPath(rootPath.getAbsolutePath());
			root.setRow(row);
			root.setDate(new Long(new java.util.Date().getTime()));
			root.setAge(new Long(0));
			root.setFiledata(new HashMap());
			root.setSize(new Long(0));
			root.setNotOnDisk(true);
			
			row.getFiles().clear();
			row.getFiles().put("_root_",root);
			//row.getParade().addRow(row);
			
		} catch(Throwable t) {
			logger.error("Couldn't access row path of row "+row.getRowname(),t);
		}
		
		root.refresh();
		
	}

	public void directoryRefresh(Row row, String path) {
		java.io.File currDir = new java.io.File(path);
		
		if(currDir.isDirectory() && !path.contains("_root_")) {
			
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
		
		row.getFiles().put(fileData.getPath(),fileData);
		
		//logger.warn("Added file: "+fileData.getName());
	}

	/* setting File informations */
	private File setFileData(Row row, java.io.File file, boolean isDir) {
		File fileData = new File();
		fileData.setIsDir(isDir);
		fileData.setRow(row);
		fileData.setPath(file.getAbsolutePath());
		fileData.setName(file.getName());
		fileData.setDate(new Long(file.lastModified()));
		fileData.setAge(new Long((new Date()).getTime() - file.lastModified()));
		fileData.setSize(new Long(file.length()));
		fileData.setNotOnDisk(false);
		return fileData;
	}
	
	
	public static File setVirtualFileData(Row r, File path, String name, boolean dir) {
		File cvsfile = new File();
		cvsfile.setName(name);
		cvsfile.setPath(path.getPath() + java.io.File.separator + name);
		cvsfile.setNotOnDisk(true);
		cvsfile.setIsDir(dir);
		cvsfile.setRow(r);
		cvsfile.setDate(new Long((new Date()).getTime()));
		cvsfile.setAge(new Long(0));
		cvsfile.setSize(new Long(0));
		return cvsfile;
	}
	
	/* returns a List of the keys of the subdirs of a given path */
	public static List getSubdirs(Row r, String path) {
		
		Set keySet = r.getFiles().keySet();
		
		List subDirs = new LinkedList();
		
		for(Iterator i = keySet.iterator(); i.hasNext();) {
			String currentKey = (String) i.next();
			if(currentKey.startsWith(path) && currentKey.substring(0,path.length()).length() > 0) {
				File f = (File) r.getFiles().get(currentKey);
				if (f.getIsDir()) {
					subDirs.add(currentKey);
				}
			}
		}
		
		return subDirs;
		
	}
	
	/* returns a List of the direct children (files, dirs) of a given Path */
	public static List getChildren(Row r, String path) {
		String keyPath = path;
		
		String absoulteRowPath = (new java.io.File(r.getRowpath()).getAbsolutePath());
		if(keyPath == null || keyPath=="") keyPath=absoulteRowPath;
		keyPath=keyPath.replace('/',java.io.File.separatorChar);
		
		Set keySet = r.getFiles().keySet();
		
		List children = new LinkedList();
		
		for(Iterator i = keySet.iterator(); i.hasNext();) {
			String currentKey = (String) i.next();
			boolean isChild = currentKey.startsWith(keyPath);
			if(isChild)  {
				boolean isNotRoot = currentKey.length() - keyPath.length() > 0;
				if (isNotRoot) {
					String childKey = currentKey.substring(keyPath.length()+1,currentKey.length());
					boolean isDirectChild = childKey.indexOf(java.io.File.separator) == -1;
					if(isDirectChild) {
						children.add(r.getFiles().get(currentKey));
					}
				}
				
			}
			
		}
		
		return children;
	}

}
