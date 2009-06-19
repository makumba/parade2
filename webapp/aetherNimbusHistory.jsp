<%-- Aether nimbus history: shows how a specific action affects the work of a user (i.e. displays percolation path) --%>
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<mak:object from="MatchedAetherEvent m" where="m.id = :mae">
	<html>
	<head>
	<title>Consequences of the action <mak:value expr="m.actor" />
	--(<mak:value expr="m.action" />)--> <mak:value expr="m.objectURL" /></title>
	<link rel="stylesheet" type="text/css"
		href="${pageContext.request.contextPath}/layout/style/bottom.css" />
	<link rel="StyleSheet" type="text/css"
		href="${pageContext.request.contextPath}/layout/style/icons_silk.css"
		media="all" />
	</head>
	<body>

	

	<%
		Set<String> ALEs = new HashSet<String>();
	%>
	<mak:list from="ALE a" where="a.user = :user_login">
		<mak:value expr="a.objectURL" printVar="URL" />
		<%
			ALEs.add(URL);
		%>
	</mak:list>


	<h4></h4>
<table width="100%" border="1" cellspacing="0" cellpadding="0" class="table_border">
		<tr class="table_header">
			<td width="100%" colspan="5" valign="top" >
			<p class="text_header"><input onClick="history.back()" type="button" value="Back" />&nbsp;Relation of the action <i><mak:value expr="m.actor" /> --(<mak:value	expr="m.action" />)--> <mak:value expr="m.objectURL" /></i> to my work</p>
			</td>
		</tr>
		<tr class="table_header">
			<td width="30%" valign="top" >
			<p class="text_header">Object</p>
			</td>
			
			<td width="20%" valign="top" >
			<p class="text_header">Description</p>
			</td>
			
			<td width="30%" valign="top" >
			<p class="text_header">Previous object</p>
			</td>
			
			<td width="5%" valign="top" >
			<p class="text_header">Focus</p>
			</td>
		
			<td width="5%" valign="top" >
			<p class="text_header">Nimbus</p>
			</td>
			
		</tr>

		<mak:list from="PercolationStep ps"	where="ps.focus = 0 and ps.matchedAetherEvent.id = m.id and ps.previous = null"	countVar="psCount">
<c:set var="count" value="${mak:count()}" />

<c:choose>
    <c:when test="${count % 2 == 0}">
      <c:set var="table_row" value="table_row_1" />
    </c:when>
    <c:otherwise>
      <c:set var="table_row" value="table_row_2" />
    </c:otherwise>
</c:choose>  
  
<tr class="${table_row}">
				<td><mak:value expr="ps.objectURL" printVar="objectURL" />
				<%
					if (ALEs.contains(objectURL)) {
				%><strong>
				<%
					}
				%>${objectURL}<%
					if (ALEs.contains(objectURL)) {
				%>
				</strong>
				<%
					}
				%>
				</td>
				<td><mak:value expr="ps.percolationRule.description" /></td>
				<td><mak:value expr="ps.previousURL" /></td>
				<td><mak:value expr="ps.focus" /></td>
				<td><mak:value expr="ps.nimbus" /></td>
			</tr>

			<mak:list from="PercolationStep subPs"
				where="subPs.root.id = ps.id and subPs.root.id != subPs.id"
				orderBy="subPs.percolationPath, subPs.created">

				<mak:value expr="subPs.percolationLevel" var="level" />
<c:set var="count" value="${mak:count()}" />

<c:choose>
    <c:when test="${count % 2 == 0}">
      <c:set var="table_row" value="table_row_1" />
    </c:when>
    <c:otherwise>
      <c:set var="table_row" value="table_row_2" />
    </c:otherwise>
</c:choose>  
  
<tr class="${table_row}">
					<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:if
						test="${level > 1}">
						<c:forEach begin="1" end="${level-1}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach>
					</c:if><mak:value expr="subPs.objectURL" printVar="subPsURL" />
					<%
						if (ALEs.contains(subPsURL)) {
					%><strong>
					<%
						}
					%>${subPsURL}<%
						if (ALEs.contains(subPsURL)) {
					%>
					</strong>
					<%
						}
					%>
					</td>
					<td><mak:value expr="subPs.percolationRule.description" /></td>
					<td><mak:value expr="subPs.previousURL" /></td>
					<td><mak:value expr="subPs.focus" /></td>
					<td><mak:value expr="subPs.nimbus" /></td>
			</mak:list>
		</mak:list>
  <tr class="table_row_1">
  <td width="100%" colspan="5" valign="top">
  <input onClick="history.back()" type="button" value="Back" />
  </td>
  </tr>		
		
	</table>
</mak:object>

</body>
</html>
