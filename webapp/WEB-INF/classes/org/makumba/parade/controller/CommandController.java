package org.makumba.parade.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        return handleFileAction(context, params, "newFile");
    }

    public static Object[] onNewDir(String context, String[] params) {
        return handleFileAction(context, params, "newDir");
    }

    public static Object[] onDeleteFile(String context, String[] params) {
        return handleFileAction(context, params, "deleteFile");
    }

    public static Object[] onDeleteDir(String context, String[] params) {
        return handleFileAction(context, params, "deleteDir");
    }

    private static Object[] handleFileAction(String context, String[] params, String action) {
        String result = new String();
        String filename = params[0];
        String relativePath = params[1];

        Session s = null;
        Transaction tx = null;
        boolean success = false;
        Row row = null;
        try {
            s = InitServlet.getSessionFactory().openSession();
            tx = s.beginTransaction();

            // FIXME refactor: start of separate function
            Parade p = (Parade) s.get(Parade.class, new Long(1));
            // FIXME Joao: there should be a getRow method in paraDe
            row = p.getRows().get(context); 
            // FIXME Joao: Should throw an exception
            if (row == null) {
                tx.commit();
                s.close();
                return res("Unknown context " + context, false);
            }

            String path;
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
            // end of separate function

            ParadeJNotifyListener.createFileLock(path + java.io.File.separator + filename);
            try {
                // security check - if the path of the file is outside the path of the row, we deny any action
                if ((new java.io.File(path).getCanonicalPath().length() < new java.io.File(absolutePath)
                        .getCanonicalPath().length())) {
                    result = "Error: you can't access files outside of the row";
                }
                Class<?> clazz = FileManager.class;
                Method m = clazz.getMethod(action, Row.class, String.class, String.class);
                result = (String) m.invoke(new FileManager(), row, path, filename);
                success = result.startsWith("OK");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                result = "Error: Invalid file action.";
                e.printStackTrace();
            }

            if (success) {
                updateCaches(context, filename, path);
            }

            ParadeJNotifyListener.removeFileLock(path + java.io.File.separator + filename);
        } finally {
            tx.commit();
            s.close();
        }
 
        if (success) {
            if (action.equals("newFile"))
                result = CommandController.newFileOK(filename);
            if (action.equals("newDir"))
                result = CommandController.newDirOK(filename);
            if (action.equals("deleteFile"))
                result = CommandController.deleteFileOK(result.substring(result.indexOf("#") + 1));
            if (action.equals("deleteDir"))
                result = CommandController.deleteDirOK(filename);
        }
        return res(result, success);
    }

    private static void updateCaches(String context, String filename, String path) {
        // updates the caches
        // TODO add other caches (e.g. tracker) here
        FileManager.updateSimpleFileCache(context, path, filename);
        CVSManager.updateSimpleCvsCache(context, path + java.io.File.separator + filename);
    }

    private static Object[] res(String message, boolean status) {
        Object obj[] = new Object[2];
        obj[0] = message;
        obj[1] = new Boolean(status);
        return obj;
    }

    // TODO move all this somewhere else
    public static String newFileOK(String filename) {
        return "New file " + filename + " created.";
    }

    public static String newDirOK(String filename) {
        return "New directory " + filename + " created.";
    }

    public static String deleteFileOK(String filename) {
        return "File " + filename + " deleted";
    }

    public static String deleteDirOK(String filename) {
        return "Directory " + filename + " deleted";
    }
    // end of move all this somewhere else
}
