<%-- ParaDe browser header: shows the header of the browser view --%>
<%@page import="org.makumba.parade.model.Row"%>
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<jsp:useBean id="antBean" class="org.makumba.parade.view.beans.AntBean" />

<html>
<head>
<title>Header view for row ${param.context}</title>

<link rel="StyleSheet" href="/layout/style/header.css" type="text/css">
</head>
<body class="header">

	<!-- Setting the parameters -->
	<c:set var="context" value="${param.context}" />
	<c:if test="${empty context}">
		<c:set var="context" value="${requestScope.context}" />
	</c:if>

	<c:choose>
		<c:when
			test="${not empty param.getPathFromSession and param.getPathFromSession}">
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


	<table class="header">
		<mak:object from="Row row" where="row.rowname = :context">
			<mak:value expr="row.rowname" printVar="rowName" />
			<mak:value expr="row.id" printVar="rowId" />
			<mak:value expr="row.status" var="rowStatus" />
			<tr>
				<td valign="top">
					<form action="/browse.jsp?getPathFromSession=true" target="_top"
						style="margin: 0px;">
						<a href='/' target='_top' title='back to front page'>&lt;</a> <select
							size='1' name='context' onchange="javascript:form.submit();">

							<mak:list from="Row r" where="r.moduleRow = false"
								orderBy="r.rowname">
								<c:set var="currentRowName">
									<mak:value expr="r.rowname" />
								</c:set>
								<option value="${currentRowName}"
									<c:if test="${currentRowName eq rowName}"> selected</c:if>>${currentRowName}</option>
							</mak:list>
						</select> <input type='submit' value='Go!'>
					</form></td>

				<td><mak:if test="row.rowname != '(root)'">
						<mak:if test="row.status = 2">
							<a target="_top" href='<mak:value expr="row.rowname"/>'>(Surf)</a>
						</mak:if>
						<mak:if test="row.status != 2">(Surf)</mak:if>
					</mak:if>
				</td>


				<td><mak:if test="row.rowname != '(root)'">
						<a href="/Command.do?op=reset">(Reset DB)</a>
					</mak:if></td>

				<td valign="top">[<a href='/log.jsp?context=${rowName}'
					title='${rowName} log' target='_new'>log</a>] <a
					href='/log.jsp?context=all' title='Server log' target='directory'>all
						logs</a> <a href='/tomcat-docs' title='Tomcat documentation'
					target='directory'>Tomcat</a> <a href='http://www.makumba.org'
					title='Makumba documentation' target='directory'>Makumba</a>
				</td>

				<td valign="top">&nbsp; ant: <c:set var="vWhere">t.row = row and t.target in (<%=antBean.getAllowedAntOperations()%>)</c:set>
					<mak:list from="AntTarget t" where="#{vWhere}" separator=",">
						<a target="command"
							href="/Ant.do?display=command&context=<mak:value expr="row.rowname"/>&path=&op=<mak:value expr="t.target"/>"><mak:value
								expr="t.target" /> </a>
					</mak:list>
				</td>

				<td valign="top">&nbsp; webapp: <c:if test="${rowStatus == 2}">
						<a
							href="/Webapp.do?display=command&context=${rowName}&path=${path}&op=servletContextReload&getPathFromSession=true"
							target="command">reload</a>
						<a
							href="/Webapp.do?display=command&context=${rowName}&path=${path}&op=servletContextStop&getPathFromSession=true"
							target="command">stop</a>
					</c:if> <c:if test="${rowStatus == 1}">
						<a
							href="/Webapp.do?display=command&context=${rowName}&path=${path}&op=servletContextStart&getPathFromSession=true"
							target="command">start</a>
					</c:if> <c:if test="${rowStatus != 0}">
						<a
							href="/Webapp.do?display=command&context=${rowName}&path=${path}&op=servletContextRedeploy"
							target="command">redeploy</a>
						<a
							href="/Webapp.do?display=command&context=${rowName}&path=${path}&op=servletContextRemove&getPathFromSession=true"
							target="command">uninstall</a>
					</c:if> <c:if test="${rowStatus == 0}">
						<a
							href="/Webapp.do?display=command&context=${rowName}&path=${path}&op=servletContextInstall&getPathFromSession=true"
							target="command">install</a>
					</c:if>
				</td>

				<td valign="top" style="float: right;" align="right">&nbsp;<a
					href="/systemInfo.jsp" target="directory">System</a>
				</td>

				<td valign="top" style="float: right;" align="right"></td>
			</tr>
		</mak:object>
	</table>
</body>
</html>