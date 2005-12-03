package org.makumba.parade.tools;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpServletRequestDummy implements HttpServletRequest {
    static Enumeration emptyEnumeration = new Enumeration() {
        public boolean hasMoreElements() {
            return false;
        }

        public Object nextElement() {
            throw new java.util.NoSuchElementException();
        }
    };

    Hashtable map = new Hashtable();

    public void setAttribute(String name, Object o) {
        map.put(name, o);
    }

    public void removeAttribute(String name) {
        map.remove(name);
    }

    public Object getAttribute(String name) {
        return map.get(name);
    }

    public Enumeration getAttributeNames() {
        return map.keys();
    }

    public java.lang.String getCharacterEncoding() {
        return "8859-1";
    }

    public void setCharacterEncoding(java.lang.String s) {
    }

    public int getContentLength() {
        return 0;
    }

    public String getContentType() {
        return "text/plain";
    }

    public javax.servlet.ServletInputStream getInputStream() {
        return null;
    }

    public java.lang.String getParameter(java.lang.String s) {
        return null;
    }

    public java.lang.String[] getParameterValues(java.lang.String s) {
        return null;
    }

    public Enumeration getParameterNames() {
        return emptyEnumeration;
    }

    public java.util.Map getParameterMap() {
        return new HashMap(1);
    }

    public java.lang.String getProtocol() {
        return "http";
    }

    public java.lang.String getScheme() {
        return null;
    }

    public java.lang.String getServerName() {
        return "localhost";
    }

    public int getServerPort() {
        return -1;
    }

    public java.io.BufferedReader getReader() {
        return null;
    }

    public java.lang.String getRemoteHost() {
        return "";
    }

    public java.lang.String getRemoteAddr() {
        return "";
    }

    public java.util.Locale getLocale() {
        return null;
    }

    public Enumeration getLocales() {
        return emptyEnumeration;
    }

    public boolean isSecure() {
        return false;
    }

    public javax.servlet.RequestDispatcher getRequestDispatcher(
            java.lang.String s) {
        return null;
    }

    public java.lang.String getRealPath(java.lang.String a1) {
        return "";
    }

    public String getAuthType() {
        return null;
    }

    public Cookie[] getCookies() {
        return null;
    }

    public long getDateHeader(String name) {
        return -1l;
    }

    public String getHeader(String name) {
        return null;
    }

    public Enumeration getHeaders(String name) {
        return emptyEnumeration;
    }

    public Enumeration getHeaderNames() {
        return emptyEnumeration;
    }

    public int getIntHeader(String name) {
        return -1;
    }

    public String getMethod() {
        return "GET";
    }

    public String getPathInfo() {
        return "";
    }

    public String getPathTranslated() {
        return "";
    }

    public String getContextPath() {
        return "";
    }

    public String getQueryString() {
        return "";
    }

    public String getRemoteUser() {
        return null;
    }

    public boolean isUserInRole(String role) {
        return false;
    }

    public java.security.Principal getUserPrincipal() {
        return null;
    }

    public String getRequestedSessionId() {
        return null;
    }

    public String getRequestURI() {
        return "";
    }

    public StringBuffer getRequestURL() {
        return new StringBuffer("");
    }

    public String getServletPath() {
        return "";
    }

    public HttpSession getSession(boolean create) {
        return null;
    }

    public HttpSession getSession() {
        return null;
    }

    public boolean isRequestedSessionIdValid() {
        return false;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    public java.lang.String getLocalName() {
        return null;
    }

    public java.lang.String getLocalAddr() {
        return null;
    }

    public int getLocalPort() {
        return -1;
    }

    public int getRemotePort() {
        return -1;
    }
}
