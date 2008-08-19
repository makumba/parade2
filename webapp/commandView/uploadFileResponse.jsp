<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<html>
<head>
<title>Create new directory</title>
<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/command.css" type="text/css">
</head>
<body class="command">

<%@include file="../setParameters.jspf" %>

<c:set var="contentType" value="${requestScope.contentType}" />
<c:set var="contentLength" value="${requestScope.contentLength}" />
<c:set var="saveFilePath" value="${requestScope.saveFilePath}" />

<h4>Uploading file:</h4>
file: ${context}/${path}<br>
content type: ${contentType}<br>
content length: ${contentLength}<br>
Saving to file: ${saveFilePath}...
<font color="green">Success!</font>


<%-- Refreshing the file browser view --%>
<script language="JavaScript">
<!-- 
top.frames["directory"].document.location.href="/commandView/commandOutput.jsp?context=${context}&path=${path}"
// -->
</script>
</body>
</html>