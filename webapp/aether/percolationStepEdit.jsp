<%@ taglib uri="http://www.makumba.org/presentation" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** EDIT ***  PAGE FOR OBJECT aether.PercolationStep --%>
<mak:object from="aether.PercolationStep percolationStep" where="percolationStep=$percolationStep">
  <fieldset style="text-align:right;">
  <legend>Edit PercolationStep <i><mak:value expr="percolationStep.object" /></i></legend
  <mak:editForm object="percolationStep" action="percolationStepView.jsp" method="post">
    <table>
    <%-- Makumba Generator - START OF NORMAL FIELDS --%>
      <tr>
        <th><label for="object">O<span class="accessKey">b</span>ject</label></th>
        <td><mak:input field="object" styleId="object" accessKey="b" /></td>
      </tr>
      <tr>
        <th><label for="userGroup">User<span class="accessKey">G</span>roup</label></th>
        <td><mak:input field="userGroup" styleId="userGroup" accessKey="g" /></td>
      </tr>
      <tr>
        <th><label for="focus">Focus</label></th>
        <td><mak:input field="focus" styleId="focus" accessKey=" " /></td>
      </tr>
      <tr>
        <th><label for="nimbus">N<span class="accessKey">i</span>mbus</label></th>
        <td><mak:input field="nimbus" styleId="nimbus" accessKey="i" /></td>
      </tr>
      <tr>
        <th><label for="percolationId">Perco<span class="accessKey">l</span>ationId</label></th>
        <td><mak:input field="percolationId" styleId="percolationId" accessKey="l" /></td>
      </tr>
      <tr>
        <th><label for="previous">Pre<span class="accessKey">v</span>ious</label></th>
        <td><mak:input field="previous" styleId="previous" accessKey="v" /></td>
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

<%-- Makumba Generator - END OF *** EDIT ***  PAGE FOR OBJECT aether.PercolationStep --%>
