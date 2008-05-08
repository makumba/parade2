<%@ taglib uri="http://www.makumba.org/presentation" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<%-- Makumba Generator - START OF  *** LIST ***  PAGE FOR OBJECT aether.PercolationStep --%>
<fieldset style="text-align:right;">
  <legend>List PercolationSteps</legend

<c:choose>
  <c:when test="${param.sortBy == 'created'}">
    <c:set var="sortBy" value="percolationStep.TS_create" />
  </c:when>
  <c:when test="${param.sortBy == 'modified'}">
    <c:set var="sortBy" value="percolationStep.TS_modify" />
  </c:when>
  <c:when test="${!empty param.sortBy}">
    <c:set var="sortBy" value="percolationStep.${param.sortBy}" />
  </c:when>
  <c:otherwise>
    <c:set var="sortBy" value="percolationStep.object" />
  </c:otherwise>
</c:choose>

<table>
  <tr>
    <th><a href="percolationStepList.jsp?sortBy=created">#</a></th>
    <th><a href="percolationStepList.jsp?sortBy=object">object</a></th>
    <th><a href="percolationStepList.jsp?sortBy=object">focus</a></th>
    <th><a href="percolationStepList.jsp?sortBy=object">nimbus</a></th>
    <th><a href="percolationStepList.jsp?sortBy=created">Created</a></th>
    <th><a href="percolationStepList.jsp?sortBy=modified">Modified</a></th>
    <th>Actions</th>
  </tr>
  <mak:list from="aether.PercolationStep percolationStep" orderBy="#{sortBy}">
    <tr>
      <td>${mak:count()}</td>
      <td><mak:value expr="percolationStep.object" /></td>
      <td><mak:value expr="percolationStep.focus" /></td>
      <td><mak:value expr="percolationStep.nimbus" /></td>
      <td><mak:value expr="percolationStep.TS_create" format="yyyy-MM-dd hh:mm:ss" /></td>
      <td><mak:value expr="percolationStep.TS_modify" format="yyyy-MM-dd hh:mm:ss" /></td>
      <td>
<a href="percolationStepView.jsp?percolationStep=<mak:value expr="percolationStep" />">[View]</a> <a href="percolationStepEdit.jsp?percolationStep=<mak:value expr="percolationStep" />">[Edit]</a> <a href="percolationStepDelete.jsp?percolationStep=<mak:value expr="percolationStep" />">[Delete]</a> </td>    </tr>
  </mak:list>
</table>
</fieldset>
<a href="percolationStepNew.jsp">[New]</a>

<%-- Makumba Generator - END OF *** LIST ***  PAGE FOR OBJECT aether.PercolationStep --%>