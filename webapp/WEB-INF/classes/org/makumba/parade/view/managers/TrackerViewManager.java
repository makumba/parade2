package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.makumba.parade.model.File;
import org.makumba.parade.model.Row;
import org.makumba.parade.view.interfaces.FileView;

public class TrackerViewManager implements FileView {

	public String getFileView(Row r, String path, File f) {
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		out.print("<td>Not tracked</td>");
		
		return result.toString();
	}

	public String getFileViewHeader(Row r, String path) {
		String header = "<th>Tracker</th>";
		return header;
	}

}
