package org.makumba.parade.tools;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.makumba.parade.tools.HttpServletRequestDummy;

/**
 * This filter invokes a servlet before and another servlet after each access to a servlet context or an entire servlet
 * engine. The servlets can be in any of the servlet contexts. If servlets from other contexts are used, in Tomcat,
 * seerver.xml must include <DefaultContext crossContext="true"/> When this class is used for all Tomcat contexts, it
 * should be configured in tomcat/conf/web.xml, and should be available statically (e.g. in tomcat/common/classes. The
 * great advantage is that all servlets that it invokes can be loaded dynamically. beforeServlet and afterServlet are
 * not invoked with the original request, but with a dummy request, that contains the original request, response and
 * context as the attributes "org.eu.best.tools.TriggerFilter.request", "org.eu.best.tools.TriggerFilter.response",
 * "org.eu.best.tools.TriggerFilter.context" The beforeServlet can indicate that it whishes the chain not to be invoked
 * by resetting the attribute "org.eu.best.tools.TriggerFilter.request" to null
 * 
 * @author Cristian Bogdan
 */
public class TriggerFilter implements Filter {
    ServletContext context;

    String beforeContext, afterContext, beforeServlet, afterServlet;

    private static ThreadLocal<ServletRedirectionData> redirectionData = new ThreadLocal<ServletRedirectionData>();

    private static ServletRedirectionData staticRedirectionData = null;

    private static List<TriggerFilterQueueData> messageQueue = new LinkedList<TriggerFilterQueueData>();
    
    // guard that makes sure that we don't enter in an infinite logging loop
    private static ThreadLocal guard = new ThreadLocal() {
        public Object initialValue() {
            return false;
        }
    };

    public void init(FilterConfig conf) {
        context = conf.getServletContext();
        beforeContext = conf.getInitParameter("beforeContext");
        beforeServlet = conf.getInitParameter("beforeServlet");
        afterContext = conf.getInitParameter("afterContext");
        afterServlet = conf.getInitParameter("afterServlet");

        ServletRedirectionData data = new ServletRedirectionData();
        if (conf.getServletContext() != null) {
            data.setContext(conf.getServletContext());
            data.setReq(new HttpServletRequestDummy());
            data.setResp(new HttpServletResponseDummy());
            redirectionData.set(data);
            if (staticRedirectionData == null)
                staticRedirectionData = data;
        }
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws java.io.IOException,
            ServletException {

        PerThreadPrintStream.setEnabled(true);

        ServletContext ctx = context.getContext(beforeContext);

        HttpServletRequest dummyReq = new HttpServletRequestDummy();
        dummyReq.setAttribute("org.eu.best.tools.TriggerFilter.request", req);
        dummyReq.setAttribute("org.eu.best.tools.TriggerFilter.response", resp);
        dummyReq.setAttribute("org.eu.best.tools.TriggerFilter.context", context);

        req.setAttribute("org.eu.best.tools.TriggerFilter.dummyRequest", dummyReq);
        req.setAttribute("org.eu.best.tools.TriggerFilter.request", req);
        req.setAttribute("org.eu.best.tools.TriggerFilter.response", resp);
        req.setAttribute("org.eu.best.tools.TriggerFilter.context", context);

        if (ctx == null) {
            if (!((HttpServletRequest) req).getContextPath().equals("/manager"))
                System.out
                        .println("got null trying to search context "
                                + beforeContext
                                + " from context "
                                + ((HttpServletRequest) req).getContextPath()
                                + " it may be that <DefaultContext crossContext=\"true\"/> is not configured in Tomcat's conf/server.xml, under Engine or Host");
        } else {

            // we store everything in a threadLocal we need to do a "duringServlet"
            ServletRedirectionData data = new ServletRedirectionData();
            data.setContext(ctx);
            data.setReq(req);
            data.setResp(resp);
            redirectionData.set(data);

            if (beforeServlet != null)
                redirectToServlet(beforeServlet, null, null, ctx, dummyReq, resp);

        }

        req = (ServletRequest) dummyReq.getAttribute("org.eu.best.tools.TriggerFilter.request");

        if (req == null)
            // beforeServlet signaled closure
            return;

        resp = (ServletResponse) dummyReq.getAttribute("org.eu.best.tools.TriggerFilter.response");

        chain.doFilter(req, resp);

        ctx = context.getContext(afterContext);
        if (ctx == null) {
            if (!((HttpServletRequest) req).getContextPath().equals("/manager"))
                System.out
                        .println("got null trying to search context "
                                + beforeContext
                                + " from context "
                                + ((HttpServletRequest) req).getContextPath()
                                + " it may be that <DefaultContext crossContext=\"true\"/> is not configured in Tomcat's conf/server.xml, under Engine or Host");
        } else {

            if (afterServlet != null)
                redirectToServlet(afterServlet, null, null, ctx, req, resp);
        }
    }

    public void destroy() {
    }

    private static void redirectToServlet(String servletName, String attributeName, Object attributeValue,
            ServletContext ctx, ServletRequest req, ServletResponse resp) throws java.io.IOException, ServletException {

        if (req != null && attributeName != null && attributeValue != null)
            req.setAttribute(attributeName, attributeValue);

        ctx.getRequestDispatcher(servletName).include(req, resp);
        
    }

    public static void redirectToServlet(String servletName, String attributeName, Object attributeValue) {

        if (guard.get().equals(false)) {
            guard.set(true);
            try {
                ServletRedirectionData data = redirectionData.get();
                if (data == null)
                    if (staticRedirectionData == null) {
                        //addToQueue(servletName, attributeName, attributeValue);
                        return;
                    } else
                        data = staticRedirectionData;

                try {
                    ServletContext rootCtx = data.getContext().getContext("/");
                    if (rootCtx == null) {
                        //addToQueue(servletName, attributeName, attributeValue);
                        return;
                    }
                    
                    // at this point we make sure that we can actually redirect without problem
                    // so we process the queue
                   /* if(PerThreadPrintStream.canWriteToDb.get()) {
                        int i=0;
                        while(!messageQueue.isEmpty()) {
                            TriggerFilterQueueData queueElement = messageQueue.get(i);
                            redirectToServlet(queueElement.getServletName(), queueElement.getAttributeName(), queueElement.getAttributeValue(), rootCtx, data.getReq(), data.getResp());
                            messageQueue.remove(i);
                            i++;
                        }
                    }*/
                    redirectToServlet(servletName, attributeName, attributeValue, rootCtx, data.getReq(), data
                            .getResp());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ServletException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } finally {
                guard.set(false);
            }
        }
    }

    private static void addToQueue(String servletName, String attributeName, Object attributeValue) {
        TriggerFilterQueueData data = new TriggerFilterQueueData();
        data.setServletName(servletName);
        data.setAttributeName(attributeName);
        data.setAttributeValue(attributeValue);
        messageQueue.add(data);
    }

}

class ServletRedirectionData {

    private ServletContext context;

    private ServletRequest req;

    private ServletResponse resp;

    public ServletContext getContext() {
        return context;
    }

    public void setContext(ServletContext context) {
        this.context = context;
    }

    public ServletRequest getReq() {
        return req;
    }

    public void setReq(ServletRequest req) {
        this.req = req;
    }

    public ServletResponse getResp() {
        return resp;
    }

    public void setResp(ServletResponse resp) {
        this.resp = resp;
    }

}

class TriggerFilterQueueData {

    private String servletName;

    private String attributeName;

    private Object attributeValue;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Object getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(Object attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

}
