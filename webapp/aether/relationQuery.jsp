<%@page import="org.makumba.aether.percolation.GroupedPercolationStrategy"%>
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>


<%@page import="org.makumba.providers.QueryProvider"%><h2>Create new query</h2>
<mak:newForm type="RelationQuery" action="relationQuery.jsp">
Description: <mak:input field="description" cols="100"/><br><br>
Query: <mak:input field="query" cols="100" rows="5"/><br>
Arguments (comma-separated): <mak:input field="arguments"/> (if none, fromURL is default)<br>
Supported arguments: Supported arguments: <%for(String a : GroupedPercolationStrategy.supportedArguments) {%><%=a%>, <%}%><br>
<br>
<input type="submit" value="Save"/>
</mak:newForm>
<br>

<h2>Existing queries</h2>
<mak:list from="RelationQuery rq">
<h3><mak:value expr="rq.description"/></h3>
<strong>Query</strong><br>
<mak:value expr="rq.query"/> <br>
<strong>Query with inlined functions</strong><br>
<mak:value expr="rq.query" printVar="queryString"/>
<%= QueryProvider.getQueryAnalzyer("hql").inlineFunctions(queryString) %><br>
<strong>Arguments</strong><br>
<mak:value expr="rq.arguments"/><br><br>
<a href="relationQueryEdit.jsp?relationQuery=<mak:value expr="rq.id"/>">[Edit]</a>&nbsp;<mak:deleteLink object="rq" action="relationQuery.jsp">[Trash]</mak:deleteLink><br>
<hr>
</mak:list>