<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** OBJECT ***  PAGE FOR OBJECT PercolationStep --%>
<mak:object from="PercolationStep percolationStep" where="percolationStep.id=:percolationStep">
  <fieldset style="text-align:right;">
  <legend>PercolationStep <i><mak:value expr="percolationStep.object" /></i></legend
  <table>
  <%-- Makumba Generator - START OF NORMAL FIELDS --%>
    <tr>
      <th>object</th>
      <td><mak:value expr="percolationStep.object"/></td>
    </tr>
    <tr>
      <th>userGroup</th>
      <td><mak:value expr="percolationStep.userGroup"/></td>
    </tr>
    <tr>
      <th>focus</th>
      <td><mak:value expr="percolationStep.focus"/></td>
    </tr>
    <tr>
      <th>nimbus</th>
      <td><mak:value expr="percolationStep.nimbus"/></td>
    </tr>
    <tr>
      <th>percolationId</th>
      <td><mak:value expr="percolationStep.percolationId"/></td>
    </tr>
    <tr>
      <th>previous</th>
      <td><mak:value expr="percolationStep.previous"/></td>
    </tr>
  </table>
</fieldset>
  <%-- Makumba Generator - END OF NORMAL FIELDS --%>

  <%-- Makumba Generator - START OF SETS --%>

  <%-- Makumba Generator - END OF SETS --%>

</table>
</fieldset>
<a href="percolationStepList.jsp">Back to percolationStep list</a>

</mak:object>

<%-- Makumba Generator - END OF *** OBJECT ***  PAGE FOR OBJECT PercolationStep --%>
