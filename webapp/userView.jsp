<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<c:if test="${empty param.user}">
  <c:set var="user" value="${user_login}" />
</c:if>

<mak:object from="parade.User u" where="u.login=:user">


<html>
<head>
<title>User information of <mak:value expr="u.name"/> <mak:value expr="u.surname"/></title>
</head>
<body>

<mak:response />

<h2>User profile of <mak:value expr="u.name"/> <mak:value expr="u.surname"/></h2>
<c:if test="${empty param.user or param.user == user_login}">
Here's the information ParaDe knows about you.
</c:if>
<br>
<br>
	<table>
		<tr>

			<td><img src="showImage.jsp?user=<mak:value expr="u.id"/>">
			</td>


			<td>


			<table>
				<tr>
					<td style="font-weight: bold;">Name</td>
					<td><mak:value expr="u.name" /></td>
				</tr>
				<tr>
					<td style="font-weight: bold;">Surname</td>
					<td><mak:value expr="u.surname" /></td>
				</tr>
				<tr>
					<td style="font-weight: bold;">Nickname</td>
					<td><mak:value expr="u.nickname" /></td>
				</tr>
				<tr>
					<td style="font-weight: bold;">E-mail</td>
					<td><mak:value expr="u.email" /></td>
				</tr>
				<tr>
					<td style="font-weight: bold;">CVS user</td>
					<td><mak:value expr="u.cvsuser" /></td>
				</tr>
			</table>

			</td>
		</tr>
	</table>

</mak:object>

<br>
<c:if test="${empty param.user or param.user == user_login}">
<a href="userEdit.jsp">Edit my info</a>
</c:if>

<br>
<br>
<br>
<a href="/">Back to the rows</a>



</body>
</html>