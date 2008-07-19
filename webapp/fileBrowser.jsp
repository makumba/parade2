<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<%-- === Setting the parameters === --%>

<%-- Context --%>
<c:set var="context" value="${param.context}" />
<c:if test="${empty context}">
  <c:set var="context" value="${requestScope.context}" />
</c:if>

<%-- Path (to be taken from session or not) --%>
<c:choose>
  <c:when test="${not empty param.getPathFromSession and param.getPathFromSession}">
    <c:set var="path" value="${sessionScope.path}" />
  </c:when>
  <c:otherwise>
    <c:set var="path" value="${param.path}" />
    <c:if test="${empty path}">
      <c:set var="path" value="${requestScope.path}" />
    </c:if>
  </c:otherwise>
</c:choose>

<c:if test="${not empty path and path != ''}">
  <c:set var="path" value="${path}" scope="session" />
</c:if>

<%-- Display order --%>
<c:set var="order" value="${param.order}" />
<c:if test="${empty order}">
  <c:set var="order" value="${requestScope.order}" />
</c:if>

<%-- Operation result --%>
<c:set var="result" value="${requestScope.result}" />
<c:set var="success" value="${requestScope.success}" />
<c:if test="${empty success}">
  <c:set var="success" value="${false}" />
</c:if>

<%-- === END setting the parameters === --%>

<%-- Getting the bean that will compute some of the data --%>
<jsp:useBean id="fileBrowserBean" class="org.makumba.parade.view.beans.FileBrowserBean" scope="request" />
<jsp:setProperty name="fileBrowserBean" property="context" value="${context}" />
<jsp:setProperty name="fileBrowserBean" property="path" value="${path}" />

<%-- Re-setting the cleaned path --%>
<c:set var="path"><jsp:getProperty name="fileBrowserBean" property="path" /></c:set>


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


<h2 class="files">[<a href='/servlet/browse?display=file&context=${context}'>${context}</a>]/<c:forEach
  var="parentDir" items="${fileBrowserBean.parentDirs}">
  <a href='/servlet/browse?display=file&context=${context}&path=${parentDir["path"]}'>${parentDir["directoryName"]}</a>/</c:forEach><img
  src='/images/folder-open.gif'></h2>
<div class='pathOnDisk'>${fileBrowserBean.pathOnDisk}</div>
<table class='files'>
  <tr>
    <th></th>
    <th colspan='2'><a href='/File.do?display=command&view=newDir&context=${context}&path=${path}' target='command'
      title='Create a new directory'><img src='/images/newfolder.gif' align='right'></a> <a
      href='/uploadFile.jsp?context=${context}&path=${path}' target='command' title='Upload a file'><img
      src='/images/uploadfile.gif' align='right'></a> <a
      href='/File.do?display=command&view=newFile&context=${context}&path=${path}' target='command'
      title='Create a new file'><img src='/images/newfile.gif' align='right'></a> <a
      href='/servlet/browse?display=file&context=${context}&path=${path}&order=name' title='Order by name'>Name</a></th>
    <th><a href='/servlet/browse?display=file&context=${context}&path=${path}&order=age' title='Order by age'>Age</a></th>
    <th><a href='/servlet/browse?display=file&context=${context}&path=${path}&order=size' title='Order by size'>Size</a></th>

    <script language="JavaScript">
<!--
function deleteFile(path, name) {
if(confirm('Are you sure you want to delete the file '+name+' ?'))
{
  url='/File.do?display=file&context=${context}&path=${fileBrowserBean.pathEncoded}&op=deleteFile&params='+encodeURIComponent(name);
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


  <%-- Setting sortGBy param for file sorting --%>
  <c:choose>
    <c:when test="${empty order}">
      <c:set var="sortBy" value="f.isDir desc, f.name asc" />
    </c:when>
    <c:when test="${order == 'name'}">
      <c:set var="sortBy" value="f.isDir desc, f.name asc" />
    </c:when>
    <c:when test="${order == 'age'}">
      <c:set var="sortBy" value="f.isDir desc, f.date asc" />
    </c:when>
    <c:when test="${order == 'size'}">
      <c:set var="sortBy" value="f.isDir desc, f.size asc" />
    </c:when>
    <c:otherwise>
      <c:set var="sortBy" value="f.isDir desc, f.name asc" />
    </c:otherwise>
  </c:choose>

  <c:set var="absolutePath" value="${fileBrowserBean.absolutePath}" />
  <c:if test="${empty absolutePath or absolutePath == '' }">
    <c:set var="absolutePath" value="${fileBrowserBean.absoluteRowPath}" />
  </c:if>

  <mak:object from="File f" where="f.path = :absolutePath">
    <mak:list from="File child" where="f.parentPath = :keyPath and f.row.rowname = :context" orderBy="#{sortBy}">
    </mak:list>
  </mak:object>



  <%-- 
<#list fileViews as file>
<tr class="<#if (file_index % 2) = 0>odd<#else>even</#if>">

<#include "fileBrowserFile.ftl">
<#include "fileBrowserCVS.ftl" >

</tr>
        
</#list>
 --%>
</table>

</body>
</html>