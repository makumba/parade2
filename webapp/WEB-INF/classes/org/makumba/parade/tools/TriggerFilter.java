package org.makumba.parade.tools;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

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

    // we need a context to be able to find others...
    private static ServletContext staticContext;
    
    private static ServletContext staticRootCtx;
    
    // guard that makes sure that we don't enter in an infinite logging loop
    private static ThreadLocal guard = new ThreadLocal() {
        public Object initialValue() {
            return false;
        }
    };

    public void init(FilterConfig conf) {
        context = conf.getServletContext();
        if(staticContext==null){
            staticContext= context;
        }
        
        beforeContext = conf.getInitParameter("beforeContext");
        beforeServlet = conf.getInitParameter("beforeServlet");
        afterContext = conf.getInitParameter("afterContext");
        afterServlet = conf.getInitParameter("afterServlet");
        
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
            checkCrossContext(req, beforeContext);
        } else {
            if (beforeServlet != null)
                invokeServlet(beforeServlet, ctx, dummyReq, resp);

        }

        req = (ServletRequest) dummyReq.getAttribute("org.eu.best.tools.TriggerFilter.request");

        if (req == null)
            // beforeServlet signaled closure
            return;

        resp = (ServletResponse) dummyReq.getAttribute("org.eu.best.tools.TriggerFilter.response");

        chain.doFilter(req, resp);

        ctx = context.getContext(afterContext);
        if (ctx == null) {
            checkCrossContext(req, afterContext);
        } else {

            if (afterServlet != null)
                invokeServlet(afterServlet, ctx, req, resp);
        }
    }

    private void checkCrossContext(ServletRequest req, String ctxName) {
        if (!((HttpServletRequest) req).getContextPath().equals("/manager"))
            System.out
                    .println("got null trying to search context "
                            + ctxName
                            + " from context "
                            + ((HttpServletRequest) req).getContextPath()
                            + " it may be that <DefaultContext crossContext=\"true\"/> is not configured in Tomcat's conf/server.xml, under Engine or Host");
    }

    public void destroy() {
    }

    private static void invokeServlet(String servletName,
            ServletContext ctx, ServletRequest req, ServletResponse resp) throws java.io.IOException, ServletException {
        ctx.getRequestDispatcher(servletName).include(req, resp);
    }

    // we need a vector so adding from multiple threads simmultaneously is safe
    static Vector<TriggerFilterQueueData> queue= new Vector<TriggerFilterQueueData>();
    
    public static void redirectToServlet(String servletName, Object attributeValue) {
        if (guard.get().equals(false)) {
            guard.set(true);
            try {
                TriggerFilterQueueData data= new TriggerFilterQueueData(servletName, attributeValue);
                if(staticRootCtx!=null)
                    data.sendTo(staticRootCtx);
                else{
                    // this happens only in the beggining
                    computeStaticRoot(data);
                }
                    
            } finally {
                guard.set(false);
            }
        }


    }
    
    private static synchronized void computeStaticRoot(TriggerFilterQueueData data) {
        if(staticRootCtx!=null){
            // probably staticRootCtx was set just afte we checked for it and just before we came into this method
            data.sendTo(staticRootCtx);
            return;
        }
        ServletContext ctx= null;
 
        queue.add(data);
        // here queue has at least one memmber!
        
        if(staticContext==null || (ctx= staticContext.getContext("/"))==null )
            return;
        
        // we have a root context, we try to send the first guy
        Iterator<TriggerFilterQueueData> i= queue.iterator();
        if(!i.next().sendTo(ctx))
            // root context is not ready to process
            return;
        // jackpot! the root context exists and is ready for action. we publish all shit before
        for(; i.hasNext();)
            i.next().sendTo(ctx);
 
        
        // now we are ready to publish the static so all other losers can use it without coming into synchronized code
        staticRootCtx=ctx;    
    }

    static class TriggerFilterQueueData {
    
    TriggerFilterQueueData(String servletName, Object attributeValue){
        this.servletName= servletName;
        this.attributeValue= attributeValue;
        this.prefix=PerThreadPrintStream.get();
    }
    
    boolean sendTo(ServletContext rootCtx){
        ServletRequest req= new HttpServletRequestDummy();
        if (attributeValue != null)
            req.setAttribute("org.makumba.parade.servletParam", attributeValue);
        if(prefix!=null)
            req.setAttribute("org.makumba.parade.logPrefix", prefix);
        try{
        invokeServlet(servletName, rootCtx, req, new HttpServletResponseDummy());
        } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        } catch (ServletException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
        return req.getAttribute("org.makumba.parade.servletSuccess")!=null;
    }
    
    private String prefix;
    
    private String servletName;

    private Object attributeValue;

}
}
