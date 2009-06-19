<%-- Aether focus history: lists all the actions of a user for an object --%>
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="org.makumba.parade.aether.ObjectTypes"%>
<%@page import="org.makumba.parade.aether.ActionTypes"%>
<%@page import="org.makumba.commons.ReadableFormatter"%>
<%@page import="java.util.Date"%>
<html>
<head>
<title>Your actions for ${param.objectURL}</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/layout/style/bottom.css" />
<link rel="StyleSheet" type="text/css" href="${pageContext.request.contextPath}/layout/style/icons_silk.css" media="all" />
</head>
<body>
<table width="100%" border="1" cellspacing="0" cellpadding="0" class="table_border">
 <tr>
  <td width="100%" valign="top" class="table_header">
     <p class="text_header"><input onClick="history.back()" type="button" value="Back" />&nbsp;Your actions for ${param.objectURL}</p>
  </td>
  </tr>

<mak:list from="MatchedAetherEvent mae" where="mae.actor = :user_login and mae.objectURL = :objectURL" orderBy="mae.eventDate desc" groupBy="mae.id">
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
<td width="100%" valign="top" >  
<mak:value expr="mae.eventDate" var="eventDate"/><%=ReadableFormatter.readableAge(new Date().getTime() - ((Date) eventDate).getTime())%> ago, you <mak:value expr="mae.action" printVar="action"/><%=ActionTypes.getReadableAction(action) %> this <mak:value expr="mae.objectURL" printVar="objectURL"/><%=ObjectTypes.getObjectType(objectURL).readableType() %>, which contributed with a total of <mak:list from="PercolationStep ps" where="ps.matchedAetherEvent.id = mae.id and ps.focus > 0" groupBy="ps.id, mae.id"><mak:value expr="sum(ps.focus)"/></mak:list> focus points.<br>
    </td>
  </tr>
</mak:list>

  <tr class="table_row_1">
  <td width="100%" valign="top">
  <input onClick="history.back()" type="button" value="Back" />
  </td>
  </tr>
</table>
</body>
</html>