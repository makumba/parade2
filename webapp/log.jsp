<%-- Log view: composes the view that shows the logs --%>
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
		src='/logHeader.jsp?logtype=log&amp;context=<%=context%>&amp;year=<%=request.getParameter("year")%>&amp;month=<%=request.getParameter("month")%>&amp;day=<%=request.getParameter("day")%>'>
	<frame name="logview"
		src='/servlet/logs?view=log&amp;context=<%=context%>&amp;year=<%=request.getParameter("year")%>&amp;month=<%=request.getParameter("month")%>&amp;day=<%=request.getParameter("day")%>'
		marginwidth="1" marginheight="1">
</frameset>
</html>