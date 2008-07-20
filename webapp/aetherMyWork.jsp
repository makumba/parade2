<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="org.makumba.parade.aether.ObjectTypes"%>

<html>
<head>

<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/icons_silk.css" type="text/css" media="all" />

</head>
<body>
<jsp:useBean id="aetherBean" class="org.makumba.parade.aether.AetherBean" />

<form action="cvsCommit.jsp" target="command" method="post">
<mak:list from="ALE a" where="a.user = :user_login and a.focus > 20" orderBy="a.focus desc, a.objectURL desc">
  <mak:value expr="a.objectURL" printVar="objectURL" />
  <input type="hidden" name="getPathFromSession" value="true">
  <input type="hidden" name="context" value="${sessionScope.currentContext}">
  <mak:object from="File f" where="f.fileURL() = a.objectURL">
    <mak:value expr="f.cvsStatus" var="cvsStatus"/><c:if test="${cvsStatus == 1}"><c:set var="commitPossible" value="true"/><input type="checkbox" name="file" value="${objectURL}"></c:if>
  </mak:object>
  <strong><a target="directory" href="<%=aetherBean.getResourceLink(objectURL, false)%>"><%=ObjectTypes.objectNameFromURL(objectURL)%></a></strong>
  <a class="icon_edit" target="directory" href="<%=aetherBean.getResourceLink(objectURL, true)%>"></a> (<a target="command" title="What does <mak:value expr="a.focus" /> mean?" href="/aetherFocusHistory.jsp?objectURL=${objectURL}"><mak:value expr="a.focus" /></a>) &nbsp;&nbsp; <a href="/aetherGetActionEffects.jsp?objectURL=${objectURL}&objectType=FILE&action=save&user=<%=request.getSession().getAttribute("user_login") %>" target="command" class="icon_info" title="Who gets notified if I change this file?"></a>
<br>
</mak:list>
<c:if test="${mak:lastCount() != 0 and commitPossible}">
<br>
<input type="submit" value="Commit">
</c:if>
</form>

</body>
</html>