package org.makumba.parade;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.makumba.parade.tools.PageContextDummy;

/**
 * The servlet called at the end of each parade access, in all servlet contexts
 * It just logs the access that was made.
 */
public class EndAccessServlet extends HttpServlet {
    ServletContext context;

    public void init(ServletConfig conf) {
        context = conf.getServletContext();
    }

    public void service(HttpServletRequest req, HttpServletResponse resp) {
        final ServletRequest req1 = req;
        final ServletResponse resp1 = resp;
        
        /*
        try {
            Config.invokeOperation("access", "accessMade",
                    new PageContextDummy() {
                        public ServletRequest getRequest() {
                            return req1;
                        }

                        public ServletResponse getResponse() {
                            return resp1;
                        }

                        public ServletContext getServletContext() {
                            return context;
                        }
                    });
                    
        } catch (ParadeException e) {
            throw new RuntimeException("got parade exception " + e);
        }
        */
    }
}
