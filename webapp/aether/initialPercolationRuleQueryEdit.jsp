<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** EDIT ***  PAGE FOR OBJECT InitialPercolationRule --%>
<mak:object from="RelationQuery rq" where="rq.id=:relationQuery">
<mak:editForm object="rq" action="initialPercolationRuleList.jsp">
<mak:input field="query"/>
<input type="submit" value="Save"/>
</mak:editForm>
</mak:object>
