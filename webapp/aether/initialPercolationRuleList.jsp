<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<%-- Makumba Generator - START OF  *** LIST ***  PAGE FOR OBJECT InitialPercolationRule --%>
<fieldset style="text-align: right;"><legend>List
InitialPercolationRules</legend>

<c:choose>
  <c:when test="${!empty param.sortBy}">
    <c:set var="sortBy" value="initialPercolationRule.${param.sortBy}" />
  </c:when>
  <c:otherwise>
    <c:set var="sortBy" value="initialPercolationRule.active" />
  </c:otherwise>
</c:choose>

<table>
  <tr>
    <th><a href="initialPercolationRuleList.jsp?sortBy=objectType">#</a></th>
    <th><a href="initialPercolationRuleList.jsp?sortBy=objectType">objectType</a></th>
    <th><a href="initialPercolationRuleList.jsp?sortBy=action">action</a></th>
    <th><a href="initialPercolationRuleList.jsp?sortBy=initialLevel">initialLevel</a></th>
    <th><a href="initialPercolationRuleList.jsp?sortBy=userType">userType</a></th>
    <th><a href="initialPercolationRuleList.jsp?sortBy=actuve">active</a></th>
    <th>Actions</th>
  </tr>
  <mak:list from="InitialPercolationRule initialPercolationRule" orderBy="#{sortBy}">
    <tr>
      <td>${mak:count()}</td>
      <td><mak:value expr="initialPercolationRule.objectType" /></td>
      <td><mak:value expr="initialPercolationRule.action" /></td>
      <td><mak:value expr="initialPercolationRule.initialLevel" /></td>
      <td><mak:value expr="initialPercolationRule.userType" /></td>
      <td><mak:value expr="initialPercolationRule.active" /></td>
      <td>
<a href="initialPercolationRuleView.jsp?initialPercolationRule=<mak:value expr="initialPercolationRule.id" />">[View]</a> <a href="initialPercolationRuleEdit.jsp?initialPercolationRule=<mak:value expr="initialPercolationRule.id" />">[Edit]</a> <a href="initialPercolationRuleDelete.jsp?initialPercolationRule=<mak:value expr="initialPercolationRule.id" />">[Delete]</a> </td>    </tr>
  </mak:list>
</table>
</fieldset>
<a href="initialPercolationRuleNew.jsp">[New]</a>

<br><br>
<a href="index.jsp">Back to index</a>

<%-- Makumba Generator - END OF *** LIST ***  PAGE FOR OBJECT InitialPercolationRule --%>
