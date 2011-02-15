<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<c:if test="${empty param.user}">
	<c:set var="user" value="${sessionScope.user_login}" />
</c:if>

<html>
<mak:object from="User u" where="u.login = :user">
	<head>
	<title>User information of <mak:value expr="u.name" /> <mak:value
		expr="u.surname" /></title>
	</head>
	<body>

	<mak:response />

	<h2>User profile of <mak:value expr="u.name" /> <mak:value
		expr="u.surname" /></h2>
	<c:if
		test="${empty param.user or param.user == sessionScope.user_login}">
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
				<tr>
					<td style="font-weight: bold;">Mentor</td>
					<td><mak:value expr="u.mentor.name" /> <mak:value
						expr="u.mentor.surname" /></td>
				</tr>
				<mak:list from="Row r" where="r.user.id = u.id" separator=" and ">
					<c:if test="${mak:maxCount() > 0}">
						<c:if test="${mak:count()==1}">
							<tr></tr>
							<td colspan="2">&nbsp;</td>
							<tr>
								<td colspan="2" style="font-weight: bold;">Proud owner of
								row
						</c:if>
						<mak:value expr="r.rowname" />
						<c:if test="${mak:count()==mak:maxCount()}">
							</td>
							</tr>
						</c:if>
					</c:if>
				</mak:list>
			</table>
			</td>
		</tr>
	</table>
</mak:object>

<br>
<c:if
	test="${empty param.user or param.user == sessionScope.user_login}">
	<a href="userEdit.jsp">Edit my info</a>
</c:if>

<br>
<br>
<br>
<a href="/">Back to the rows</a>
</body>
</html>
