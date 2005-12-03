package org.makumba.parade.tools;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/** sometimes you need a page context and can't get one... here's a dummy */
public class PageContextDummy extends PageContext {
    public void initialize(Servlet servlet, ServletRequest request,
            ServletResponse response, String errorPageURL,
            boolean needsSession, int bufferSize, boolean autoFlush)
            throws IOException, IllegalStateException, IllegalArgumentException {
    }

    public void release() {
    }

    Hashtable attributes = new Hashtable();

    public void setAttribute(String name, Object attribute) {
        attributes.put(name, attribute);
    }

    public void setAttribute(String name, Object o, int scope) {
        if (scope == PAGE_SCOPE) {
            setAttribute(name, o);
            return;
        }
        if (scope == REQUEST_SCOPE && getRequest() != null) {
            getRequest().setAttribute(name, o);
            return;
        }
        if (scope == SESSION_SCOPE && getSession() != null) {
            getSession().setAttribute(name, o);
            return;
        }
        if (scope == APPLICATION_SCOPE && getServletContext() != null) {
            getServletContext().setAttribute(name, o);
            return;
        }
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Object getAttribute(String name, int scope) {
        if (scope == PAGE_SCOPE)
            return getAttribute(name);
        if (scope == REQUEST_SCOPE && getRequest() != null) {
            return getRequest().getAttribute(name);
        }
        if (scope == SESSION_SCOPE && getSession() != null) {
            return getSession().getAttribute(name);
        }
        if (scope == APPLICATION_SCOPE && getServletContext() != null) {
            return getServletContext().getAttribute(name);
        }
        return null;
    }

    public Object findAttribute(String name) {
        int scope = getAttributesScope(name);
        return scope == 0 ? null : getAttribute(name, scope);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public void removeAttribute(String name, int scope) {
        if (scope == PAGE_SCOPE) {
            removeAttribute(name);
            return;
        }
        if (scope == REQUEST_SCOPE && getRequest() != null) {
            getRequest().removeAttribute(name);
            return;
        }
        if (scope == SESSION_SCOPE && getSession() != null) {
            getSession().removeAttribute(name);
            return;
        }
        if (scope == APPLICATION_SCOPE && getServletContext() != null) {
            getServletContext().removeAttribute(name);
            return;
        }
    }

    public int getAttributesScope(String name) {
        if (getAttribute(name) != null)
            return PAGE_SCOPE;
        if (getRequest() != null && getRequest().getAttribute(name) != null)
            return REQUEST_SCOPE;
        if (getSession() != null && getSession().getAttribute(name) != null)
            return SESSION_SCOPE;
        if (getServletContext() != null
                && getServletContext().getAttribute(name) != null)
            return APPLICATION_SCOPE;
        return 0;
    }

    public Enumeration getAttributeNamesInScope(int scope) {
        if (scope == PAGE_SCOPE) {
            return attributes.keys();
        }
        if (scope == REQUEST_SCOPE && getRequest() != null) {
            return getRequest().getAttributeNames();
        }
        if (scope == SESSION_SCOPE && getSession() != null) {
            return getSession().getAttributeNames();
        }
        if (scope == APPLICATION_SCOPE && getServletContext() != null) {
            return getServletContext().getAttributeNames();
        }
        return null;
    }

    public JspWriter getOut() {
        return null;
    }

    public HttpSession getSession() {
        return (getRequest() != null) ? ((HttpServletRequest) getRequest())
                .getSession() : null;
    }

    public Object getPage() {
        return null;
    }

    public ServletRequest getRequest() {
        return null;
    }

    public ServletResponse getResponse() {
        return null;
    }

    public Exception getException() {
        return null;
    }

    public ServletConfig getServletConfig() {
        return (getPage() != null) ? ((Servlet) getPage()).getServletConfig()
                : null;
    }

    public ServletContext getServletContext() {
        return (getServletConfig() != null) ? getServletConfig()
                .getServletContext() : null;
    }

    public void forward(String relativeUrlPath) throws ServletException,
            IOException {
        if (getServletContext() == null || getRequest() == null
                || getResponse() == null)
            return; // or mb throw exception?
        getServletContext().getRequestDispatcher(relativeUrlPath).forward(
                getRequest(), getResponse());
    }

    public void include(String relativeUrlPath) throws ServletException,
            IOException {
        if (getServletContext() == null || getRequest() == null
                || getResponse() == null)
            return; // or mb throw exception?
        getServletContext().getRequestDispatcher(relativeUrlPath).include(
                getRequest(), getResponse());
    }

    public void handlePageException(Exception e) throws ServletException,
            IOException {
    }

    public void handlePageException(Throwable t) throws ServletException,
            IOException {
    }

    public javax.servlet.jsp.tagext.BodyContent pushBody() {
        return null;
    }

    public JspWriter popBody() {
        return null;
    }

    // new method in tomcat 5
    public void include(java.lang.String a, boolean b) {
    }

    public javax.servlet.jsp.el.ExpressionEvaluator getExpressionEvaluator() {
        return null;
    }

    public javax.servlet.jsp.el.VariableResolver getVariableResolver() {
        return null;
    }
}
