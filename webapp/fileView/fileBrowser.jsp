<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="org.makumba.parade.tools.DisplayFormatter"%>
<%@page import="org.makumba.parade.view.beans.FileBrowserBean"%>
<%@page import="java.util.Date"%>

<%@include file="../setParameters.jspf" %>

<%-- Getting the bean that will compute some of the data --%>
<% FileBrowserBean fileBrowserBean = new FileBrowserBean();
   fileBrowserBean.setContext((String)pageContext.getAttribute("context"));
   fileBrowserBean.setPath((String)pageContext.getAttribute("path"));%>

<%-- Re-setting the cleaned path --%>
<c:set var="path"><%=fileBrowserBean.getPath() %></c:set>
<c:set var="pathEncoded"><%=fileBrowserBean.encode(fileBrowserBean.getPath()) %></c:set>


<html>
<head>
<title>File browser for row ${param.context}</title>

<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/files.css" type="text/css">
<link rel="StyleSheet" href="${pageContext.request.contextPath}/layout/style/parade.css" type="text/css">

</head>
<body class="files">

<c:if test="${not empty result}">
  <c:choose>
    <c:when test="${success}">
      <div class='success'>${result}</div>
    </c:when>
    <c:otherwise>
      <div class='failure'>${result}</div>
    </c:otherwise>
  </c:choose>
</c:if>

<% pageContext.setAttribute("parentDirs", fileBrowserBean.getParentDirs()); %>

<h2 class="files">[<a href='/fileView/fileBrowser.jsp?context=${context}'>${context}</a>]/<c:forEach
  var="parentDir" items="${parentDirs}"><a href='/fileView/fileBrowser.jsp?context=${context}&path=${parentDir["path"]}'>${parentDir["directoryName"]}</a>/</c:forEach><img src='/images/folder-open.gif'></h2>
<div class='pathOnDisk'><%=fileBrowserBean.getPathOnDisk() %></div>
<table class='files'>
  <tr>
    <th></th>
    <th colspan='2'><a href='/commandView/newDir.jsp?context=${context}&path=${path}' target='command'
      title='Create a new directory'><img src='/images/newfolder.gif' align='right'></a> <a
      href='/commandView/uploadFile.jsp?context=${context}&path=${path}' target='command' title='Upload a file'><img
      src='/images/uploadfile.gif' align='right'></a> <a
      href='/commandView/newFile.jsp?context=${context}&path=${path}' target='command'
      title='Create a new file'><img src='/images/newfile.gif' align='right'></a> <a
      href='/fileView/fileBrowser.jsp?context=${context}&path=${path}&order=name' title='Order by name'>Name</a></th>
    <th><a href='/fileView/fileBrowser.jsp?context=${context}&path=${path}&order=age' title='Order by age'>Age</a></th>
    <th><a href='/fileView/fileBrowser.jsp?context=${context}&path=${path}&order=size' title='Order by size'>Size</a></th>

    <script language="JavaScript">
<!--
function deleteFile(path, name) {
if(confirm('Are you sure you want to delete the file '+name+' ?'))
{
  url='/File.do?context=${context}&path=${pathEncoded}&op=deleteFile&params='+encodeURIComponent(name);
  location.href=url;
}
}
-->
</script>
    <th>CVS <a href='/Cvs.do?op=check&context=${context}&params=${path}' target='command' title='CVS check status'><img
      src='/images/cvs-query.gif' alt='CVS check status' border='0'></a> <a
      href='/Cvs.do?op=update&context=${context}&params=${path}' target='command' title='CVS local update'><img
      src='/images/cvs-update.gif' alt='CVS local update' border='0'></a> <a
      href='/Cvs.do?op=rupdate&context=${context}&params=${path}' target='command' title='CVS recursive update'><img
      src='/images/cvs-update.gif' alt='CVS recursive update' border='0'></a></th>
  </tr>


  <%-- Setting sortBy param for file sorting --%>
  <c:choose>
    <c:when test="${empty order}">
      <c:set var="sortBy" value="child.isDir desc, child.name asc" />
    </c:when>
    <c:when test="${order == 'name'}">
      <c:set var="sortBy" value="child.isDir desc, child.name asc" />
    </c:when>
    <c:when test="${order == 'age'}">
      <c:set var="sortBy" value="child.isDir desc, child.date asc" />
    </c:when>
    <c:when test="${order == 'size'}">
      <c:set var="sortBy" value="child.isDir desc, child.size asc" />
    </c:when>
    <c:otherwise>
      <c:set var="sortBy" value="child.isDir desc, child.name asc" />
    </c:otherwise>
  </c:choose>

  <c:set var="absolutePath"><%=fileBrowserBean.getAbsolutePath() %></c:set>

  <mak:object from="File parent" where="parent.path = :absolutePath">
<%-- and (length(child.path) >= length(parent.row.rowpath)) --%>
    <mak:list from="File child" where="child.parentPath = parent.path" orderBy="#{sortBy}">

    <%--Common values used by both file and cvs view --%>
    <mak:value expr="child.isDir" var="isDir"/>
    <mak:value expr="child.name" printVar="fileName"/>
    <mak:value expr="child.path" printVar="filePath"/>
    <mak:value expr="child.fileURL()" printVar="fileURL"/>
    <c:set var="relativeFilePath"><%=fileBrowserBean.getPath(fileName) %></c:set>
    <c:set var="encodedURL"><%=fileBrowserBean.encode(fileURL) %></c:set>
<tr>
<%@include file="fileBrowserFile.jspf" %>
<%@include file="fileBrowserCVS.jspf" %>
</tr>

    </mak:list>
  </mak:object>

</table>

</body>
</html>