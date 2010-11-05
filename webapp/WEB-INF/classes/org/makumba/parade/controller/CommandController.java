package org.makumba.parade.controller;

import org.makumba.parade.listeners.ParadeJNotifyListener;
import org.makumba.parade.model.managers.FileManager;

public class CommandController {

    public static Response onNewFile(String context, String path, String filename) {
        String fullname = FileManager.getFullFilename(context, path, filename);
        String result = "Error: you can't access files outside of the row";

        ParadeJNotifyListener.createFileLock(fullname);
        // security check - if the path of the file is outside the path of the row, we deny any action
        if (FileManager.isInsideRow(context, path)) {
            result = FileManager.newFile(fullname);
        }
        ParadeJNotifyListener.removeFileLock(fullname);

        FileManager.updateSimpleCaches(context, fullname);
        return new Response(result);
    }

    public static Response onNewDir(String context, String path, String filename) {
        String fullname = FileManager.getFullFilename(context, path, filename);
        String result = "Error: you can't access files outside of the row";

        ParadeJNotifyListener.createFileLock(fullname);
        // security check - if the path of the file is outside the path of the row, we deny any action
        if (FileManager.isInsideRow(context, path)) {
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
        String fullname = FileManager.getFullFilename(context, path, filename);
        String result = "Error: you can't access files outside of the row";

        // security check - if the path of the file is outside the path of the row, we deny any action
        if (FileManager.isInsideRow(context, path)) {
            result = FileManager.uploadFile(fullname, fileData);
        }

        FileManager.updateSimpleCaches(context, fullname);
        return new Response(result);
    }
}