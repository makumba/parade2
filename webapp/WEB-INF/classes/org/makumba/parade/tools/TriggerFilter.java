package org.makumba.parade.tools;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.makumba.parade.tools.HttpServletRequestDummy;

/** This filter invokes a servlet before and another servlet after each access to a servlet context or an entire servlet engine. The servlets can be in any of the servlet contexts. If servlets from other contexts are used, in Tomcat, seerver.xml must include <DefaultContext crossContext="true"/>
 * When this class is used for all Tomcat contexts, it should be configured in tomcat/conf/web.xml, and should be available statically (e.g. in tomcat/common/classes. The great advantage is that all servlets that it invokes can be loaded dynamically.
 * beforeServlet and afterServlet are not invoked with the original request, but with a dummy request, that contains the original request, response and context as the attributes "org.eu.best.tools.TriggerFilter.request", "org.eu.best.tools.TriggerFilter.response", "org.eu.best.tools.TriggerFilter.context"
 * The beforeServlet can indicate that it whishes the chain not to be invoked by resetting the attribute "org.eu.best.tools.TriggerFilter.request" to null
 *@author Cristian Bogdan
*/
public class TriggerFilter implements Filter
{
  ServletContext context;
  String beforeContext, afterContext, beforeServlet, afterServlet;

  public void init(FilterConfig conf)
  {
    context=conf.getServletContext(); 
    beforeContext=conf.getInitParameter("beforeContext");
    beforeServlet=conf.getInitParameter("beforeServlet");
    afterContext=conf.getInitParameter("afterContext");
    afterServlet=conf.getInitParameter("afterServlet");
  }

  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws java.io.IOException, ServletException
  {
    ServletContext ctx=context.getContext(beforeContext);

    HttpServletRequest dummyReq= new HttpServletRequestDummy();
    dummyReq.setAttribute("org.eu.best.tools.TriggerFilter.request", req);
    dummyReq.setAttribute("org.eu.best.tools.TriggerFilter.response", resp);
    dummyReq.setAttribute("org.eu.best.tools.TriggerFilter.context", context);

    req.setAttribute("org.eu.best.tools.TriggerFilter.dummyRequest", dummyReq);
    req.setAttribute("org.eu.best.tools.TriggerFilter.request", req);
    req.setAttribute("org.eu.best.tools.TriggerFilter.response", resp);
    req.setAttribute("org.eu.best.tools.TriggerFilter.context", context);

    if(ctx==null){
        if(!((HttpServletRequest)req).getContextPath().equals("/manager"))
            System.out.println("got null trying to search context "+beforeContext+" from context " +((HttpServletRequest)req).getContextPath()+" it may be that <DefaultContext crossContext=\"true\"/> is not configured in Tomcat's conf/server.xml, under Engine or Host");
    }
    else
      if(beforeServlet!=null)
          ctx.getRequestDispatcher(beforeServlet).include(dummyReq, resp);
    
    req=(ServletRequest)dummyReq.getAttribute("org.eu.best.tools.TriggerFilter.request");
    
    if(req==null)
      // beforeServlet signaled closure
      return;

    resp=(ServletResponse)dummyReq.getAttribute("org.eu.best.tools.TriggerFilter.response");
    
    chain.doFilter(req, resp);

    ctx=context.getContext(afterContext);
    if(ctx==null){
        if(!((HttpServletRequest)req).getContextPath().equals("/manager"))
            System.out.println("got null trying to search context "+beforeContext+" from context " +((HttpServletRequest)req).getContextPath()+" it may be that <DefaultContext crossContext=\"true\"/> is not configured in Tomcat's conf/server.xml, under Engine or Host");
    }
    else
      if(afterServlet!=null)
          ctx.getRequestDispatcher(afterServlet).include(req, resp);
  }

  public void destroy(){}
}
