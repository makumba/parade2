<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<mak:list from="ALE a" where="a.user = :user_login and a.nimbus > 20" orderBy="a.nimbus desc">
<mak:value expr="a.objectURL" printVar="objectURL"/>
<% String fileName = objectURL.substring(objectURL.lastIndexOf("/")+1); %>
<a title="<mak:value expr="a.objectURL" />"><%=fileName %></a> (<mak:value expr="a.nimbus" />)<br>
Why is this file listed here?<br>

<mak:list from="PercolationStep step join step.matchedAetherEvent mae" where="step.nimbus > 20 and step.objectURL = a.objectURL and mae.actor != :user_login" groupBy="mae.id" orderBy="sum(step.nimbus), mae.eventDate">
<mak:value expr="mae.eventDate" format="dd-MM-yyyy HH:mm" /> <mak:value expr="mae.actor" /> <mak:value expr="mae.action"/> <mak:value expr="mae.objectURL"/> (<mak:value expr="step.nimbus"/>)<br>
The ALE he contributed was:
<mak:list from="PercolationStep ps" where="ps.matchedAetherEvent = mae"><mak:value expr="ps.nimbus"/> </mak:list><br>
</mak:list>
<br>
</mak:list>