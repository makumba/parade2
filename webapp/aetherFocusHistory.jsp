<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="org.makumba.parade.aether.ObjectTypes"%>


<%@page import="org.makumba.parade.aether.ActionTypes"%>
<%@page import="org.makumba.commons.ReadableFormatter"%>
<%@page import="java.util.Date"%><html>
<head>
<title>Your actions for ${param.objectURL}</title>

<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/icons_silk.css" type="text/css" media="all" />

</head>
<body>

<h3>Your actions for ${param.objectURL}</h3>

<mak:list from="MatchedAetherEvent mae" where="mae.actor = :user_login and mae.objectURL = :objectURL" orderBy="mae.eventDate desc" groupBy="mae.id">
<mak:value expr="mae.eventDate" var="eventDate"/><%=ReadableFormatter.readableAge(new Date().getTime() - ((Date) eventDate).getTime())%> ago, you <mak:value expr="mae.action" printVar="action"/><%=ActionTypes.getReadableAction(action) %> this file, which contributed with a total of <mak:list from="PercolationStep ps" where="ps.matchedAetherEvent.id = mae.id and ps.focus > 0" groupBy="ps.id, mae.id"><mak:value expr="sum(ps.focus)"/></mak:list> focus points.<br>
</mak:list>

</body>
</html>