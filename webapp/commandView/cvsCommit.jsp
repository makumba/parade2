<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="org.makumba.parade.aether.ObjectTypes"%>

<html>
<head>
<title>CVS commit</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/layout/style/bottom.css" />
<link rel="StyleSheet" type="text/css"
	href="${pageContext.request.contextPath}/layout/style/command.css" />

</head>
<body class="command">

<%@include file="../setParameters.jspf"%>

<%
	String[] files = request.getParameterValues("file");
%>

<table width="100%" border="1" cellspacing="0" cellpadding="0"
	class="table_border">
	<tr>
		<td width="50%" valign="top" class="table_header">
		<p class="text_header">Commit files</p>
		</td>
		<td width="50%" valign="top" class="table_header">
		<p class="text_header">More information</p>
		</td>
	</tr>
	<tr class="table_row_1">
		<td width="50%" valign="top">
		<form style="display: inline;" target="command" action="/Cvs.do"
			method="GET"><input type="hidden" value="${context}"
			name="context"> <input type="hidden" value="commit" name="op">
		<input type="hidden" value="${path}" name="path"> <% if(files != null) { %>
		<% if(files.length > 1) { %> <input type="hidden" name="refreshBrowser"
			value="false"> <%} %> <% for(String fileURL : files) { %> <input
			type=hidden value="<%=fileURL %>" name="file"> <%} %>

		Committing <strong>
		<%for(String fileURL : files) {%><%=ObjectTypes.objectNameFromURL(fileURL)%>
		<%} %>
		</strong> with message:<br>
		<textarea rows="3" cols="40" name="message"></textarea><br>
		<input type=submit value=Commit></form>
		</td>
		<td width="50%"><b>Some guidelines for the commit message:</b><br>
		<ul>
			<li>if committing from context that is not yours, make sure to
			include your name</li>
			<li>make sure there are no line breaks on the commit message</li>
			<li>make sure that the commit message is meaningful</li>
		</ul>
		</td>
	</tr>
	<%} else { %>
	<tr class="table_row_1">
		<td width="100%" colspan="2" valign="top">There are no files to
		commit. Please select the files to commit on the previous page first.
		</td>
	</tr>
	<%} %>

	<tr class="table_row_1">
		<td width="100%" colspan="2" valign="top"><input
			onClick="history.back()" type="button" value="Back" /></td>
	</tr>
</table>
</body>
</html>