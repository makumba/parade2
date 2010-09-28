<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<html>
<head>
<title>Create new file</title>
<link rel="StyleSheet"
	href="${pageContext.request.contextPath}/layout/style/command.css"
	type="text/css">
</head>
<body class="command">

<%@include file="../setParameters.jspf"%>

<form action="/Command.do" target="directory" method="GET"><input
	type=hidden value="${context}" name=context> <input type=hidden
	value="newFile" name="op"> Create new file: <input type="text"
	name="params"> <input type=hidden value="${path}" name="path">
<input type=submit value=Create> <br>
<font style="font-size: smaller;">(in ${context}/${path})</font></form>

</body>
</html>