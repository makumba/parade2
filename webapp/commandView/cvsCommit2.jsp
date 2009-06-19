<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="org.makumba.parade.aether.ObjectTypes"%>

<html>
<head>
<title>CVS commit</title>

<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/command.css" type="text/css">

</head>
<body class="command">

<%@include file="../setParameters.jspf" %>

<%
	String[] files = request.getParameterValues("file");
%>

<table border="0">
  <tr>
    <td>
    <form style="display: inline;" target="command" action="/Cvs.do" method="GET">

<input type="hidden" value="${context}" name="context">
<input type="hidden" value="commit" name="op">
<input type="hidden" value="${path}" name="path"> 
<% if(files.length > 1) { %>
<input type="hidden" name="refreshBrowser" value="false">
<%} %>

<% for(String fileURL : files) { %>
<input type=hidden value="<%=fileURL %>" name="file">
<%} %>

Committing <strong><%for(String fileURL : files) {%><%=ObjectTypes.objectNameFromURL(fileURL)%> <%} %></strong> with message:<br>
    <textarea rows="3" cols="40" name="message"></textarea><br>
    <input type=submit value=Commit></form>
    </td>
    <td>&nbsp;</td>
    <td><b>Some guidelines for the commit message:</b><br>
    <ul>
      <li>if committing from context that is not yours, make sure to include your name</li>
      <li>make sure there are no line breaks on the commit message</li>
      <li>make sure that the commit message is meaningful</li>
    </ul>
    </td>
  </tr>
</table>
</body>
</html>