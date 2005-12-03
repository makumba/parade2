package org.makumba.parade.tools;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/** this class connects to a http site and tests if a password is ok */
public class ForeignHttpAuthorizer implements Authorizer {
    String url;

    public static HttpURLConnection sendAuth(URL u, String username,
            String password) throws java.io.IOException {
        HttpURLConnection uc = (HttpURLConnection) u.openConnection();
        uc.setRequestProperty("connection", "close");
        uc.setRequestProperty("Authorization", "Basic "
                + Base64.encode((username + ":" + password).getBytes()));
        uc.setUseCaches(false);
        uc.connect();
        return uc;
    }

    public ForeignHttpAuthorizer(String url) {
        this.url = url;
    }

    public boolean auth(String username, String password) {
        try {
            URLConnection uc = sendAuth(new URL(url), username, password);

            // trick, to make the server check the authorization
            uc.getInputStream().close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void main(String[] argv) {
        System.out.println(new ForeignHttpAuthorizer(
                "http://private.best.eu.org:3030/tigro/archive.nsf").auth(
                argv[0], argv[1]));
        System.out.println(new ForeignHttpAuthorizer(
                "http://private.best.eu.org:3030/tigro/archive.nsf").auth(
                "shit", "gugu"));

    }

}
