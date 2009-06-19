<%-- Aether my work: shows the objects a user works on, sorted by focus, and separated into files, directories and rows --%>

<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="org.makumba.parade.aether.ObjectTypes"%>
<%@page import="org.makumba.parade.init.InitServlet"%>
<%@page import="org.makumba.commons.ReadableFormatter"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>

<jsp:useBean id="aetherBean" class="org.makumba.parade.aether.AetherBean" />
<form name="fileform" action="/commandView/cvsCommit.jsp" target="bottom" method="post" style="display: inline;	margin:0 0 0 0;">
<table width="100%" border="1" cellspacing="0" cellpadding="0" class="table_border">

<%-- Do an Aether check first --%>
<% if(InitServlet.aetherEnabled) {%>

<%-- FILES --%>
<mak:list from="ALE a" where="a.user = :user_login and a.focus > 20 and a.isFile()" id="0">
<mak:value expr="max(a.focus)" printVar="maxFileFocus"/><%request.setAttribute("maxFileFocus", maxFileFocus); %>
</mak:list>
  <mak:list from="ALE a" where="a.user = :user_login and a.focus > 20 and a.isFile()" orderBy="a.focus desc, a.objectURL desc">
    <mak:value expr="a.objectURL" printVar="objectURL" />
    <mak:value expr="a.focus" printVar="fileFocus" />

  <c:choose>
    <c:when test="${(fileFocus / maxFileFocus) * 100 == 100}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="900" />
      <c:set var="fontColor" value="red" />
    </c:when>
    <c:when test="${((fileFocus / maxFileFocus) * 100) > 75 and ((fileFocus / maxNimbus) * 100) < 100}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="700" />
      <c:set var="fontColor" value="black" />
    </c:when>
    <c:when test="${((fileFocus / maxFileFocus) * 100) > 50 and ((fileFocus / maxNimbus) * 100) < 75}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="500" />
      <c:set var="fontColor" value="black" />
    </c:when>
    <c:when test="${((fileFocus / maxFileFocus) * 100) > 25 and ((fileFocus / maxNimbus) * 100 )< 50}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="200" />
      <c:set var="fontColor" value="black" />
    </c:when>
    <c:when test="${((fileFocus / maxFileFocus) * 100) >= 0 and ((fileFocus / maxNimbus) * 100) < 25}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="100" />
      <c:set var="fontColor" value="black" />
    </c:when>
  </c:choose>
  <c:set var="count" value="${mak:count()}" />

<c:choose>
    <c:when test="${count % 2 == 0}">
      <c:set var="table_row" value="table_row_1" />
    </c:when>
    <c:otherwise>
      <c:set var="table_row" value="table_row_2" />
    </c:otherwise>
</c:choose>  
  
<tr id="tr${count}" class="${table_row}">
<td width="100%" height="25" valign="top">
    <mak:object from="File f" where="f.fileURL() = a.objectURL">
      <mak:value expr="f.cvsStatus" var="cvsStatus"/>
      <c:if test="${cvsStatus == 1}">
      <c:set var="commitPossible" value="true"/>
      	<input onClick="JavaScript: highlight('tr${count}', '${table_row}', this.id);" id="cb${count}" type="checkbox" name="file" value="${objectURL}">
      </c:if>
    </mak:object>
  
  <font style="font-size: ${fontSize}; font-weight: ${fontWeight}; color: ${fontColor};">
      <strong>
      <a class="icon_file" title="<%=ObjectTypes.rowNameFromURL(objectURL) %>" target="directory" href="<%=aetherBean.getResourceLink(objectURL, false)%>"><%=ObjectTypes.objectNameFromURL(objectURL)%></a>
      </strong>
      <a class="icon_edit" target="directory" href="<%=aetherBean.getResourceLink(objectURL, true)%>"></a> 
      (<font style="font-size: small";><a target="bottom" title="What does <mak:value expr="a.focus" /> mean?" href="/aetherFocusHistory.jsp?objectURL=${objectURL}"><mak:value expr="a.focus" /></a>) <a href="/aetherGetActionEffects.jsp?objectURL=${objectURL}&objectType=FILE&action=save&user=<%=request.getSession().getAttribute("user_login") %>" target="command" class="icon_info" title="Who gets notified if I change this file?"></font></a>
  </font>
    	
</td>
</tr>
  </mak:list>

<c:choose>
<c:when test="${count != 0}">
<tr class="table_final">
  <td width="100%" valign="top" >
	<font style="font-size: x-small;">&nbsp;<a href="JavaScript: check_all();">Check</a> / <a href="JavaScript: uncheck_all();">uncheck</a> all
	- last updated on: 
  <% 
	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
  	Date date = new Date();
  	%><%=dateFormat.format(date) %>
	<br>
	</font>  	
  		<input type="hidden" name="getPathFromSession" value="true">
  		<input type="submit" value="Commit">
  	
  </td>
</tr> 
</c:when>
</c:choose>

<%-- DIRS --%>
<mak:list from="ALE a" where="a.user = :user_login and a.focus > 20 and a.isDir()">
<mak:value expr="max(a.focus)" printVar="maxDirFocus"/><%request.setAttribute("maxDirFocus", maxDirFocus); %>
</mak:list>

<mak:list from="ALE a" where="a.user = :user_login and a.focus > 20 and a.isDir()" orderBy="a.focus desc, a.objectURL desc" limit="5">
  <mak:value expr="a.objectURL" printVar="objectURL" />
  <mak:value expr="a.focus" printVar="dirFocus" />

  <c:choose>
    <c:when test="${(dirFocus / maxDirFocus) * 100 == 100}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="900" />
      <c:set var="fontColor" value="red" />
      <c:set var="table_row" value="table_row_highest" />
    </c:when>
    <c:when test="${((dirFocus / maxDirFocus) * 100) > 75 and ((dirFocus / maxDirFocus) * 100) < 100}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="700" />
      <c:set var="fontColor" value="black" />
      <c:set var="table_row" value="table_row_high" />
    </c:when>
    <c:when test="${((dirFocus / maxDirFocus) * 100) > 50 and ((dirFocus / maxDirFocus) * 100) < 75}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="500" />
      <c:set var="fontColor" value="black" />
      <c:set var="table_row" value="table_row_medium" />
    </c:when>
    <c:when test="${((dirFocus / maxDirFocus) * 100) > 25 and ((dirFocus / maxDirFocus) * 100 )< 50}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="200" />
      <c:set var="fontColor" value="black" />
      <c:set var="table_row" value="table_row_low" />
    </c:when>
    <c:when test="${((dirFocus / maxDirFocus) * 100) >= 0 and ((dirFocus / maxDirFocus) * 100) < 25}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="100" />
      <c:set var="fontColor" value="black" />
      <c:set var="table_row" value="table_row_lowest" />
    </c:when>
  </c:choose>
</mak:list>

<%-- ROWs --%>
<mak:list from="ALE a" where="a.user = :user_login and a.focus > 20 and a.isRow()">
<mak:value expr="max(a.focus)" printVar="maxRowFocus"/><%request.setAttribute("maxRowFocus", maxRowFocus); %>
</mak:list>

<mak:list from="ALE a" where="a.user = :user_login and a.focus > 20 and a.isRow()" orderBy="a.focus desc, a.objectURL desc" limit="3">
  <mak:value expr="a.objectURL" printVar="objectURL" />
  <mak:value expr="a.focus" printVar="rowFocus" />

  <c:choose>
    <c:when test="${(rowFocus / maxRowFocus) * 100 == 100}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="900" />
      <c:set var="fontColor" value="red" />
      <c:set var="table_row" value="table_row_highest" />
    </c:when>
    <c:when test="${((rowFocus / maxRowFocus) * 100) > 75 and ((dirFocus / maxDirFocus) * 100) < 100}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="700" />
      <c:set var="fontColor" value="black" />
      <c:set var="table_row" value="table_row_high" />
    </c:when>
    <c:when test="${((rowFocus / maxRowFocus) * 100) > 50 and ((dirFocus / maxDirFocus) * 100) < 75}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="500" />
      <c:set var="fontColor" value="black" />
      <c:set var="table_row" value="table_row_medium" />
    </c:when>
    <c:when test="${((rowFocus / maxRowFocus) * 100) > 25 and ((dirFocus / maxDirFocus) * 100 )< 50}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="200" />
      <c:set var="fontColor" value="black" />
      <c:set var="table_row" value="table_row_low" />
    </c:when>
    <c:when test="${((rowFocus / maxRowFocus) * 100) >= 0 and ((dirFocus / maxDirFocus) * 100) < 25}">
      <c:set var="fontSize" value="medium" />
      <c:set var="fontWeight" value="100" />
      <c:set var="fontColor" value="black" />
      <c:set var="table_row" value="table_row_lowest" />
    </c:when>
  </c:choose>
</mak:list>

<%-- If aether is disabled, show error message --%>
<% } else { %>
  <tr class="table_row_1">
 	<td width="100%" valign="top">
		<p class="text_row"><strong>Aether is disabled!</strong></p>
	</td>
  </tr>
<% } %>

</table>
</form>