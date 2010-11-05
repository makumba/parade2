<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="org.makumba.parade.model.User"%>
<%@page import="org.makumba.parade.tools.ParadeLogger"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="org.makumba.parade.view.beans.AntBean"%>
<%@page import="org.makumba.parade.model.Row"%>
<%@page import="org.makumba.Pointer"%>
<%@page import="org.makumba.parade.view.ParadeRefreshPolicy"%>

<html>
<head>
<title>Welcome to ParaDe!</title>
<link rel="StyleSheet"
	href="${pageContext.request.contextPath}/layout/style/icons_silk.css"
	type="text/css" media="all" />
<link rel="StyleSheet"
	href="${pageContext.request.contextPath}/layout/style/rowstore.css"
	type="text/css">
<link rel="StyleSheet"
	href="${pageContext.request.contextPath}/layout/style/parade.css"
	type="text/css">
</head>
<body>
<center><%@include file="setParameters.jspf"%>

<%
	// set the User
	User u = (User) ((HttpServletRequest) request).getSession(true)
			.getAttribute("org.makumba.parade.userObject");
	if (u == null) {
		RequestDispatcher dispatcher = super.getServletContext()
				.getRequestDispatcher("/servlet/user");
		dispatcher.forward(request, response);
	}
%> <c:if test="${not empty result}">
	<c:choose>
		<c:when test="${not empty success}">
			<div
				class='<c:if test="${success}">success</c:if><c:if test="${not success}">failure</c:if>'>${result}</div>
			<br />
		</c:when>
		<c:otherwise>
			<div class='result'>${result}</div>
			<br />
		</c:otherwise>
	</c:choose>
</c:if>
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td align="left"><a class="icon_list" title="All the logs"
			href="log.jsp?context=all">[Log]</a>&nbsp;&nbsp; <a
			class="icon_project" title="All the action logs"
			href="actionLog.jsp?context=all">[Action log]</a>&nbsp;&nbsp; <strong><a
			class="icon_members"
			title="People who were active in the 20 past minutes">Currently
		online:</a></strong> <mak:list from="ActionLog a, User u"
			where="a.user = u.login and (unix_timestamp(a.logDate) &gt; ( unix_timestamp(now()) - 20 * 60))"
			groupBy="u.login">
			<a href='userView.jsp?user=<mak:value expr="u.login"/>'><mak:value
				expr="u.nickname" /></a>&nbsp;&nbsp;
</mak:list></td>
		<td align="right"><a class="icon_user_edit"
			title="See and modify your profile here" href="userView.jsp">My
		profile</a>&nbsp;&nbsp; <a class="icon_bug"
			href="mailto:parade-developers@lists.sourceforge.net"
			title="Report a bug">Report a bug</a> <a class="icon_logout"
			href="logout.jsp">Logout</a>&nbsp;&nbsp;</td>
	</tr>
</table>

<br>
Hi <%=u.getNickname()%>! Have a nice time on ParaDe!<br>
<br>
<br>
<br>
<table class='rowstore'>
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

	<mak:list from="Row row" orderBy="row.rowname asc" countVar="row_index">
		<tr
			class='<mak:if test="row.rowNotWatched()">notWatched</mak:if><mak:if test="row.rowWatched()"><c:choose><c:when test="${(row_index % 2) == 0}">odd</c:when><c:otherwise>even</c:otherwise></c:choose></mak:if>'>
			<td align='center'><mak:if test="row.rowNotWatched()">
				<img src="/images/exclamation.gif" alt="">&nbsp;This row is not watched by JNotify and won't work properly! Please restart ParaDe and read the logs to get more information.<br>
				<br>
			</mak:if> <a target="_top"
				href='/browse.jsp?context=<mak:value expr="row.rowname"/>'> <mak:value
				expr="row.rowname" /></a> <mak:if test="row.rowname = '(root)'">
				<a target="_top" href=''>(Surf)</a>
			</mak:if><mak:if test="row.rowname != '(root)'">
				<mak:if test="row.status = 2">
					<a target="_top" href='<mak:value expr="row.rowname"/>'>(Surf)</a>
				</mak:if>
				<mak:if test="row.status != 2">(Surf)</mak:if>
			</mak:if>&nbsp;<a href='log.jsp?context=<mak:value expr="row.rowname"/>'
				target='new'>(log)</a><br>
			<font style="font-size: smaller;"><mak:value
				expr="row.rowpath" /></font></td>
			<td><mak:value expr="row.description" /></td>
			<td><mak:value expr="row.cvsuser" /></td>
			<td><mak:value expr="row.module" /></td>
			<td><mak:value expr="row.branch" /></td>
			<td><mak:value expr="row.buildfile" /><br>

			<jsp:useBean id="antBean"
				class="org.makumba.parade.view.beans.AntBean" /> <mak:value
				expr="row.id" printVar="rowId" /> <c:set var="vWhere">t.row = row and t.target in (<%=antBean.getAllowedAntOperations()%>)</c:set>
			<mak:list from="AntTarget t" where="#{vWhere}" separator=", "
				orderBy="t.target">
				<a target="command"
					href='/Ant.do?display=index&amp;context=<mak:value expr="row.rowname"/>&amp;path=&amp;op=<mak:value expr="t.target"/>'><mak:value
					expr="t.target" /></a>
			</mak:list></td>
			<td><mak:value expr="row.webappPath" /></td>
			<td><mak:if test="row.status = 0">
				<div class="notinstalled">not installed</div>
			</mak:if> <mak:if test="row.status != 0">
				<div class="installed">installed</div>
			</mak:if> <mak:if test="row.status = 1">
				<div class="stopped">stopped</div>
			</mak:if> <mak:if test="row.status = 2">
				<div class="started">started</div>
			</mak:if> <mak:if test="row.status = 2">
				<a
					href='/Webapp.do?display=index&amp;context=<mak:value expr="row.rowname"/>&amp;path=<mak:value expr="row.webappPath"/>&amp;op=servletContextReload'>reload</a>
				<a
					href='/Webapp.do?display=index&amp;context=<mak:value expr="row.rowname"/>&amp;path=<mak:value expr="row.webappPath"/>&amp;op=servletContextStop'>stop</a>
			</mak:if> <mak:if test="row.status = 1">
				<a
					href='/Webapp.do?display=index&amp;context=<mak:value expr="row.rowname"/>&amp;path=<mak:value expr="row.webappPath"/>&amp;op=servletContextStart'>start</a>
			</mak:if> <mak:if test="row.status != 0">
				<a
					href='/Webapp.do?display=index&amp;context=<mak:value expr="row.rowname"/>&amp;path=<mak:value expr="row.webappPath"/>&amp;op=servletContextRedeploy'>redeploy</a>
				<a
					href='/Webapp.do?display=index&amp;context=<mak:value expr="row.rowname"/>&amp;path=<mak:value expr="row.webappPath"/>&amp;op=servletContextRemove'>uninstall</a>
			</mak:if> <mak:if test="row.status = 0">
				<a
					href='/Webapp.do?display=index&amp;context=<mak:value expr="row.rowname"/>&amp;path=<mak:value expr="row.webappPath"/>&amp;op=servletContextInstall'>install</a>
			</mak:if></td>
			<mak:value expr="row.version" printVar="version" />
			<td <%if (version.startswith("error")) {%> class="error" <%}%>><mak:value
				expr="row.version" /><br>
			<font style="font-size: smaller;"><mak:value expr="row.db" /></font>
			</td>
		</tr>
	</mak:list>
</table>
</center>
<br>
<br>
<br>
<a title="ParaDe TODO list" href="todo.jsp">ParaDe todo list</a>
</body>
</html>
