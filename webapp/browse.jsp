<%-- ParaDe browser view: composes the tree-file browser-command log view --%>
<%  String context = request.getParameterValues("context")[0];
	if(context == null)
	    context = (String) request.getAttribute("context");
	String opResult = request.getParameter("opResult");
	if(opResult == null)
	    opResult = (String) request.getAttribute("opResult");
	
    String path = null;
    String getPathFromSession = request.getParameter("getPathFromSession");
    if(getPathFromSession != null) {
        path = (String) request.getSession().getAttribute("path");
    } else {
        path = request.getParameter("path");
    }
    if (path == null)
        path = (String) request.getAttribute("path");
    if(path == null)
        path = "/";
%>


<%@page import="org.makumba.parade.init.InitServlet"%><HTML><HEAD><TITLE><%=context %> browser</TITLE>

</HEAD>
<% if(InitServlet.aetherEnabled) {%>
<FRAMESET rows="30,*">  
	<FRAME name="header" src="/browserHeader.jsp?context=<%=context %>&getPathFromSession=true" marginwidth="1" marginheight="1">
	<FRAMESET cols="190,*">
<FRAME name="tree" src="/servlet/browse?display=tree&context=<%=context %>" marginwidth="0" marginheight="5">

<FRAMESET cols="*,200">      
            <FRAMESET rows="*,20%">      
    			<FRAME name="directory" src="/fileView/fileBrowser.jsp?context=<%=context %>&opResult=<%=opResult %>&path=<%=path %>&getPathFromSession=true">
    			<FRAME name="command" src="/commandView/tipOfTheDay.jsp" marginwidth="1" marginheight="1">
    		</FRAMESET>
<FRAME name="aether" src="/aetherView.jsp">
  </FRAMESET>
</FRAMESET>
</FRAMESET>
<% } else { %>
<FRAMESET rows="30,*">  
  <FRAME name="header" src="/browserHeader.jsp?context=<%=context %>&getPathFromSession=true" marginwidth="1" marginheight="1">
  <FRAMESET cols="190,*">
    <FRAME name="tree" src="/servlet/browse?display=tree&context=<%=context %>" marginwidth="0" marginheight="5">
    <FRAMESET rows="*,20%">      
      <FRAME name="directory" src="/fileView/fileBrowser.jsp?context=<%=context %>&opResult=<%=opResult %>&path=<%=path %>&getPathFromSession=true">
      <FRAME name="command" src="/commandView/tipOfTheDay.jsp" marginwidth="1" marginheight="1">
    </FRAMESET>
  </FRAMESET>
</FRAMESET>
<%} %>
</HTML>