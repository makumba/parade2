
<%
	String context = null;
	Object ctxValues = request.getParameterValues("context");
	if (ctxValues != null)
		context = (String) (((Object[]) ctxValues))[0];
	if (context == null)
		context = "all";
%>

<HTML>
<HEAD>
<TITLE><%=context%> logs</TITLE>
</HEAD>
<FRAMESET rows="30,*">
	<FRAME name="logmenu" marginwidth="1" marginheight="1"
		noresize="noresize"
		src="/logHeader.jsp?logtype=actionlog&context=<%=context %>&year=<%=request.getParameter("year")%>&month=<%=request.getParameter("month")%>&day=<%=request.getParameter("day")%>">
	<FRAME name="logview"
		src="/actionLogList.jsp?context=<%=context %>&year=<%=request.getParameter("year")%>&month=<%=request.getParameter("month")%>&day=<%=request.getParameter("day")%>"
		marginwidth="1" marginheight="1">
</FRAMESET>
</HTML>