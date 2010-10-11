package org.makumba.parade.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class CommandAction extends DispatchAction {

    public ActionForward newFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String file = request.getParameter("file");

        Response result = CommandController.onNewFile(context, path, file);

        Boolean success = result.isSuccess();
        if (success) {
            // FIXME re-enable Edit file link in a hack-free way
            // result += " <a href='/File.do?op=editFile&context=" + context + "&path=" + path + "&file=" + filename
            // + "&editor=codepress'>Edit</a></b>";
            /*
             * result += " <a href='simpleFileEditor.jsp?context=" + row.getRowname() + "&path=" + path + "&file=" +
             * path + java.io.File.separator + filename + "&editor=codepress'>Edit</a></b>";
             */
        }

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", success);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "file");
        return mapping.findForward("files");
    }

    public ActionForward newDir(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String path = request.getParameter("path");
        String file = request.getParameter("file");

        Response result = CommandController.onNewDir(context, path, file);

        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", result.isSuccess());
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("display", "file");
        return mapping.findForward("files");
    }

    public ActionForward upload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String context = request.getParameter("context");
        String path = request.getParameter("path");

        // Process the file upload form
        UploadForm upload = (UploadForm) form;
        String contentType = upload.getFileContentType();
        String fileName = upload.getFileName();
        int fileSize = upload.getFileSize();
        byte[] fileData = upload.getFileData();

        Response result = CommandController.onUpload(context, path, fileName, fileData);

        Boolean success = result.isSuccess();
        if (success) {
            request.setAttribute("contentType", contentType);
            request.setAttribute("contentLength", fileSize);
        }
        request.setAttribute("result", result.getMessage());
        request.setAttribute("success", success);
        request.setAttribute("context", context);
        request.setAttribute("path", path);
        request.setAttribute("file", fileName);
        return mapping.findForward("uploadResponse");
    }
}
