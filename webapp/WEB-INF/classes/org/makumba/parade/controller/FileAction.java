package org.makumba.parade.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.makumba.parade.listeners.ParadeJNotifyListener;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.managers.CVSManager;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.tools.ParadeException;

/**
 * Struts Action for File
 * 
 * TODO refactor to use operations directly (see CvsAction)
 * 
 * @author Manuel Gay
 * 
 */
public class FileAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String file = request.getParameter("file");
        String op = request.getParameter("op");
        String editor = request.getParameter("editor");
        String[] source = request.getParameterValues("source");

        if (op != null && op.startsWith("deleteFile")) {
            String[] params = { request.getParameter("params"), path };

            Object result[] = CommandController.onDeleteFile(context, params);
            request.setAttribute("result", result[0]);
            request.setAttribute("success", result[1]);
        }

        if (op != null && op.startsWith("saveFile")) {
            String absoluteFilePath = Parade.constructAbsolutePath(context, path) + java.io.File.separator + file;
            ParadeJNotifyListener.createFileLock(absoluteFilePath);

            if (source == null) {
                throw new ParadeException(
                        "Cannot save file: ParaDe did not receive any contents from your browser. If you use the Codepress editor, make sure that JavaScript is enabled and try reloading the edit page.");
            }

            FileController.saveFile(absoluteFilePath, source);

            FileManager.updateSimpleFileCache(context, Parade.constructAbsolutePath(context, path), file);
            CVSManager.updateSimpleCvsCache(context, absoluteFilePath);
            ParadeJNotifyListener.updateRelations(Parade.constructAbsolutePath(context, ""), path
                    + (path.endsWith("/") || file.startsWith("/") ? "" : java.io.File.separator) + file);
            ParadeJNotifyListener.removeFileLock(absoluteFilePath);

            if (editor.equals("codepress")) {
                return (mapping.findForward("codePressEdit"));
            } else {
                return (mapping.findForward("simpleEdit"));
            }
        }

        if (op != null && op.startsWith("upload")) {

            request.setAttribute("context", context);
            request.setAttribute("path", path);
            request.setAttribute("file", file);

            UploadForm uploadForm = (UploadForm) form;

            // Process the FormFile
            FormFile theFile = uploadForm.getTheFile();
            String contentType = theFile.getContentType();
            String fileName = theFile.getFileName();
            int fileSize = theFile.getFileSize();
            byte[] fileData = theFile.getFileData();

            // upload the file
            boolean success = true;
            String result = "";
            String saveFilePath = Parade.constructAbsolutePath(context, path) + File.separator + fileName;

            FileOutputStream fileOut;
            try {
                fileOut = new FileOutputStream(saveFilePath);
                fileOut.write(fileData);
                fileOut.flush();
                fileOut.close();
            } catch (FileNotFoundException e) {
                success = false;
                result = ("Error writing file: " + e);

            } catch (IOException e) {
                success = false;
                result = ("Error writing file: " + e);
            }

            if (success) {
                request.setAttribute("contentType", contentType);
                request.setAttribute("contentLength", fileSize);
            }

            request.setAttribute("result", result);
            request.setAttribute("success", success);
            request.setAttribute("saveFilePath", saveFilePath);

            return mapping.findForward("uploadResponse");

        }

        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("file", file);

        return (mapping.findForward("files"));

    }
}
