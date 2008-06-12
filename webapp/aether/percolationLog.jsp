<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<html>
<head>
<title>Percolation log</title>
</head>
<body>
<h1>Percolation log</h1>

<mak:list from="MatchedAetherEvent mae" orderBy="mae.eventDate desc"
	limit="3">

	<h3><mak:value expr="mae.eventDate" format="yyyy-MM-dd HH:mm" />
	Percolation steps for: <mak:value expr="mae.actor" /> --(<mak:value
		expr="mae.action" />)--> <mak:value expr="mae.objectURL" /></h3>
(<mak:value expr="mae.initialPercolationRule.percolationMode" /> percolation applying for group "<mak:value
		expr="mae.userGroup" />")
<table>
		<thead>
			<th>Object</th>
			<th>Description</th>
			<th>Previous object</th>
			<th>Focus</th>
			<th>Nimbus</th>
		</thead>
		<mak:list from="PercolationStep ps"
			where="ps.matchedAetherEvent.id = mae.id and ps.previous = null"
			countVar="psCount">
			<tr>
				<td><mak:value expr="ps.objectURL" /></td>
				<td><a
					href="percolationRuleEdit.jsp?percolationRule=<mak:value expr="ps.percolationRule.id"/>"><mak:value
					expr="ps.percolationRule.description" /></a></td>
				<td><mak:value expr="ps.previousURL" /></td>
				<td><mak:value expr="ps.focus" /></td>
				<td><mak:value expr="ps.nimbus" /></td>
			</tr>

			<mak:list from="PercolationStep subPs"
				where="subPs.root.id = ps.id and subPs.root.id != subPs.id"
				orderBy="subPs.percolationPath, subPs.created">

				<mak:value expr="subPs.percolationLevel" var="level" />
				<tr>
					<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:if
						test="${level > 1}"><c:forEach begin="1" end="${level-1}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach></c:if><mak:value expr="subPs.objectURL" /></td>
					<td><a
						href="percolationRuleEdit.jsp?percolationRule=<mak:value expr="subPs.percolationRule.id"/>"><mak:value
						expr="subPs.percolationRule.description" /></a></td>
					<td><mak:value expr="subPs.previousURL" /></td>
					<td><mak:value expr="subPs.focus" /></td>
					<td><mak:value expr="subPs.nimbus" /></td>
			</mak:list>
		</mak:list>
	</table>

</mak:list>

</body>
</html>