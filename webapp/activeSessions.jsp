<%@page import="org.makumba.parade.listeners.ParadeSessionListener"%>
<%@page import="java.util.List"%>
<%@page import="org.makumba.parade.model.User"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Date"%>
<html><head>Active sessions</head>
<body>
<h1>Active sessions</h1>
<% List<HttpSession> sessions = ParadeSessionListener.getActiveSessions();
for(int i=0;i<sessions.size();i++) {
	User u = (User) sessions.get(i).getAttribute("org.makumba.parade.userObject");
	String userInfo = "", attribList = "";
	if(u != null) {
		userInfo = u.getName() + " "+ u.getSurname() + " " + u.getNickname() + " " + u.getEmail();
	}

	Enumeration attribs = sessions.get(i).getAttributeNames();
	while(attribs.hasMoreElements()) {
		attribList += attribs.nextElement() + ", ";
	}
	 
	%>
	<b>ParaDe user:</b> <%=sessions.get(i).getAttribute("org.makumba.parade.user") %><br>
	<b>ParaDe user info:</b> <%= userInfo%><br>
	<b>Session creation time:</b> <%=new Date(sessions.get(i).getCreationTime()) %><br>
	<b>Last accessed time:</b> <%=new Date(sessions.get(i).getLastAccessedTime()) %><br>
	<b>Max inactive interval:</b> <%=sessions.get(i).getMaxInactiveInterval() %><br>
	<b>Context:</b> <%=sessions.get(i).getServletContext().getRealPath("/") %><br>
	<b>Attributes:</b> <%= attribList %><br>
	<hr>
	
	<%
}
%>
</body>
</html>