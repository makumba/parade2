package org.makumba.parade.access;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.makumba.parade.tools.PerThreadPrintStream;

public class PerThreadPrintStreamEnablerServlet extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object status = req.getAttribute("perThreadEnabled");
        
        if(status == null)
            return;
        
        Boolean b = (Boolean) status;
        
        PerThreadPrintStream.setEnabled(b);

    }
}
