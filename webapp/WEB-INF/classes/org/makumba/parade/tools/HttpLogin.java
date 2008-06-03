package org.makumba.parade.tools;

import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.makumba.parade.access.ActionLogDTO;
import org.makumba.parade.aether.ActionTypes;
import org.makumba.parade.auth.Authorizer;

/**
 * this class provides http login on a servlet according to a given authorization policy
 */
public class HttpLogin {
    protected Authorizer a;

    protected String realm;

    public HttpLogin(Authorizer a, String realm) {
        this.a = a;
        this.realm = realm;
    }

    public boolean login(ServletRequest req, ServletResponse res) throws java.io.IOException {
        String authString = ((HttpServletRequest) req).getHeader("Authorization");
        if (authString != null) {
            authString = new String(Base64.decode(authString.substring(6)));
            int n = authString.indexOf(':');
            if (checkAuth(authString.substring(0, n), authString.substring(n + 1), (HttpServletRequest) req))
                return true;
        }

        ((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ((HttpServletResponse) res).setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
        
        return false;
    }

    protected boolean checkAuth(String user, String pass, HttpServletRequest req) {
        boolean authenticated = a.auth(user, pass);
        if(authenticated) {
            logUserLogin(user);
        }
        
        return authenticated;
    }
    
    protected static void logUserLogin(String user) {
        
        ActionLogDTO log = new ActionLogDTO();
        log.setAction(ActionTypes.LOGIN.action());
        log.setDate(new Date());
        log.setUser(user);
        
        TriggerFilter.redirectToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", log);
        
    }
}
