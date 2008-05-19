<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** DELETE ***  PAGE FOR OBJECT InitialPercolationRule --%>
<fieldset style="text-align:right;">
  <legend>Delete confirmation</legend
<mak:object from="InitialPercolationRule initialPercolationRule" where="initialPercolationRule.id=:initialPercolationRule">
  Delete initialPercolationRule '<mak:value expr="initialPercolationRule.objectType" />'?
  <a href="javascript:back();">No</a> &nbsp;
  <mak:delete object="initialPercolationRule" action="initialPercolationRuleList.jsp">
    Delete
  </mak:delete>
</mak:object>

<%-- Makumba Generator - END OF *** DELETE ***  PAGE FOR OBJECT InitialPercolationRule --%>
