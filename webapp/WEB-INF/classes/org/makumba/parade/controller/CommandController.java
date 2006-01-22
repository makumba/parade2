package org.makumba.parade.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.ResultTransformer;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.view.managers.FileDisplay;

/**
 * 
 * @author manu
 * @version $id
 */

public class CommandController {

    public static Object[] onNewFile(String context, String[] params) {

        Object[] result = fileHandler(context, params, "newFile");
        return result;
    }

    public static Object[] onNewDir(String context, String[] params) {

        Object[] result = fileHandler(context, params, "newDir");
        return result;
    }

    public static Object[] onDeleteFile(String context, String[] params) {
        Object[] result = fileHandler(context, params, "deleteFile");

        return result;
    }

    private static Object[] fileHandler(String context, String[] params, String action) {
        Object[] obj = new Object[2];
        String result = new String();
        String filename = params[0];
        String path = params[1];
        
        FileManager fileMgr = new FileManager();

        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Parade p = (Parade) s.get(Parade.class, new Long(1));

        Row row = (Row) p.getRows().get(context);
        if (row == null) {
            tx.commit();
            s.close();
            return res("Unknown context " + context, false);
        }
        String filepath = path + java.io.File.separator + filename;
        String relativepath = path.substring(row.getRowpath().length());
        if (action.equals("newFile"))
            result = fileMgr.newFile(row, path, filename);
        else if (action.equals("newDir"))
            result = fileMgr.newDir(row, path, filename);
        else if (action.equals("deleteFile"))
            result = fileMgr.deleteFile(row, filename);

        tx.commit();
        s.close();

        boolean success = result.startsWith("OK");
        if (success) {
            if (action.equals("newFile"))
                return res(FileDisplay.creationFileOK(row.getRowname(), relativepath, filepath, filename), success);
            if (action.equals("newDir"))
                return res(FileDisplay.creationDirOK(filename), success);
            if (action.equals("deleteFile"))
                return res(FileDisplay.deletionFileOK(result.substring(result.indexOf("#") + 1)), success);
        } else {
            if (action.equals("newFile"))
                return res(result, success);
            if (action.equals("newDir"))
                return res(result, success);
            if (action.equals("deleteFile"))
                return res(result, success);
        }

        return obj;
    }

    private static Object[] res(String message, boolean status) {
        Object obj[] = new Object[2];
        obj[0] = message;
        obj[1] = new Boolean(status);
        return obj;
    }

}
