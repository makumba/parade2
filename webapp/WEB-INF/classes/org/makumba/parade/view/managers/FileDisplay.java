package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.tools.FileComparator;

public class FileDisplay {

    // TODO move this somewhere else
    public static String creationFileOK(String rowname, String path, String filepath, String filename) {
        return "New file " + filename + " created. " + "<a href='/File.do?op=editFile&context=" + rowname + "&path="
                + path + "&file=" + filepath+"'>Edit</a></b>";
    }

    public static String creationDirOK(String filename) {
        return "New directory " + filename + " created. ";
    }

    public static String deletionFileOK(String filename) {
        return "File " + filename + " deleted";
    }

    public String getFileBrowserView(Parade p, Row r, String path, String opResult, boolean success) {

        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);

        // if this is the root of the row
        if (path == null || path.equals("null"))
            path = r.getRowpath();

        FileViewManager fileV = new FileViewManager();
        CVSViewManager cvsV = new CVSViewManager();
        TrackerViewManager trackerV = new TrackerViewManager();

        out.println("<HTML><HEAD><TITLE>" + r.getRowname() + " files</TITLE>");
        out.println("<link rel='StyleSheet' href='/style/parade.css' type='text/css'>");
        out.println("<link rel='StyleSheet' href='/style/files.css' type='text/css'>");
        out.println("</HEAD><BODY class='files'>");

        if (opResult != null) {

            if (success)
                out.println("<div class='success'>" + opResult + "</div>");
            else
                out.println("<div class='failure'>" + opResult + "</div>");

            /*
             * try { out.println(URLDecoder.decode(opResult, "UTF-8") + "<br>"); } catch (UnsupportedEncodingException
             * e) { // TODO Auto-generated catch block e.printStackTrace(); }
             */
        }

        out.println("<h2>" + "[<a href='/servlet/browse?display=file&context=" + r.getRowname() + "'>" + r.getRowname()
                + "</a>]/" + getParentDir(r, path));
        out.println("<img src='/images/folder-open.gif'>" + "</h2><div class='pathOnDisk'>" + path + "</div>");
        out.println("<table class='files'>");

        out.println("</p>");

        // headers
        out.println("<tr>" + fileV.getFileViewHeader(r, path) + cvsV.getFileViewHeader(r, path)
                /* + trackerV.getFileViewHeader(r, path) */ + "</tr>");

        // files
        File file = (File) r.getFiles().get(path);
        List files = file.getChildren();
        FileComparator fc = new FileComparator();

        Collections.sort(files, fc);
        String relativePath = path.substring(r.getRowpath().length(), path.length());

        int counter = 0;
        for (Iterator j = files.iterator(); j.hasNext();) {
            File currentFile = (File) j.next();
            out.println("<tr class='" + (((counter % 2) == 0) ? "odd" : "even") + "'>"
                    + fileV.getFileView(r, relativePath, currentFile) + cvsV.getFileView(r, relativePath, currentFile)
                    /* + trackerV.getFileView(r, relativePath, currentFile) */ + "</tr>");

            counter++;
        }

        out.println("</TABLE></BODY></HTML>");

        return result.toString();
    }

    private String getParentDir(Row r, String path) {

        if (path == null)
            path = r.getRowpath()+java.io.File.separator;
        
        String relativePath = path.substring(r.getRowpath().length(), path.length());

        String parentDir = "";
        String currentPath = path.substring(0, r.getRowpath().length());

        StringTokenizer st = new StringTokenizer(relativePath, java.io.File.separator);
        while (st.hasMoreTokens()) {
            String thisToken = st.nextToken();
            currentPath += java.io.File.separator + thisToken;
            parentDir += "<a href='/servlet/browse?display=file&context=" + r.getRowname() + "&path=" + currentPath
                    + "'>" + thisToken + "</a>" + "/";
        }
        return parentDir;
    }

}
