package org.makumba.parade.controller;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.listeners.ParadeJNotifyListener;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.CVSManager;
import org.makumba.parade.model.managers.FileManager;

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
        String relativePath = params[1];

        FileManager fileMgr = new FileManager();

        Session s = null;
        Transaction tx = null;
        boolean success = false;
        Row row = null;

        try {
            s = InitServlet.getSessionFactory().openSession();
            tx = s.beginTransaction();

            Parade p = (Parade) s.get(Parade.class, new Long(1));

            row = p.getRows().get(context);
            if (row == null) {
                tx.commit();
                s.close();
                return res("Unknown context " + context, false);
            }

            String path = new String();
            String absolutePath = row.getRowpath();

            if (relativePath == null || relativePath == "")
                path = absolutePath;
            if (relativePath.equals("/"))
                path = absolutePath;
            if (relativePath.endsWith("/"))
                relativePath = relativePath.substring(0, relativePath.length() - 1);

            if (relativePath.length() > 0) {
                path = absolutePath + java.io.File.separator + relativePath.replace('/', java.io.File.separatorChar);
            } else {
                path = absolutePath;
            }

            ParadeJNotifyListener.createFileLock(path + java.io.File.separator + filename);

            // security check - if the path of the file is outisde the path of the row, we deny any action
            try {
                if ((new java.io.File(path).getCanonicalPath().length() < new java.io.File(absolutePath)
                        .getCanonicalPath().length())) {
                    result = "Error: you can't access files outside of the row";
                } else if (action.equals("newFile"))
                    result = fileMgr.newFile(row, path, filename);
                else if (action.equals("newDir"))
                    result = fileMgr.newDir(row, path, filename);
                else if (action.equals("deleteFile"))
                    result = fileMgr.deleteFile(row, path, filename);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            success = result.startsWith("OK");
            if (success) {
                // updates the caches
                // TODO add other caches (e.g. tracker) here
                FileManager.updateSimpleFileCache(context, path, filename);
                CVSManager.updateSimpleCvsCache(context, path + java.io.File.separator + filename);
            }

            ParadeJNotifyListener.removeFileLock(path + java.io.File.separator + filename);

        } finally {
            tx.commit();
            s.close();
        }

        if (success) {
            if (action.equals("newFile"))
                return res(CommandController.creationFileOK(row.getRowname(), relativePath, filename), success);
            if (action.equals("newDir"))
                return res(CommandController.creationDirOK(filename), success);
            if (action.equals("deleteFile"))
                return res(CommandController.deletionFileOK(result.substring(result.indexOf("#") + 1)), success);
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

    // TODO move this somewhere else
    public static String creationFileOK(String rowname, String path, String filename) {
        return "New file " + filename + " created. " + "<a href='/File.do?op=editFile&context=" + rowname + "&path="
                + path + "&file=" + filename + "&editor=codepress'>Edit</a></b>";
    }

    public static String creationDirOK(String filename) {
        return "New directory " + filename + " created. ";
    }

    public static String deletionFileOK(String filename) {
        return "File " + filename + " deleted";
    }
}
