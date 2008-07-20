<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** NEW ***  PAGE FOR OBJECT InitialPercolationRule --%>
<fieldset style="text-align:left;">
  <legend>New InitialPercolationRule</legend>
  <jsp:include page="aetherTypes.jsp"/>
  <mak:newForm type="InitialPercolationRule" action="initialPercolationRuleView.jsp" name="initialPercolationRule" >
  <table>
  <%-- Makumba Generator - START OF NORMAL FIELDS --%>
    <tr>
      <th><label for="description">description</label></th>
      <td><mak:input field="description"/></td>
    </tr>
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
      <th><label for="percolation"><span class="accessKey">M</span>ode</label></th>
      <td><mak:input field="percolationMode" styleId="percolationMode" accessKey="M" /></td>
    </tr>
    <tr>
      <th><label for="interactionType"><span class="accessKey">i</span>nteractionType</th>
      <td><mak:input field="interactionType" styleId="interactionType" accessKey="i" /></td>
    </tr>
    <tr>
      <th><label for="focusProgressionCurve">focusProgressionCurve</label></th>
      <td><mak:input field="focusProgressionCurve" /></td>
    </tr>
    <tr>
      <th><label for="nimbusProgressionCurve">nimbusProgressionCurve</label></th>
      <td><mak:input field="nimbusProgressionCurve"/></td>
    </tr>
    
  <%-- Makumba Generator - END OF NORMAL FIELDS --%>
    <tr>
      <td>  <input type="submit" value="Add" accessKey="A">  <input type="reset" accessKey="R">  <input type="reset" value="Cancel" accessKey="C" onClick="javascript:back();">  </td>
    </tr>
  </table>
</fieldset>
</mak:newForm>

<%-- Makumba Generator - END OF *** NEW ***  PAGE FOR OBJECT InitialPercolationRule --%>