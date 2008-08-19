<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<%@include file="../setParameters.jspf" %>
<c:set var="refreshBrowser" value="${requestScope.refreshBrowser}" />

<html>
<head>
<title>Command output</title>
<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/command.css" type="text/css">
</head>
<body class="command">
<c:if test="${refreshBrowser}">
<script language='JavaScript'>
<!--
top.frames["directory"].document.location.href='/servlet/browse?display=file&context=${context}&path='+encodeURIComponent(${path})
// -->
</script>
</c:if>
${result}


</body>
</html>