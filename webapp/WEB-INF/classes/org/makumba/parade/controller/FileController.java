package org.makumba.parade.controller;

import java.util.logging.Logger;

import org.makumba.parade.listeners.ParadeJNotifyListener;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.tools.ParadeLogger;

public class FileController {

    static Logger logger = ParadeLogger.getParadeLogger(FileController.class.getName());

    public static Response onDeleteFile(String context, String path, String filename) {
        String fullname = FileManager.getFullname(context, path, filename);

        ParadeJNotifyListener.createFileLock(fullname);
        String result = FileManager.check("delete", context, fullname);
        if (result == null) {
            result = FileManager.deleteFile(fullname);
        }
        ParadeJNotifyListener.removeFileLock(fullname);

        FileManager.updateSimpleCaches(context, fullname);
        return new Response(result);
    }

    public static Response onDeleteDir(String context, String path, String filename) {
        String fullname = FileManager.getFullname(context, path, filename);

        ParadeJNotifyListener.createFileLock(fullname);
        String result = FileManager.check("delete", context, fullname);
        if (result == null) {
            result = FileManager.deleteDir(fullname);
        }
        ParadeJNotifyListener.removeFileLock(fullname);

        FileManager.updateSimpleCaches(context, fullname);
        return new Response(result);
    }

    public static Response onSaveFile(String context, String path, String filename, String[] source) {
        String fullname = FileManager.getFullname(context, path, filename);
        
        ParadeJNotifyListener.createFileLock(fullname);
        String result = FileManager.check("save", context, fullname);
        if (result == null) {
            result = FileManager.saveFile(fullname, source[0]);
        }
        ParadeJNotifyListener.updateRelations(Parade.constructAbsolutePath(context, ""), path
                + (path.endsWith("/") || filename.startsWith("/") ? "" : java.io.File.separator) + filename);
        ParadeJNotifyListener.removeFileLock(fullname);
        
        FileManager.updateSimpleCaches(context, fullname);
        return new Response(result);
    }
}
