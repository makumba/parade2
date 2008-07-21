<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<mak:object from="MatchedAetherEvent m" where="m.id = :mae">

<head>
<title>Consequences of the action <mak:value expr="m.actor"/> --(<mak:value expr="m.action"/>)--> <mak:value expr="m.objectURL"/></title>

<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/icons_silk.css" type="text/css" media="all" />

</head>
<body>

<h4>Relation of the action <i><mak:value expr="m.actor"/> --(<mak:value expr="m.action"/>)--> <mak:value expr="m.objectURL"/></i> to my work</h4>

<table>
    <thead>
      <th>Object</th>
      <th>Description</th>
      <th>Previous object</th>
      <th>Focus</th>
      <th>Nimbus</th>
    </thead>
    <mak:list from="PercolationStep ps" where="ps.matchedAetherEvent.id = m.id and ps.previous = null" countVar="psCount">
      <tr>
        <td><mak:value expr="ps.objectURL" /></td>
        <td><a
          href="percolationRuleEdit.jsp?percolationRule=<mak:value expr="ps.percolationRule.id"/>"><mak:value
          expr="ps.percolationRule.description" /></a></td>
        <td><mak:value expr="ps.previousURL" /></td>
        <td><mak:value expr="ps.focus" /></td>
        <td><mak:value expr="ps.nimbus" /></td>
      </tr>

      <mak:list from="PercolationStep subPs"
        where="subPs.root.id = ps.id and subPs.root.id != subPs.id"
        orderBy="subPs.percolationPath, subPs.created">

        <mak:value expr="subPs.percolationLevel" var="level" />
        <tr>
          <td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:if
            test="${level > 1}"><c:forEach begin="1" end="${level-1}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach></c:if><mak:value expr="subPs.objectURL" /></td>
          <td><a
            href="percolationRuleEdit.jsp?percolationRule=<mak:value expr="subPs.percolationRule.id"/>"><mak:value
            expr="subPs.percolationRule.description" /></a></td>
          <td><mak:value expr="subPs.previousURL" /></td>
          <td><mak:value expr="subPs.focus" /></td>
          <td><mak:value expr="subPs.nimbus" /></td>
      </mak:list>
    </mak:list>
  </table>

</mak:object>

</body>
</html>
