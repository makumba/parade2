<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%>
<html><head><title>Error during authentication</title></head>
<body>

<h1>Login failed due to an exception</h1>

<p>Sorry, your login to ParaDe failed. Please contact ITA@BEST.eu.org.

<% Throwable t = (Throwable) session.getAttribute("org.makumba.parade.authenticationError");
if(t != null) {
    t.fillInStackTrace();
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    String message = t.getMessage();
    String res = sw.toString();
%>
The error message is:</p>
<strong>Message:</strong><%=message %><br/><br/>
<strong>Stacktrace:</strong><%=res %>
<%} else {%>
</p>
<%} %>
</body>
</html>