<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** DELETE ***  PAGE FOR OBJECT PercolationRule --%>
<fieldset style="text-align:right;">
  <legend>Delete confirmation</legend
<mak:object from="PercolationRule percolationRule" where="percolationRule.id=:percolationRule">
  Delete percolationRule '<mak:value expr="percolationRule.subject" />'?
  <a href="javascript:back();">No</a> &nbsp;
  <mak:delete object="percolationRule" action="percolationRuleList.jsp">
    Delete
  </mak:delete>
</mak:object>

<%-- Makumba Generator - END OF *** DELETE ***  PAGE FOR OBJECT PercolationRule --%>
