<%-- Aether nimbus history: shows how a specific action affects the work of a user (i.e. displays percolation path) --%>
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<mak:object from="MatchedAetherEvent m" where="m.id = :mae">

<head>
<title>Consequences of the action <mak:value expr="m.actor"/> --(<mak:value expr="m.action"/>)--> <mak:value expr="m.objectURL"/></title>

<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/icons_silk.css" type="text/css" media="all" />

</head>
<body>

<% Set<String> ALEs = new HashSet<String>();%>
<mak:list from="ALE a" where="a.user = :user_login"><mak:value expr="a.objectURL" printVar="URL"/><% ALEs.add(URL); %></mak:list>


<h4>Relation of the action <i><mak:value expr="m.actor"/> --(<mak:value expr="m.action"/>)--> <mak:value expr="m.objectURL"/></i> to my work</h4>

<table>
    <thead>
      <th>Object</th>
      <th>Description</th>
      <th>Previous object</th>
      <th>Focus</th>
      <th>Nimbus</th>
    </thead>
    <mak:list from="PercolationStep ps" where="ps.focus = 0 and ps.matchedAetherEvent.id = m.id and ps.previous = null" countVar="psCount">
      <tr>
        <td><mak:value expr="ps.objectURL" printVar="objectURL" /><%if(ALEs.contains(objectURL)) { %><strong><%} %>${objectURL}<%if(ALEs.contains(objectURL)) { %></strong><%} %></td>
        <td><mak:value
          expr="ps.percolationRule.description" /></td>
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
            test="${level > 1}"><c:forEach begin="1" end="${level-1}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach></c:if><mak:value expr="subPs.objectURL" printVar="subPsURL" /><%if(ALEs.contains(subPsURL)) { %><strong><%} %>${subPsURL}<%if(ALEs.contains(subPsURL)) { %></strong><%} %></td>
          <td><mak:value expr="subPs.percolationRule.description" /></td>
          <td><mak:value expr="subPs.previousURL" /></td>
          <td><mak:value expr="subPs.focus" /></td>
          <td><mak:value expr="subPs.nimbus" /></td>
      </mak:list>
    </mak:list>
  </table>

</mak:object>

</body>

<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%></html>
