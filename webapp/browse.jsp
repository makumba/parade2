<%-- ParaDe browser view: composes the tree-file browser-command log view --%>
<%
	String context = request.getParameterValues("context")[0];
	if (context == null) {
		context = (String) request.getAttribute("context");
	}
	String opResult = request.getParameter("opResult");
	if (opResult == null) {
		opResult = (String) request.getAttribute("opResult");
	}

	String path = null;
	String getPathFromSession = request
			.getParameter("getPathFromSession");
	if (getPathFromSession != null) {
		path = (String) request.getSession().getAttribute("path");
	} else {
		path = request.getParameter("path");
	}
	if (path == null) {
		path = (String) request.getAttribute("path");
	}
	if (path == null) {
		path = "/";
	}
%>


<%@page import="org.makumba.parade.init.InitServlet"%>
<html>
<head>
<title><%=context%> browser</title>

</head>
<frameset rows="30,*">
	<frame name="header"
		src="/browserHeader.jsp?context=<%=context%>&amp;getPathFromSession=true"
		marginwidth="1" marginheight="1">
	<frameset cols="190,*">
		<frame name="tree"
			src="/servlet/browse?display=tree&amp;context=<%=context%>"
			marginwidth="0" marginheight="5">
		<frameset rows="*,25%">
			<frame name="directory"
				src="/fileView/fileBrowser.jsp?context=<%=context%>&amp;opResult=<%=opResult%>&amp;path=<%=path%>&amp;getPathFromSession=true">
			<%
				if (InitServlet.aetherEnabled) {
			%>
			<frame name="bottom" src="/aetherView.jsp" marginwidth="1"
				marginheight="1" scrolling="auto">
			<%
				} else {
			%>
			<frame name="command" src="/commandView/tipOfTheDay.jsp"
				marginwidth="1" marginheight="1">
			<%
				}
			%>
		</frameset>
	</frameset>
</frameset>
</html>