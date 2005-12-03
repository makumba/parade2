package org.makumba.parade.tools;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * this class provides http login on a servlet according to a given
 * authorization policy
 */
public class HttpLogin {
    Authorizer a;

    String realm;

    public HttpLogin(Authorizer a, String realm) {
        this.a = a;
        this.realm = realm;
    }

    public boolean login(ServletRequest req, ServletResponse res)
            throws java.io.IOException {
        String authString = ((HttpServletRequest) req)
                .getHeader("Authorization");
        if (authString != null) {
            authString = new String(Base64.decode(authString.substring(6)));
            int n = authString.indexOf(':');
            if (checkAuth(authString.substring(0, n), authString
                    .substring(n + 1), (HttpServletRequest) req))
                return true;
        }

        ((HttpServletResponse) res)
                .setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ((HttpServletResponse) res).setHeader("WWW-Authenticate",
                "Basic realm=\"" + realm + "\"");
        res.setContentType("text/html");
        ServletOutputStream out = res.getOutputStream();
        out.println("<h1> Unauthorized </h1>");
        out.close();
        return false;
    }

    protected boolean checkAuth(String user, String pass, HttpServletRequest req) {
        return a.auth(user, pass);
    }
}
