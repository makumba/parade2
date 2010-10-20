<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<%@include file="/setParameters.jspf"%>

<html>
<head>
<title>New user</title>
<link rel="StyleSheet"
	href="${13pageContext.getRequest().getContextPath()}/layout/style/parade.css"
	type="text/css">
</head>
<body>

<c:if test="not empty opResult">
	<div class='result'>${result}</div>
	<br />
</c:if>

<%
	String username = (String) ((HttpServletRequest) request)
			.getSession(true).getAttribute("org.makumba.parade.user");
	String name = "";
	int n = username.indexOf(".");
	if (n > -1) {
		name = username.substring(0, n);
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
	} else {
		name = username;
	}
	pageContext.setAttribute("login", username);
	pageContext.setAttribute("username", name);
%>

<h2>User account creation</h2>
Hi ${username}! It looks like ParaDe doesn't know you that well yet.
<br>
In the future, ParaDe will have features that allow better work
(development, testing), so we need you to feed it with some data about
yourself!
<br>
<font style="font-size: smaller;">(note that this information
will be connected to your login <i>${login}</i>)</font>
<br>
<br>
<form action="/User.do?op=newUser" method="POST">
<table>
	<tr>
		<td>Name</td>
		<td><input type="text" name="name"></td>
	</tr>
	<tr>
		<td>Surname</td>
		<td><input type="text" name="surname"></td>
	</tr>
	<tr>
		<td>Nickname</td>
		<td><input type="text" name="nickname"></td>
	</tr>
	<tr>
		<td>E-mail address</td>
		<td><input type="text" name="email"> (preferably Gmail if
		you have one - maybe we'll have a ParaDe Gtalk bot someday)</td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2" align="center"><input type="submit"
			value="Create my account!"></td>
	</tr>
</table>
</form>

</body>
</html>