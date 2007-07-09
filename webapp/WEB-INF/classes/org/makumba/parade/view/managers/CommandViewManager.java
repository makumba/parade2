package org.makumba.parade.view.managers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.view.interfaces.CommandView;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CommandViewManager implements CommandView {

    public String getCommandView(String view, Row r, String path, String file, String opResult) {
        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);
        String template = "";
        
        // treating parameters
        if(opResult == null) opResult = new String("");
        if(path == null) path = new String("");

        String pathEncoded = "";

        try {
            pathEncoded = URLEncoder.encode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // depending on what we want, we generate different results
        
        if (view == null || view.equals(""))
            return "jsp:/tipOfTheDay.jsp";
        else if (view != null && view.equals("newFile"))
            template = "newFile.ftl";
        else if (view != null && view.equals("newDir"))
            template = "newDir.ftl";
        else if(view != null && view.equals("commandOutput"))
            template ="commandOutput.ftl";
        else if(view != null && view.equals("commit"))
            template="cvsCommit.ftl";
        else
            return "No such view defined for Command";
        
        Template temp = null;
        try {
            temp = InitServlet.getFreemarkerCfg().getTemplate(template);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        /* Creating the data model */
        SimpleHash root = new SimpleHash();
        root.put("rowName", r.getRowname());
        root.put("path", path);
        root.put("pathURI", pathEncoded);
        root.put("opResult", opResult);
        
        if(file != null) {
            java.io.File f = new java.io.File(file);
            root.put("fileAbsolutePath", f.getAbsolutePath());
            root.put("fileName", f.getName());
        }

        /* Merge data model with template */
        try {
            temp.process(root, out);
        } catch (TemplateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        out.flush();
        
        return result.toString();

    }

    public String uploadFile(String path, String file, Object content, Object context) {

        boolean success = true;

        try {
            OutputStream dest = new BufferedOutputStream(new FileOutputStream(path + File.separator + file));
            ((org.makumba.Text) content).writeTo(dest);
            dest.close();
        } catch (Exception ew) {
            success = false;
            return ("Error writing file: " + ew);
        }

        if (success) {
            Session s = InitServlet.getSessionFactory().openSession();
            Transaction tx = s.beginTransaction();

            Parade p = (Parade) s.get(Parade.class, new Long(1));
            FileManager fileMgr = new FileManager();
            fileMgr.uploadFile(p, path + File.separator + file, (String) context);

            tx.commit();
            s.close();
        }
        return ("");
    }
    
}
