<%@ taglib uri="http://www.makumba.org/presentation" prefix="mak"%>

<html>
<head>
<title>ParaDe administration interface: row configuration</title>
</head>
<body>

<table border="1">
	<thead>
		<th>Row</th>
		<th>User</th>
		<th>Action</th>
	</thead>

	<mak:list from="parade.Row r" where="r.user <> nil AND r.rowname <> '(root)' AND NOT (r.rowname like '%-module')" orderBy="r.rowname">
		<tr>
			<td><mak:value expr="r.rowname" /></td>
			<td><mak:value expr="r.user.name" /></td>
			<td><a href="rowEdit.jsp?row=<mak:value expr="r"/>">Edit</a></td>
		</tr>
	</mak:list>
	<mak:list from="parade.Row r" where="r.user = nil AND r.rowname <> '(root)' AND NOT (r.rowname like '%-module')" orderBy="r.rowname">
		<tr>
			<td><mak:value expr="r.rowname" /></td>
			<td></td>
			<td><a href="rowEdit.jsp?row=<mak:value expr="r"/>">Edit</a></td>
		</tr>
	</mak:list>
</table>
<br>
<a href="index.jsp">Back to admin interface</a>

</body>
</html>