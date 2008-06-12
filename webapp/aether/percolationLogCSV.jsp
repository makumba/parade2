<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<html>
<body>
MatchedAetherEvent;Object;Description;Previous object;Focus;Nimbus;<br>
<mak:list from="MatchedAetherEvent mae" orderBy="mae.eventDate desc" limit="3">
		<mak:list from="PercolationStep ps"
			where="ps.matchedAetherEvent.id = mae.id and ps.previous = null"
			countVar="psCount">

<mak:value expr="mae.eventDate" format="yyyy-MM-dd HH:mm" /> <mak:value expr="mae.actor" /> --(<mak:value expr="mae.action" />)--> <mak:value expr="mae.objectURL"/>
(<mak:value expr="mae.initialPercolationRule.percolationMode" /> percolation applying for group "<mak:value expr="mae.userGroup" />");

<mak:value expr="ps.objectURL" />;
<mak:value expr="ps.percolationRule.description" lineSeparator=""/>;
<mak:value expr="ps.previousURL"/>;
<mak:value expr="ps.focus" />;
<mak:value expr="ps.nimbus" />;
<br>
<mak:list from="PercolationStep subPs" where="subPs.root.id = ps.id and subPs.root.id != subPs.id" orderBy="subPs.percolationPath, subPs.created">

<mak:value expr="subPs.percolationLevel" var="level" />
<mak:value expr="mae.eventDate" format="yyyy-MM-dd HH:mm" /> <mak:value expr="mae.actor" /> --(<mak:value expr="mae.action" />)--> <mak:value expr="mae.objectURL"/>
(<mak:value expr="mae.initialPercolationRule.percolationMode" /> percolation applying for group "<mak:value expr="mae.userGroup" />");
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:if
			test="${level > 1}"><c:forEach begin="1" end="${level-1}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach></c:if><mak:value expr="subPs.objectURL" />;
<mak:value expr="subPs.percolationRule.description"  lineSeparator=""/>;
<mak:value expr="subPs.previousURL" />;
<mak:value expr="subPs.focus" />;
<mak:value expr="subPs.nimbus" />;
<br>
</mak:list>
</mak:list>

</mak:list>

</body>
</html>