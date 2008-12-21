<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<jsp:include page="/layout/header.jsp?pageTitle=Row list" />
<h1>Row list</h1>

<table>
	<thead>
		<th>Row</th>
		<th>User</th>
		<th>External user (Unison)</th>
		<th>Action</th>
	</thead>

	<mak:list from="Row r" where="r.user != null AND r.rowname != '(root)' AND NOT (r.rowname like '%-module')" orderBy="r.rowname">
		<tr>
			<td><mak:value expr="r.rowname" /></td>
			<td><mak:value expr="r.user.name" /></td>
			<td><mak:value expr="r.externalUser.name" /></td>
			<td><a class="icon_edit" href="rowEdit.jsp?row=<mak:value expr="r.id"/>"></a></td>
		</tr>
	</mak:list>
	<mak:list from="Row r" where="r.user = null AND r.rowname != '(root)' AND NOT (r.rowname like '%-module')" orderBy="r.rowname">
		<tr>
			<td><mak:value expr="r.rowname" /></td>
			<td></td>
			<td></td>
			<td><a class="icon_edit" href="rowEdit.jsp?row=<mak:value expr="r.id"/>"></a></td>
		</tr>
	</mak:list>
</table>
<br>
<a href="index.jsp">Back to admin interface</a>

<jsp:include page="/layout/footer.jsp" />
