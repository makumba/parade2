<%@ taglib uri="http://www.makumba.org/presentation" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** LIST ***  PAGE FOR OBJECT aether.InitialPercolationRule --%>
<fieldset style="text-align:right;">
  <legend>List InitialPercolationRules</legend

<c:choose>
  <c:when test="${param.sortBy == 'created'}">
    <c:set var="sortBy" value="initialPercolationRule.TS_create" />
  </c:when>
  <c:when test="${param.sortBy == 'modified'}">
    <c:set var="sortBy" value="initialPercolationRule.TS_modify" />
  </c:when>
  <c:when test="${!empty param.sortBy}">
    <c:set var="sortBy" value="initialPercolationRule.${param.sortBy}" />
  </c:when>
  <c:otherwise>
    <c:set var="sortBy" value="initialPercolationRule.objectType" />
  </c:otherwise>
</c:choose>

<table>
  <tr>
    <th><a href="initialPercolationRuleList.jsp?sortBy=created">#</a></th>
    <th><a href="initialPercolationRuleList.jsp?sortBy=objectType">objectType</a></th>
    <th><a href="initialPercolationRuleList.jsp?sortBy=action">action</a></th>
    <th><a href="initialPercolationRuleList.jsp?sortBy=initialLevel">initialLevel</a></th>

    <th><a href="initialPercolationRuleList.jsp?sortBy=created">Created</a></th>
    <th><a href="initialPercolationRuleList.jsp?sortBy=modified">Modified</a></th>
    <th>Actions</th>
  </tr>
  <mak:list from="aether.InitialPercolationRule initialPercolationRule" orderBy="#{sortBy}">
    <tr>
      <td>${mak:count()}</td>
      <td><mak:value expr="initialPercolationRule.objectType" /></td>
      <td><mak:value expr="initialPercolationRule.action" /></td>
      <td><mak:value expr="initialPercolationRule.initialLevel" /></td>
      <td><mak:value expr="initialPercolationRule.TS_create" format="yyyy-MM-dd hh:mm:ss" /></td>
      <td><mak:value expr="initialPercolationRule.TS_modify" format="yyyy-MM-dd hh:mm:ss" /></td>
      <td>
<a href="initialPercolationRuleView.jsp?initialPercolationRule=<mak:value expr="initialPercolationRule" />">[View]</a> <a href="initialPercolationRuleEdit.jsp?initialPercolationRule=<mak:value expr="initialPercolationRule" />">[Edit]</a> <a href="initialPercolationRuleDelete.jsp?initialPercolationRule=<mak:value expr="initialPercolationRule" />">[Delete]</a> </td>    </tr>
  </mak:list>
</table>
</fieldset>
<a href="initialPercolationRuleNew.jsp">[New]</a>

<%-- Makumba Generator - END OF *** LIST ***  PAGE FOR OBJECT aether.InitialPercolationRule --%>
