<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** OBJECT ***  PAGE FOR OBJECT InitialPercolationRule --%>
<mak:response/>
<mak:object from="InitialPercolationRule initialPercolationRule" where="initialPercolationRule.id=:initialPercolationRule">
  <fieldset style="text-align:right;">
  <legend>InitialPercolationRule <i><mak:value expr="initialPercolationRule.objectType" /></i></legend
  <table>
  <%-- Makumba Generator - START OF NORMAL FIELDS --%>
    <tr>
      <th>objectType</th>
      <td><mak:value expr="initialPercolationRule.objectType"/></td>
    </tr>
    <tr>
      <th>action</th>
      <td><mak:value expr="initialPercolationRule.action"/></td>
    </tr>
    <tr>
      <th>userType</th>
      <td><mak:value expr="initialPercolationRule.userType"/></td>
    </tr>
    <tr>
      <th>initialLevel</th>
      <td><mak:value expr="initialPercolationRule.initialLevel"/></td>
    </tr>
    <tr>
      <th>relationQueries</th>
      <td><mak:list from="IN(initialPercolationRule.relationQueries) rq"><mak:value expr="rq.query"/></mak:list></td>
    </tr>
  </table>
</fieldset>
  <%-- Makumba Generator - END OF NORMAL FIELDS --%>

  <%-- Makumba Generator - START OF SETS --%>

  <%-- Makumba Generator - END OF SETS --%>

</table>
<a href="initialPercolationRuleList.jsp">Back to initialPercolationRule list</a>
</fieldset>
</mak:object>

<%-- Makumba Generator - END OF *** OBJECT ***  PAGE FOR OBJECT InitialPercolationRule --%>
