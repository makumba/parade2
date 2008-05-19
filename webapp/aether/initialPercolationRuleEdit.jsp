<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** EDIT ***  PAGE FOR OBJECT InitialPercolationRule --%>
<mak:object from="InitialPercolationRule initialPercolationRule" where="initialPercolationRule.id=:initialPercolationRule">
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
      <input type="hidden" name="initialPercolationRule" value="<mak:value expr="initialPercolationRule.id"/>"/>
      <tr>
        <td>    <input type="submit" value="Save changes" accessKey="S">    <input type="reset" accessKey="R">    <input type="reset" value="Cancel" accessKey="C" onClick="javascript:back();">    </td>
      </tr>
    </table>
</fieldset>
  </mak:editForm>
  <%-- Makumba Generator - END OF NORMAL FIELDS --%>

  <%-- Makumba Generator - START OF SETS --%>

  <%-- Makumba Generator - END OF SETS --%>

<tr><td>relationQueries</td>
    <td><mak:list from="join initialPercolationRule.relationQueries rq">
      <mak:value expr="rq.query"/>&nbsp;<a href="initialPercolationRuleQueryEdit.jsp?relationQuery=<mak:value expr="rq.id"/>">[Edit]</a><br>
      </mak:list>
    </td>
</tr>

</table>
</fieldset>
</mak:object>

<%-- Makumba Generator - END OF *** EDIT ***  PAGE FOR OBJECT InitialPercolationRule --%>