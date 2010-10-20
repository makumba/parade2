<%-- ActionLog view: displays the ActionLogs of a specific context (or of all context if none provided), i.e. the header and the list --%>
<%
	String context = null;
	Object ctxValues = request.getParameterValues("context");
	if (ctxValues != null)
		context = (String) (((Object[]) ctxValues))[0];
	if (context == null)
		context = "all";
%>

<html>
<head>
<title><%=context%> logs</title>
</head>
<frameset rows="30,*">
	<frame name="logmenu" marginwidth="1" marginheight="1"
		noresize="noresize"
		src='/logHeader.jsp?logtype=actionlog&amp;context=<%=context%>&amp;year=<%=request.getParameter("year")%>&amp;month=<%=request.getParameter("month")%>&amp;day=<%=request.getParameter("day")%>'>
	<frame name="logview"
		src='/actionLogList.jsp?context=<%=context%>&amp;year=<%=request.getParameter("year")%>&amp;month=<%=request.getParameter("month")%>&amp;day=<%=request.getParameter("day")%>'
		marginwidth="1" marginheight="1">
</frameset>
</html>