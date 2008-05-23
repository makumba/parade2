<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>

<table>
<thead><th>Date</th><th>User</th><th>Action</th><th>File</th><th>URL</th><th>Query String</th></thead>
	<mak:list from="ActionLog l"> 
		<tr>
			<td><mak:value expr="l.logDate" /></td>
			<td><mak:value expr="l.user" /></td>
			<td><mak:value expr="l.action" /></td>
			<td><mak:value expr="l.file" /></td>
			<td><mak:value expr="l.url" /></td>
			<td><mak:value expr="l.queryString" /></td>
		</tr>
	</mak:list>
</table>