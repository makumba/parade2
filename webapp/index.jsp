<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="org.makumba.parade.init.InitServlet"%>

<html>
<head>
<title>Welcome to ParaDe!</title>
  	<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/icons_silk.css" type="text/css" media="all"/>
  	<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/rowstore.css" type="text/css">
  	<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/parade.css" type="text/css">
</head>

<% if(InitServlet.aetherEnabled) {%>
<FRAMESET rows="*,20%" border="0">  
  <FRAME name="main" src="/showRows.jsp" marginwidth="0" marginheight="0">
  <FRAME name="bottom" src="/aetherView.jsp" marginwidth="0" marginheight="0">
</FRAMESET>
<%} else {%>
<FRAMESET rows="100%" border="0">  
  <FRAME name="main" src="/showRows.jsp" marginwidth="0" marginheight="0">
</FRAMESET>
<% } %>

</html>
