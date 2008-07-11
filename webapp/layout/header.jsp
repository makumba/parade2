<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><% 
/* $Header$ */ 
%><%@ taglib uri="http://www.makumba.org/presentation" prefix="mak" 
%><%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
  <c:when test="${!empty param.pageTitle}"><c:set var="pageTitle" value="${param.pageTitle}" scope="request"/></c:when>
  <c:otherwise><c:set var="pageTitle" value="${param.title}" scope="request"/></c:otherwise>
</c:choose>

<html>
<head>
<title>${param.pageTitle}</title>

<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/icons_silk.css" type="text/css" media="all"/>
<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/log.css" type="text/css">
<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/parade.css" type="text/css">
<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/header.css" type="text/css">
<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/command.css" type="text/css">
<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/files.css" type="text/css">
<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/tree.css" type="text/css">
<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/rowstore.css" type="text/css">
<link rel="StyleSheet" href="${pageContext.request.contextPath}/scripts/tickertape/tickertape.css">

<script src="${pageContext.request.contextPath}/scripts/codepress/codepress.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/treeMenu/sniffer.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/treeMenu/TreeMenu.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/CalendarPopup.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/utils.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/prototype.js" type="text/javascript"></script>

<script type="text/javascript">
function pointToBottom(){
  window.location=window.location.href.replace( /(#.*)?$/,'')+'#bottomlink';
}
</script>

<c:if test="${not empty param.baseTarget}"><base target="${param.baseTarget}"></c:if>

</head>

<body<c:if test="${not empty param.class}"> class="${param.class}"</c:if><c:if test="${not empty param.pointToBottom}"> onLoad="pointToBottom();"</c:if>>
<div id="container">
<div>
<a name="topOfthePage"></a>
<% if (request.getAttribute("makumba.response") != null && !request.getAttribute("makumba.response").equals("")) { %>
<div class="mak_response"><mak:response/></div>
<% } %>
</div>