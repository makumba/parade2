<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<html>
<body>
<table>
<thead><th>Date</th><th>User</th><th>Row</th><th>Context</th><th>Action</th><th>Object type</th><th>File</th><th>URL</th><th>Query String</th></thead>
	<mak:list from="ActionLog l" orderBy="l.logDate desc" limit="500"> 
		<tr>
			<td><mak:value expr="l.logDate" format="dd-MM HH:mm"/></td>
			<td><mak:value expr="l.user" /></td>
			<td><mak:value expr="l.paradecontext" /></td>
			<td><mak:value expr="l.context" /></td>
			<td><mak:value expr="l.action" /></td>
			<td><mak:value expr="l.objectType" /></td>
			<td><mak:value expr="l.file" /></td>
			<td><mak:value expr="l.url" /></td>
			<td><mak:value expr="l.queryString" /></td>
		</tr>
	</mak:list>
</table>
</body>
</html>