<%@ taglib uri="http://www.makumba.org/presentation" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** DELETE ***  PAGE FOR OBJECT aether.InitialPercolationRule --%>
<fieldset style="text-align:right;">
  <legend>Delete confirmation</legend
<mak:object from="aether.InitialPercolationRule initialPercolationRule" where="initialPercolationRule=$initialPercolationRule">
  Delete initialPercolationRule '<mak:value expr="initialPercolationRule.objectType" />'?
  <a href="javascript:back();">No</a> &nbsp;
  <mak:delete object="initialPercolationRule" action="initialPercolationRuleList.jsp">
    Delete
  </mak:delete>
</mak:object>

<%-- Makumba Generator - END OF *** DELETE ***  PAGE FOR OBJECT aether.InitialPercolationRule --%>
