<%@ taglib uri="http://www.makumba.org/presentation" prefix="mak"%>

<html>
<head>
<title>ParaDe administration interface: row configuration</title>
</head>
<body>

<table>
	<thead>
		<th>Row</th>
		<th>User</th>
		<th>Action</th>
	</thead>

	<mak:list from="parade.Row r" where="r.user <> nil" orderBy="r.rowname">
		<tr>
			<td><mak:value expr="r.rowname" /></td>
			<td><mak:value expr="r.user.name" /></td>
			<td><a href="rowEdit.jsp?row=<mak:value expr="r"/>">Edit</a></td>
		</tr>
	</mak:list>
	<mak:list from="parade.Row r" where="r.user = nil" orderBy="r.rowname">
		<tr>
			<td><mak:value expr="r.rowname" /></td>
			<td></td>
			<td><a href="rowEdit.jsp?row=<mak:value expr="r"/>">Edit</a></td>
		</tr>
	</mak:list>
</table>
</body>
</html>