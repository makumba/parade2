<%  String context = request.getParameterValues("context")[0];
	if(context == null)
	    context = (String) request.getAttribute("context");
	String opResult = request.getParameter("opResult");
	if(opResult == null)
	    opResult = (String) request.getAttribute("opResult");
	String path = request.getParameter("path");
	if(path == null)
	    path = (String) request.getAttribute("path");
%>

<HTML><HEAD><TITLE><%=context %> browser</TITLE>
</HEAD>
<FRAMESET rows="30,*">  
	<FRAME name="header" src="/servlet/browse?display=header&context=<%=context %>" marginwidth="1" marginheight="1">
	<FRAMESET cols="190,*">
		<FRAME name="tree" src="/servlet/browse?display=tree&context=<%=context %>" marginwidth="0" marginheight="5">
		<FRAMESET rows="*,20%">      
			<FRAME name="directory" src="/servlet/browse?display=file&context=<%=context %>&opResult=<%=opResult %>&path=<%=path %>">
			<FRAME name="command" src="/servlet/browse?display=command&context=<%=context %>" marginwidth="1" marginheight="1">
		</FRAMESET>
	</FRAMESET>
</FRAMESET>
</HTML>