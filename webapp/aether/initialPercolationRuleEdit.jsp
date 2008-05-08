<%@ taglib uri="http://www.makumba.org/presentation" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** EDIT ***  PAGE FOR OBJECT aether.InitialPercolationRule --%>
<mak:object from="aether.InitialPercolationRule initialPercolationRule" where="initialPercolationRule=$initialPercolationRule">
  <fieldset style="text-align:right;">
  <legend>Edit InitialPercolationRule <i><mak:value expr="initialPercolationRule.objectType" /></i></legend
  <mak:editForm object="initialPercolationRule" action="initialPercolationRuleView.jsp" method="post">
    <table>
    <%-- Makumba Generator - START OF NORMAL FIELDS --%>
      <tr>
        <th><label for="objectType"><span class="accessKey">o</span>bjectType</label></th>
        <td><mak:input field="objectType" styleId="objectType" accessKey="o" /></td>
      </tr>
      <tr>
        <th><label for="action">Ac<span class="accessKey">t</span>ion</label></th>
        <td><mak:input field="action" styleId="action" accessKey="t" /></td>
      </tr>
      <tr>
        <th><label for="userType"><span class="accessKey">u</span>serType</label></th>
        <td><mak:input field="userType" styleId="userType" accessKey="u" /></td>
      </tr>
      <tr>
        <th><label for="initialLevel"><span class="accessKey">i</span>nitialLevel</label></th>
        <td><mak:input field="initialLevel" styleId="initialLevel" accessKey="i" /></td>
      </tr>
      <tr>
        <th><label for="userGroup">Us<span class="accessKey">e</span>rGroup</label></th>
        <td><mak:input field="userGroup" styleId="userGroup" accessKey="e" /></td>
      </tr>
      <tr>
        <td>    <input type="submit" value="Save changes" accessKey="S">    <input type="reset" accessKey="R">    <input type="reset" value="Cancel" accessKey="C" onClick="javascript:back();">    </td>
      </tr>
    </table>
</fieldset>
  </mak:editForm>
  <%-- Makumba Generator - END OF NORMAL FIELDS --%>

  <%-- Makumba Generator - START OF SETS --%>

  <%-- Makumba Generator - END OF SETS --%>

</table>
</fieldset>
</mak:object>

<%-- Makumba Generator - END OF *** EDIT ***  PAGE FOR OBJECT aether.InitialPercolationRule --%>
