package org.makumba.parade.controller;

import org.makumba.parade.listeners.ParadeJNotifyListener;
import org.makumba.parade.model.managers.FileManager;

public class CommandController {

    public static Response onNewFile(String context, String path, String filename) {
        String fullname = FileManager.getFullname(context, path, filename);

        ParadeJNotifyListener.createFileLock(fullname);
        String result = FileManager.check("new", context, fullname);
        if (result == null) {
            result = FileManager.newFile(fullname);
        }
        ParadeJNotifyListener.removeFileLock(fullname);

        FileManager.updateSimpleCaches(context, fullname);
        return new Response(result);
    }

    public static Response onNewDir(String context, String path, String filename) {
        String fullname = FileManager.getFullname(context, path, filename);

        ParadeJNotifyListener.createFileLock(fullname);
        String result = FileManager.check("new", context, fullname);
        if (result == null) {
            result = FileManager.newDir(fullname);
        }
        ParadeJNotifyListener.removeFileLock(fullname);

        FileManager.updateSimpleCaches(context, fullname);
        return new Response(result);
    }

    /**
     * @author Joao Andrade
     * @param context
     * @param params
     * @return
     */
    public static Response onUpload(String context, String path, String filename, byte[] fileData) {
        String fullname = FileManager.getFullname(context, path, filename);

        String result = FileManager.uploadFile(fullname, fileData);

        FileManager.updateSimpleCaches(context, fullname);
        return new Response(result);
    }
}