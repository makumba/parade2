<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** DELETE ***  PAGE FOR OBJECT PercolationStep --%>
<fieldset style="text-align:right;">
  <legend>Delete confirmation</legend
<mak:object from="PercolationStep percolationStep" where="percolationStep.id=:percolationStep">
  Delete percolationStep '<mak:value expr="percolationStep.object" />'?
  <a href="javascript:back();">No</a> &nbsp;
  <mak:delete object="percolationStep" action="percolationStepList.jsp">
    Delete
  </mak:delete>
</mak:object>

<%-- Makumba Generator - END OF *** DELETE ***  PAGE FOR OBJECT PercolationStep --%>
