<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<%@page import="org.makumba.aether.Aether"%>
<%@page import="org.makumba.parade.init.InitServlet"%>
<%@page import="org.makumba.aether.AetherEvent"%>
<%@page import="java.util.Date"%>
<%@page import="org.hibernate.Session"%>
<%@page import="org.hibernate.Transaction"%>
<%@page import="java.util.List"%>
<%@page import="org.makumba.aether.model.ALE"%>
<%@page import="java.util.Hashtable"%>
<%@page import="org.hibernate.Query"%>
<%@page import="java.util.Iterator"%><script src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
<% 
AetherEvent ae = new AetherEvent(request.getParameter("objectURL"), request.getParameter("objectType"), request.getParameter("user"), request.getParameter("action"), new Date(), 1.00);
InitServlet.getAether().registerEvent(ae, true);

Session s = null;
try {
  s = InitServlet.getSessionFactory().openSession();
  Transaction tx = s.beginTransaction();
  Query q = s.createQuery("select user as user, objectURL as objectURL, virtualNimbus as virtualNimbus, focus as focus, nimbus as nimbus, (virtualNimbus - nimbus) as contribution from ALE a where (virtualNimbus != 0) and (focus > 20) order by (virtualNimbus-nimbus) desc");
  List results = q.list();
  Iterator i = results.iterator();
  while(i.hasNext()) {
    Object[] resultRow = (Object[])i.next();
    String user = (String)resultRow[0];
    String objectURL = (String)resultRow[1];
    int virtualNimbus = (Integer)resultRow[2];
    int focus = (Integer)resultRow[3];
    int nimbus = (Integer)resultRow[4];
    int contribution = (Integer)resultRow[5];
    
    out.println(user + " ("+objectURL+") "+contribution+ " (current focus: "+focus +")<br>") ;
    
  }
  
  tx.commit();
    
  
} finally {
 if(s!= null) s.close(); 
}



%>

<% InitServlet.getAether().cleanVirtualPercolations(); %>