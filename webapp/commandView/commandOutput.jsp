<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<%@include file="../setParameters.jspf" %>
<%-- This variable is used in order to indicate whether the browser frame should be refreshed or not.
It is used by the cvsCommit.jsp in some cases --%>
<c:set var="refreshBrowser" value="${empty param.refreshBrowser}" />

<html>
<head>
<title>Command output</title>
<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/command.css" type="text/css">
</head>
<body class="command" <c:if test="${refreshBrowser}">onLoad="javascript:top.frames['directory'].document.location.href='/fileView/fileBrowser.jsp?context=${context}&path='+encodeURIComponent('${path}');"</c:if>>
${result}
</body>
</html>