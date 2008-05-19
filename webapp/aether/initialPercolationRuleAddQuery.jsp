<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>


<h2>Queries for InitialPercolationRule ${param.initialPercolationRule}</h2>
<ul>
<mak:list from="InitialPercolationRule ipr, IN(ipr.relationQueries) rq" where="ipr.id = :initialPercolationRule">
<li><mak:value expr="rq.query"/></li>
</mak:list>
</ul>
<br>

<h2>Select queries for InitialPercolationRule ${param.initialPercolationRule}</h2>
<mak:object from="InitialPercolationRule ipr" where="ipr.id = :initialPercolationRule">
<mak:editForm object="ipr" action="initialPercolationRuleList.jsp">
<mak:input field="relationQueries"/>
<input type="submit" value="Save"/>
</mak:editForm>
</mak:object>
<br>

<h2>Create new query</h2>
<mak:newForm type="RelationQuery" action="initialPercolationRuleAddQuery.jsp">
<input type="hidden" value="${param.initialPercolationRule}" name="initialPercolationRule"/>
Query: <mak:input field="query"/>
<input type="submit" value="Save"/>
</mak:newForm>
<br>

<h2>Existing queries</h2>
<mak:list from="RelationQuery rq">
<mak:value expr="rq.query"/> <mak:deleteLink object="rq" action="initialPercolationRuleList.jsp">[Trash]</mak:deleteLink>
</mak:list>