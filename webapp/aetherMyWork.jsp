<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="org.makumba.parade.aether.ObjectTypes"%>

<html>
<head>

<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/icons_silk.css" type="text/css" media="all" />
</head>
<body>
<jsp:useBean id="aetherBean" class="org.makumba.parade.aether.AetherBean" />

<%-- FILES --%>
<mak:list from="ALE a" where="a.user = :user_login and a.focus > 20 and a.isFile()" id="0">
<mak:value expr="max(a.focus)" printVar="maxFileFocus"/><%request.setAttribute("maxFileFocus", maxFileFocus); %>
</mak:list>

<form action="/commandView/cvsCommit.jsp" target="command" method="post" style="display: inline;	margin:0 0 0 0;">
  <input type="hidden" name="getPathFromSession" value="true">
  
  <mak:list from="ALE a" where="a.user = :user_login and a.focus > 20 and a.isFile()" orderBy="a.focus desc, a.objectURL desc">
    <mak:value expr="a.objectURL" printVar="objectURL" />
    <mak:value expr="a.focus" printVar="fileFocus" />
  
    <mak:object from="File f" where="f.fileURL() = a.objectURL">
      <mak:value expr="f.cvsStatus" var="cvsStatus"/><c:if test="${cvsStatus == 1}"><c:set var="commitPossible" value="true"/><input type="checkbox" name="file" value="${objectURL}"></c:if>
    </mak:object>

  <c:choose>
    <c:when test="${(fileFocus / maxFileFocus) * 100 == 100}">
      <c:set var="objectWeight" value="large" />
    </c:when>
    <c:when test="${((fileFocus / maxFileFocus) * 100) > 75 and ((fileFocus / maxNimbus) * 100) < 100}">
      <c:set var="objectWeight" value="medium" />
    </c:when>
    <c:when test="${((fileFocus / maxFileFocus) * 100) > 50 and ((fileFocus / maxNimbus) * 100) < 75}">
      <c:set var="objectWeight" value="small" />
    </c:when>
    <c:when test="${((fileFocus / maxFileFocus) * 100) > 25 and ((fileFocus / maxNimbus) * 100 )< 50}">
      <c:set var="objectWeight" value="x-small" />
    </c:when>
    <c:when test="${((fileFocus / maxFileFocus) * 100) >= 0 and ((fileFocus / maxNimbus) * 100) < 25}">
      <c:set var="objectWeight" value="xx-small" />
    </c:when>

  </c:choose>

    <font style="font-size: ${objectWeight};">
      <strong><a class="icon_file" title="<%=ObjectTypes.rowNameFromURL(objectURL) %>" target="directory" href="<%=aetherBean.getResourceLink(objectURL, false)%>"><%=ObjectTypes.objectNameFromURL(objectURL)%></a></strong>
      <a class="icon_edit" target="directory" href="<%=aetherBean.getResourceLink(objectURL, true)%>"></a> (<a target="command" title="What does <mak:value expr="a.focus" /> mean?" href="/aetherFocusHistory.jsp?objectURL=${objectURL}"><mak:value expr="a.focus" /></a>) <a href="/aetherGetActionEffects.jsp?objectURL=${objectURL}&objectType=FILE&action=save&user=<%=request.getSession().getAttribute("user_login") %>" target="command" class="icon_info" title="Who gets notified if I change this file?"></a>
    </font>
    <br>
  </mak:list>
  <c:if test="${mak:lastCount() != 0 and commitPossible}"><br><input type="submit" value="Commit"><br></c:if>
</form>
<br>

<%-- DIRS --%>
<mak:list from="ALE a" where="a.user = :user_login and a.focus > 20 and a.isDir()">
<mak:value expr="max(a.focus)" printVar="maxDirFocus"/><%request.setAttribute("maxDirFocus", maxDirFocus); %>
</mak:list>


<mak:list from="ALE a" where="a.user = :user_login and a.focus > 20 and a.isDir()" orderBy="a.focus desc, a.objectURL desc" limit="5">
  <mak:value expr="a.objectURL" printVar="objectURL" />
  <mak:value expr="a.focus" printVar="dirFocus" />

  <c:choose>
    <c:when test="${(dirFocus / maxDirFocus) * 100 == 100}">
      <c:set var="objectWeight" value="large" />
    </c:when>
    <c:when test="${((dirFocus / maxDirFocus) * 100) > 75 and ((dirFocus / maxDirFocus) * 100) < 100}">
      <c:set var="objectWeight" value="medium" />
    </c:when>
    <c:when test="${((dirFocus / maxDirFocus) * 100) > 50 and ((dirFocus / maxDirFocus) * 100) < 75}">
      <c:set var="objectWeight" value="small" />
    </c:when>
    <c:when test="${((dirFocus / maxDirFocus) * 100) > 25 and ((dirFocus / maxDirFocus) * 100 )< 50}">
      <c:set var="objectWeight" value="x-small" />
    </c:when>
    <c:when test="${((dirFocus / maxDirFocus) * 100) >= 0 and ((dirFocus / maxDirFocus) * 100) < 25}">
      <c:set var="objectWeight" value="xx-small" />
    </c:when>
  </c:choose>

  <font style="font-size: ${objectWeight};">
  <strong><a class="icon_folder" title="<%=ObjectTypes.rowNameFromURL(objectURL) %>" target="directory" href="<%=aetherBean.getResourceLink(objectURL, false)%>"><%=ObjectTypes.objectNameFromURL(objectURL)%></a> (<a target="command" title="What does <mak:value expr="a.focus" /> mean?" href="/aetherFocusHistory.jsp?objectURL=${objectURL}"><mak:value expr="a.focus" /></a>)</strong>
  </font>  
<br>
</mak:list>
<br>

<%-- ROWs --%>
<mak:list from="ALE a" where="a.user = :user_login and a.focus > 20 and a.isRow()">
<mak:value expr="max(a.focus)" printVar="maxRowFocus"/><%request.setAttribute("maxRowFocus", maxRowFocus); %>
</mak:list>


<mak:list from="ALE a" where="a.user = :user_login and a.focus > 20 and a.isRow()" orderBy="a.focus desc, a.objectURL desc" limit="3">
  <mak:value expr="a.objectURL" printVar="objectURL" />
  <mak:value expr="a.focus" printVar="rowFocus" />

  <c:choose>
    <c:when test="${(rowFocus / maxRowFocus) * 100 == 100}">
      <c:set var="objectWeight" value="large" />
    </c:when>
    <c:when test="${((rowFocus / maxRowFocus) * 100) > 75 and ((dirFocus / maxDirFocus) * 100) < 100}">
      <c:set var="objectWeight" value="medium" />
    </c:when>
    <c:when test="${((rowFocus / maxRowFocus) * 100) > 50 and ((dirFocus / maxDirFocus) * 100) < 75}">
      <c:set var="objectWeight" value="small" />
    </c:when>
    <c:when test="${((rowFocus / maxRowFocus) * 100) > 25 and ((dirFocus / maxDirFocus) * 100 )< 50}">
      <c:set var="objectWeight" value="x-small" />
    </c:when>
    <c:when test="${((rowFocus / maxRowFocus) * 100) >= 0 and ((dirFocus / maxDirFocus) * 100) < 25}">
      <c:set var="objectWeight" value="xx-small" />
    </c:when>
  </c:choose>

  <font style="font-size: ${objectWeight};">
  <strong><a class="icon_project" target="directory" href="<%=aetherBean.getResourceLink(objectURL, false)%>"><%=ObjectTypes.objectNameFromURL(objectURL)%></a></strong>
  </font>
  <br>
</mak:list>

</body>
</html>