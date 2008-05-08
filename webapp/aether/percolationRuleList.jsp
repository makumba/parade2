<%@ taglib uri="http://www.makumba.org/presentation" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** LIST ***  PAGE FOR OBJECT aether.PercolationRule --%>
<fieldset style="text-align:right;">
  <legend>List PercolationRules</legend

<c:choose>
  <c:when test="${param.sortBy == 'created'}">
    <c:set var="sortBy" value="percolationRule.TS_create" />
  </c:when>
  <c:when test="${param.sortBy == 'modified'}">
    <c:set var="sortBy" value="percolationRule.TS_modify" />
  </c:when>
  <c:when test="${!empty param.sortBy}">
    <c:set var="sortBy" value="percolationRule.${param.sortBy}" />
  </c:when>
  <c:otherwise>
    <c:set var="sortBy" value="percolationRule.subject" />
  </c:otherwise>
</c:choose>

<table>
  <tr>
    <th><a href="percolationRuleList.jsp?sortBy=created">#</a></th>
    <th><a href="percolationRuleList.jsp?sortBy=subject">subject</a></th>
    <th><a href="percolationRuleList.jsp?sortBy=predicate">predicate</a></th>
    <th><a href="percolationRuleList.jsp?sortBy=object">object</a></th>
    <th><a href="percolationRuleList.jsp?sortBy=created">Created</a></th>
    <th><a href="percolationRuleList.jsp?sortBy=modified">Modified</a></th>
    <th>Actions</th>
  </tr>
  <mak:list from="aether.PercolationRule percolationRule" orderBy="#{sortBy}">
    <tr>
      <td>${mak:count()}</td>
      <td><mak:value expr="percolationRule.subject" /></td>
      <td><mak:value expr="percolationRule.predicate" /></td>
      <td><mak:value expr="percolationRule.object" /></td>
      <td><mak:value expr="percolationRule.TS_create" format="yyyy-MM-dd hh:mm:ss" /></td>
      <td><mak:value expr="percolationRule.TS_modify" format="yyyy-MM-dd hh:mm:ss" /></td>
      <td>
<a href="percolationRuleView.jsp?percolationRule=<mak:value expr="percolationRule" />">[View]</a> <a href="percolationRuleEdit.jsp?percolationRule=<mak:value expr="percolationRule" />">[Edit]</a> <a href="percolationRuleDelete.jsp?percolationRule=<mak:value expr="percolationRule" />">[Delete]</a> </td>    </tr>
  </mak:list>
</table>
</fieldset>
<a href="percolationRuleNew.jsp">[New]</a>

<%-- Makumba Generator - END OF *** LIST ***  PAGE FOR OBJECT aether.PercolationRule --%>
