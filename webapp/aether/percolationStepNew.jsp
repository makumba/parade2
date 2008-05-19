<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** NEW ***  PAGE FOR OBJECT PercolationStep --%>
<fieldset style="text-align:right;">
  <legend>New PercolationStep</legend
<mak:newForm type="PercolationStep" action="percolationStepView.jsp" name="percolationStep" >
  <table>
  <%-- Makumba Generator - START OF NORMAL FIELDS --%>
    <tr>
      <th><label for="object"><span class="accessKey">o</span>bject</label></th>
      <td><mak:input field="object" styleId="object" accessKey="o" /></td>
    </tr>
    <tr>
      <th><label for="userGroup"><span class="accessKey">u</span>serGroup</label></th>
      <td><mak:input field="userGroup" styleId="userGroup" accessKey="u" /></td>
    </tr>
    <tr>
      <th><label for="focus"><span class="accessKey">f</span>ocus</label></th>
      <td><mak:input field="focus" styleId="focus" accessKey="f" /></td>
    </tr>
    <tr>
      <th><label for="nimbus"><span class="accessKey">n</span>imbus</label></th>
      <td><mak:input field="nimbus" styleId="nimbus" accessKey="n" /></td>
    </tr>
    <tr>
      <th><label for="percolationId"><span class="accessKey">p</span>ercolationId</label></th>
      <td><mak:input field="percolationId" styleId="percolationId" accessKey="p" /></td>
    </tr>
    <tr>
      <th><label for="previous">Pr<span class="accessKey">e</span>vious</label></th>
      <td><mak:input field="previous" styleId="previous" accessKey="e" /></td>
    </tr>
  <%-- Makumba Generator - END OF NORMAL FIELDS --%>
    <tr>
      <td>  <input type="submit" value="Add" accessKey="A">  <input type="reset" accessKey="R">  <input type="reset" value="Cancel" accessKey="C" onClick="javascript:back();">  </td>
    </tr>
  </table>
</fieldset>
</mak:newForm>

<%-- Makumba Generator - END OF *** NEW ***  PAGE FOR OBJECT PercolationStep --%>
