<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<h1>Percolation log</h1>

<mak:list from="MatchedAetherEvent mae" orderBy="mae.eventDate asc">

<h3><mak:value expr="mae.eventDate" format="yyyy-MM-dd HH:mm"/> Percolation steps for: <mak:value expr="mae.actor"/> --(<mak:value expr="mae.action"/>)--> <mak:value expr="mae.objectURL"/></h3>
(<mak:value expr="mae.initialPercolationRule.percolationMode"/> percolation applying for group "<mak:value expr="mae.userGroup"/>")
<table>
<thead>
<th></th>
<th>Object</th>
<th>Focus</th>
<th>Nimbus</th>
<th>Description</th>
</thead>
<mak:list from="PercolationStep ps" where="ps.matchedAetherEvent.id = mae.id" countVar="psCount">
<mak:list from="PercolationStep ps1" where="ps1.previous.id = ps.id">
<tr>
<td>${psCount}</td>
<td><mak:value expr="ps1.objectURL"/></td>
<td><mak:value expr="ps1.focus"/></td>
<td><mak:value expr="ps1.nimbus"/></td>
<td><mak:value expr="ps1.percolationRule.description"/></td>
</tr>
</mak:list>
</mak:list>
</table>

</mak:list>