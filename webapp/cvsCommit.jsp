<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="org.makumba.parade.aether.ObjectTypes"%>

<html>
<head>
<title>CVS commit</title>

<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/command.css" type="text/css">

</head>
<body class="command">

<c:set var="context" value="${param.context}" />
<c:if test="${empty context}">
  <c:set var="context" value="${requestScope.context}" />
</c:if>
<c:if test="${empty context}"><c:set var="context" value="${sessionScope.currentContext }"/></c:if>


<c:choose>
  <c:when test="${not empty param.getPathFromSession and param.getPathFromSession}">
    <c:set var="path" value="${sessionScope.path}" />
  </c:when>
  <c:otherwise>
    <c:set var="path" value="${param.path}" />
    <c:if test="${empty path}">
      <c:set var="path" value="${requestScope.path}" />
    </c:if>
  </c:otherwise>
</c:choose>

<c:if test="${not empty path and path ne ''}">
  <c:set var="path" value="${path}" scope="session" />
</c:if>

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