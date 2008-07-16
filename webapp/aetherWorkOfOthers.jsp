<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<mak:list from="ALE a" where="a.user = :user_login and a.nimbus > 20" orderBy="a.nimbus desc">
<mak:value expr="a.objectURL" printVar="objectURL"/>
<strong><a title="<mak:value expr="a.objectURL" />"><mak:value expr="a.objectURL"/></a></strong> (<mak:value expr="a.nimbus" />)<br>
Why is this file listed here?<br>

<mak:list from="PercolationStep ps1 join ps1.matchedAetherEvent mae" where="ps1.nimbus > 20 and ps1.objectURL = a.objectURL and mae.actor != :user_login and mae.virtualPercolation = false">
<mak:list from="PercolationStep ps2 join ps2.matchedAetherEvent mae2" where="ps2.matchedAetherEvent = mae and ps2.objectURL = a.objectURL" groupBy="ps2">
<i><mak:value expr="ps2.created" format="dd-MM-yyyy HH:mm:ss"/> <mak:value expr="ps2.objectURL"/> <mak:value expr="sum(ps2.nimbus)"/></i><br>
</mak:list>
<mak:value expr="ps1.objectURL" /><mak:value expr="ps1.created" format="dd-MM-yyyy HH:mm"/><br>
</mak:list>
<br><br>

</mak:list>