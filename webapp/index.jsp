<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache" %>
<%@page import="org.makumba.parade.model.User"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="org.makumba.parade.view.beans.IndexBean"%>
<%@page import="org.makumba.parade.model.Row"%>
<%@page import="org.makumba.Pointer"%>

<%@page import="org.makumba.parade.view.ParadeRefreshPolicy"%><html>
<head>
<title>Welcome to ParaDe!</title>
  <link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/icons_silk.css" type="text/css" media="all"/>
  <link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/rowstore.css" type="text/css">
  <link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/parade.css" type="text/css">
</head>
<body>
<CENTER>
<%@include file="setParameters.jspf" %>

<%
// set the User
User u = (User) ((HttpServletRequest) request).getSession(true).getAttribute("org.makumba.parade.userObject");
if (u == null) {
    RequestDispatcher dispatcher = super.getServletContext().getRequestDispatcher("/servlet/user");
    dispatcher.forward(request, response);
}

%>

<c:if test="${not empty result}">
  <c:choose>
  <c:when test="${not empty success}"><div class='<c:if test="${success}">success</c:if><c:if test="${not success}">failure</c:if>'>${result}</div><br/></c:when>
  <c:otherwise>
    <div class='result'>${result}</div><br/>
  </c:otherwise>
  </c:choose>
</c:if>
<table width="100%" cellpadding="0" cellspacing="0" >
<tr>
<td align="left">
<a class="icon_list" title="All the logs" href="log.jsp?context=all">[Log]</a>&nbsp;&nbsp;
<a class="icon_project" title="All the action logs" href="actionLog.jsp?context=all">[Action log] (beta)</a>&nbsp;&nbsp;
<strong><a class="icon_members" title="People who were active in the 20 past minutes">Currently online:</a></strong> 

<%
Calendar cal = Calendar.getInstance();
cal.setTime(new Date());
cal.add(Calendar.MINUTE, -20);
request.setAttribute("myDate", cal.getTime()); %>
<mak:list from="ActionLog a, User u" where="a.user = u.login and a.logDate > :myDate" groupBy="u.login">
<a href='userView.jsp?user=<mak:value expr="u.login"/>'><mak:value expr="u.nickname"/></a>&nbsp;&nbsp;
</mak:list>
</td>
<td align="right">
<a class="icon_user_edit" title="See and modify your profile here" href="userView.jsp">My profile</a>&nbsp;&nbsp;
<a class="icon_bug" href="mailto:parade-developers@lists.sourceforge.net" title="Report a bug">Report a bug</a>
<a class="icon_logout" href="logout.jsp">Logout</a>&nbsp;&nbsp;
</td>
</tr>
</table>

<br>
Hi <%=u.getNickname() %>! Have a nice time on ParaDe!</div><br><br><br>

<c:set var="row_cache_key"><%=ParadeRefreshPolicy.ROW_CACHE_KEY%></c:set>

<cache:cache key="${row_cache_key}" scope="application" refreshpolicyclass="org.makumba.parade.view.ParadeRefreshPolicy">

<border class='rowstore'>

<br>
<TABLE class='rowstore'>
<tr>
<th class='rowstore'>Name, Path</th>
<th class='rowstore'>Description</th>
<th class='rowstore'>CVS user</th>
<th class='rowstore'>module</th>
<th class='rowstore'>branch</th>
<th class='rowstore'>Ant buildfile</th>
<th class='rowstore'>Webapp path</th>
<th class='rowstore'>Webapp status</th>
<th class='rowstore'>Makumba version</th>

</tr>

<mak:list from="Row row" countVar="row_index">
<tr class="<mak:if test="row.rowNotWatched()">notWatched</mak:if><mak:if test="row.rowWatched()"><c:choose><c:when test="${(row_index % 2) == 0}">odd</c:when><c:otherwise>even</c:otherwise></c:choose></mak:if>">
<td align='center'>
<mak:if test="row.rowNotWatched()"><img src="/images/exclamation.gif">&nbsp;This row is not watched by JNotify and won't work properly! Please restart ParaDe and read the logs to get more information.<br><br></mak:if>
<a href='/browse.jsp?context=<mak:value expr="row.rowname"/>'><mak:value expr="row.rowname"/></a> <mak:if test="row.rowname = '(root)'"><a href=''>(Surf)</a></mak:if><mak:if test="row.rowname != '(root)'"><mak:if test="row.status = 2"><a href='<mak:value expr="row.rowname"/>'>(Surf)</a></mak:if><mak:if test="row.status != 2">(Surf)</mak:if></mak:if>&nbsp;<a href='log.jsp?context=<mak:value expr="row.rowname"/>' target='new'>(log)</a><br>
<font style="font-size:smaller;"><mak:value expr="row.rowpath"/></font></td>
<td><mak:value expr="row.description"/></td>
<td><mak:value expr="row.cvsuser"/></td>
<td><mak:value expr="row.module"/></td>
<td><mak:value expr="row.branch"/></td>
<td><mak:value expr="row.buildfile"/><br>

<jsp:useBean id="indexBean" class="org.makumba.parade.view.beans.IndexBean" />
<mak:value expr="row.id" printVar="rowId"/>
<% if(request.getAttribute("antOperations" + rowId) == null) {
request.setAttribute("antOperations" + rowId, indexBean.getAntOperations(new Pointer("Row",rowId)));
}%>
<c:forEach var="target" items="${antOperations}"
  varStatus="allowedOpsListStatus">
  <a target="command" href="/Ant.do?display=index&context=<mak:value expr="row.rowname"/>&path=&op=${target}">${target}</a>
  <c:if test="${not allowedOpsListStatus.last}">, </c:if>
</c:forEach></td>

</td>
<td><mak:value expr="row.webappPath"/></td>
<td>
<mak:if test="row.status = 0"><div class="notinstalled">not installed</div></mak:if>
<mak:if test="row.status != 0"><div class="installed">installed</div></mak:if>
<mak:if test="row.status = 1"><div class="stopped">stopped</div></mak:if>
<mak:if test="row.status = 2"><div class="started">started</div></mak:if>

<mak:if test="row.status = 2">
<a href='/Webapp.do?display=index&context=<mak:value expr="row.rowname"/>&path=<mak:value expr="row.webappPath"/>&op=servletContextReload'>reload</a>
<a href='/Webapp.do?display=index&context=<mak:value expr="row.rowname"/>&path=<mak:value expr="row.webappPath"/>&op=servletContextStop'>stop</a>        
</mak:if>
<mak:if test="row.status = 1">
<a href='/Webapp.do?display=index&context=<mak:value expr="row.rowname"/>&path=<mak:value expr="row.webappPath"/>&op=servletContextStart'>start</a>
</mak:if>
<mak:if test="row.status != 0">
<a href='/Webapp.do?display=index&context=<mak:value expr="row.rowname"/>&path=<mak:value expr="row.webappPath"/>&op=servletContextRedeploy'>redeploy</a>  
<a href='/Webapp.do?display=index&context=<mak:value expr="row.rowname"/>&path=<mak:value expr="row.webappPath"/>&op=servletContextRemove'>uninstall</a>
</mak:if>
<mak:if test="row.status = 0">
<a href='/Webapp.do?display=index&context=<mak:value expr="row.rowname"/>&path=<mak:value expr="row.webappPath"/>&op=servletContextInstall'>install</a>
</mak:if>
</td>
<mak:value expr="row.version" printVar="version"/>
<td <%if(version.startsWith("Error")){%>class="error"<%}%>><mak:value expr="row.version"/><br>
<font style="font-size:smaller;"><mak:value expr="row.db"/></font>
</td>
</tr>
</mak:list>
</TABLE>
</CENTER>
<br><br><br>
<a title="ParaDe TODO list" href="todo.jsp">ParaDe</a>
</cache:cache>
</body>
</html>
