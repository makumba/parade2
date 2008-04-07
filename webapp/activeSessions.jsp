<%@page import="org.makumba.parade.listeners.ParadeSessionListener"%>
<%@page import="java.util.List"%>
<%@page import="org.makumba.parade.model.User"%>
<%@page import="java.util.Enumeration"%>
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
		attribList += attribs.nextElement() + " ";
	}
	 
	%>
	Context: <%=sessions.get(i).getServletContext().getServletContextName() %><br>
	Context: <%=sessions.get(i).getServletContext().getRealPath("/") %><br>
	ParaDe user: <%=sessions.get(i).getAttribute("org.makumba.parade.user") %><br>
	ParaDe user info: <%= userInfo%><br>
	attributes: <%= attribList %>
	
	<%
}
%>
