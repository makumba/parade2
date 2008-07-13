<%@page import="org.makumba.parade.init.InitServlet"%>
<%@page import="org.hibernate.Session"%>
<%@page import="org.hibernate.Transaction"%>
<%@page import="org.makumba.parade.model.Row"%>
<%@page import="org.makumba.parade.model.RowAnt"%>
<%@page import="java.util.List"%>
<%@page import="org.makumba.parade.model.RowWebapp"%>
<%@page import="org.makumba.parade.model.managers.ServletContainer"%>
<%@page import="org.makumba.db.hibernate.HibernatePointer"%>
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<html>
<head>
<title>Header view for row ${param.context}</title>

<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/header.css" type="text/css">
</head>
<body class="header">

<!-- Setting the parameters -->
<c:set var="context" value="${param.context}" />
<c:if test="${empty context}">
  <c:set var="context" value="${requestScope.context}" />
</c:if>

<c:choose>
  <c:when test="${not empty param.getPathFromSession and param.getPathFromSession}">
    <c:set var="path" value="${sessionScope.path}" />
  </c:when>
  <c:otherwise>
    <c:set var="path" value="${param.path}" />
    <c:if test="${empty path}">
      <c:set var="path" value="${requestScope.path}" />
    </c:if>
  </c:otherwise>
</c:choose>

<c:if test="${not empty path and path != ''}">
  <c:set var="path" value="${path}" scope="session" />
</c:if>



<mak:object from="Row row" where="row.rowname = :context">
  <mak:value expr="row.rowname" printVar="rowName" />

  <table class='header'>

    <form action="/browse.jsp?getPathFromSession=true" target="_top" style="margin: 0px;">
    <tr>
      <td valign="top"><a href='/' target='_top' title='back to front page'>&lt;</a> <select size='1'
        name='context' onchange="javascript:form.submit();">

        <mak:list from="Row r" where="r.moduleRow = false">
          <c:set var="currentRowName"><mak:value expr="r.rowname" /></c:set>
          <option value="${currentRowName}" <c:if test="${currentRowName eq rowName}"> selected</c:if>>${currentRowName}</option>
        </mak:list>
      </select> <input type='submit' value='Go!'></td>
      </form>
      <td valign="top">[<a href='/log.jsp?context=${rowName}' title='${rowName} log' target='_new'>log</a>] <a
        href='/log.jsp?context=all' title='Server log' target='directory'>all logs</a> <a href='/tomcat-docs'
        title='Tomcat documentation' target='directory'>Tomcat</a> <a href='http://www.makumba.org'
        title='Makumba documentation' target='directory'>Makumba</a></td>

      <mak:value expr="row.id" var="rowId" />
      <%
      	// Fetching the necessary data from the Hibernate model. This can't be done yet by Makumba, since the model uses inheritance, and this is not supported by Makumba.
      		Session s = null;
      		try {
      			// Creating a new Hibernate Session, which needs to be closed in any case at the end (in the finally block)
      			s = InitServlet.getSessionFactory().openSession();
      			// Starting a new transaction
      			Transaction tx = s.beginTransaction();

      			// We fetch the row
      			Row r = (Row) s.get(Row.class, new Long(
      					((HibernatePointer) rowId).getId()));

      			// Ant data - allowed operations
      			List<String> allowedOps = ((RowAnt) r.getRowdata().get(
      					"ant")).getAllowedOperations();

      			request.setAttribute("allowedOps", allowedOps);

      			// Webapp status data
      			RowWebapp data = (RowWebapp) r.getRowdata().get("webapp");

      			int status = data.getStatus().intValue();

      			if (r.getRowpath().equals(r.getParade().getBaseDir())) {
      				data.setStatus(new Integer(ServletContainer.RUNNING));
      				status = ServletContainer.RUNNING;
      			}

      			request.setAttribute("status", status);

      			// we commit the transaction
      			tx.commit();
      %>

      <td valign="top">&nbsp; ant: <c:forEach var="target" items="${requestScope.allowedOps}"
        varStatus="allowedOpsListStatus">
        <a href="/Ant.do?getPathFromSession=true&display=command&context=${rowName}&path=&op=${target}">${target}</a>
        <c:if test="${not allowedOpsListStatus.last}">, </c:if>
      </c:forEach></td>
      <td valign="top">&nbsp; webapp: <c:if test="${requestScope.status == 2}">
        <a
          href="/Webapp.do?display=command&context=${rowName}&path=${path}&op=servletContextReload&getPathFromSession=true">reload</a>
        <a
          href="/Webapp.do?display=command&context=${rowName}&path=${path}&op=servletContextStop&getPathFromSession=true">stop</a>
      </c:if> <c:if test="${requestScope.status == 1}">
        <a
          href="/Webapp.do?display=command&context=${rowName}&path=${path}&op=servletContextStart&getPathFromSession=true">start</a>
      </c:if> <c:if test="${requestScope.status != 0}">
        <a href="/Webapp.do?display=index&context=${rowName}&path=${path}&op=servletContextRedeploy">redeploy</a>
        <a
          href="/Webapp.do?display=command&context=${rowName}&path=${path}&op=servletContextRemove&getPathFromSession=true">uninstall</a>
      </c:if> <c:if test="${requestScope.status == 0}">
        <a
          href="/Webapp.do?display=command&context=${rowName}&path=${path}&op=servletContextInstall&getPathFromSession=true">install</a>
      </c:if></td>
      <td valign="top" style="float: right;" align="right">&nbsp;<a href="/systemInfo.jsp" target="directory">System</a></td>

    </tr>
  </table>

  <%
    // finally, we close the Hibernate Session
  	} finally {
  			if (s != null) {
  				s.close();
  			}
  		}
  %>


</mak:object>

</body>
</html>