package org.makumba.parade.view.beans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.tools.ParadeLogger;

public class FileEditorBean {

    private Logger logger = ParadeLogger.getParadeLogger(FileEditorBean.class.getName());

    private String context;

    private String path;

    private String file;

    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    /**
     * Fetch the content of a file
     * 
     * @return the content of the file
     */
    public String getContent() {

        String content = "";
        Session s = null;
        try {
            s = InitServlet.getSessionFactory().openSession();
            Transaction tx = s.beginTransaction();

            Parade p = (Parade) s.get(Parade.class, new Long(1));
            Row r = p.getRows().get(context);
            if (r == null) {
                content = "Unknown context " + context;

            } else {

                // we need to build the absolute Path to the file
                String absoluteFilePath = Parade.constructAbsolutePath(context, path) + java.io.File.separator + file;

                File file = r.getFiles().get(absoluteFilePath);
                if (file == null) {
                    content = "Internal ParaDe error: cannot access file " + absoluteFilePath;
                } else {
                    content = getFileContent(source, file);
                }

                tx.commit();
            }

        } finally {
            if (s != null) {
                s.close();
            }
        }

        return content;

    }

    private String getFileContent(String source, File file) {
        java.io.File f = new java.io.File(file.getPath());
        String content = "";

        if (source != null && source.length() > 0) {
            content = source;
        } else {
            // we read the file
            if (f.exists()) {
                Reader rd;
                try {
                    rd = new BufferedReader(new FileReader(f));
                    int c;
                    StringBuffer sb = new StringBuffer();
                    while ((c = rd.read()) != -1) {
                        sb.append((char) c);
                    }
                    content = sb.toString();
                } catch (FileNotFoundException e) {
                    logger.severe(e.getMessage());
                } catch (IOException e) {
                    logger.severe(e.getMessage());
                }
            }
        }

        // we convert special characters so they are correctly displayed in HTML
        // commented out for now, because it may cause troubles with this editor

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < content.length(); i++)
            if (content.charAt(i) == '<')
                sb.append("&lt;");
            else if (content.charAt(i) == '&')
                sb.append("&amp;");
            else
                sb.append(content.charAt(i));
        content = sb.toString();

        return content;

    }

}
