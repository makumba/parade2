<%@ page isErrorPage="true" %>
<%@page import="org.makumba.parade.tools.ParadeException"%>
<%@page import="java.io.PrintStream"%>
<%@page import="java.util.Arrays"%>
<html>
<head>
<title>Relax, take it easy</title>
</head>
<%if(request.getAttribute("mak_error_title") != null) { %>
<h1><%=request.getAttribute("mak_error_title") %></h1>
<font color="green"><i><%=request.getAttribute("mak_error_description") %></i></font>
<br><br>
<% } else if(exception instanceof ParadeException) {%>
<h1>Sorry, a ParaDe error occured</h1>
<br>
An internal ParaDe error just happened, with following message:<br><br>
<font color="green"><i><%=exception.getMessage()%></i></font>
<br><br>
If this message doesn't help you, and keep on bugging you, please contact the developers.
<% } else { %>
<h1>An internal server error occured</h1>
<br>
An internal server error just happened, with following message:<br><br>
<font color="green"><i><%=exception.getMessage()%></i></font>
<br><br>
If this error keeps on showing up, or if you think that it should have a better error message, please save this HTML page and send it to the developers (use the "Save as..." feature of your browser).
<%}%>
<br>
<!--
<%=Arrays.toString(exception.getStackTrace())%>
-->
</html>