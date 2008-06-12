<%@page import="org.makumba.aether.percolation.GroupedPercolationStrategy"%>
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** EDIT ***  PAGE FOR OBJECT InitialPercolationRule --%>
<mak:object from="RelationQuery rq" where="rq.id=:relationQuery">
<mak:editForm object="rq" action="relationQuery.jsp">
Description: <mak:input field="description" cols="100"/><br><br>
Query: <mak:input field="query" cols="100" rows="5"/><br>
Arguments: <mak:input field="arguments"/> (if none, fromURL is default)<br>
Supported arguments: <%for(String a : GroupedPercolationStrategy.supportedArguments) {%><%=a%>, <%}%><br>
<br>
<input type="submit" value="Save"/>
</mak:editForm>
</mak:object>
