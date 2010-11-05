<%-- File browser view: composes the file browser header, lists files according to the sorting and includes file and CVS specific data--%>
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<jsp:useBean id="fileBrowser"
	class="org.makumba.parade.view.beans.FileBrowserBean" />
<%@include file="../setParameters.jspf"%>

<%-- Getting the bean that will compute some of the data --%>
<%
	fileBrowser.setPath((String) pageContext.getAttribute("path"));
%>
<%-- Re-setting the cleaned path --%>
<c:set var="path"><%=fileBrowser.getPath()%></c:set>
<c:set var="pathEncoded"><%=fileBrowser.encode(fileBrowser.getPath())%></c:set>

<mak:object from="Row r" where="r.rowname = :context">
	<mak:value expr="r.application.id" var="applicationId" />
	<mak:value expr="r.module" printVar="rowmodule" />
	<mak:value expr="r.rowpath" printVar="rowpath" />
	<mak:value expr="r.rowname" printVar="rowname" />
	<mak:value expr="r.status" printVar="rowstatus" />
	<mak:value expr="r.webappPath" printVar="webapppath" />
	<%
		fileBrowser.setRow();
			fileBrowser.setModule(rowmodule);
			fileBrowser.setRowpath(rowpath);
			fileBrowser.setRowname(rowname);
			fileBrowser.setStatus(rowstatus);
			fileBrowser.setWebappPath(webapppath);
	%>
	<html>
	<head>
	<title>File browser for row <mak:value expr="r.rowname" /></title>
	<link rel="StyleSheet"
		href="${pageContext.request.contextPath}/layout/style/files.css"
		type="text/css">
	<link rel="StyleSheet"
		href="${pageContext.request.contextPath}/layout/style/parade.css"
		type="text/css">
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

	<%
		pageContext.setAttribute("parentDirs", fileBrowser
					.getParentDirs());
	%>

	<h2 class="files">[<a
		href='/fileView/fileBrowser.jsp?context=${context}'>${context}</a>]/<c:forEach
		var="parentDir" items="${parentDirs}">
		<a
			href='/fileView/fileBrowser.jsp?context=${context}&path=${parentDir["path"]}'>${parentDir["directoryName"]}</a>/</c:forEach><img
		src='/images/folder-open.gif'></h2>
	<div class='pathOnDisk'><%=fileBrowser.getPathOnDisk()%></div>
	<table class='files'>
		<tr>
			<th></th>
			<th colspan='2'><a
				href='/commandView/newDir.jsp?context=${context}&path=${path}'
				target='command' title='Create a new directory'><img
				src='/images/newfolder.gif' align='right'></a> <a
				href='/commandView/uploadFile.jsp?context=${context}&path=${path}'
				target='command' title='Upload a file'><img
				src='/images/uploadfile.gif' align='right'></a> <a
				href='/commandView/newFile.jsp?context=${context}&path=${path}'
				target='command' title='Create a new file'><img
				src='/images/newfile.gif' align='right'></a> <a
				href='/fileView/fileBrowser.jsp?context=${context}&path=${path}&order=name'
				title='Order by name'>Name</a></th>
			<th><a
				href='/fileView/fileBrowser.jsp?context=${context}&path=${path}&order=age'
				title='Order by age'>Age</a></th>
			<th><a
				href='/fileView/fileBrowser.jsp?context=${context}&path=${path}&order=size'
				title='Order by size'>Size</a></th>
			<th>CVS <a
				href='/Cvs.do?op=check&context=${context}&path=${path}'
				target='command' title='CVS check status'><img
				src='/images/cvs-query.gif' alt='CVS check status' border='0'></a>
			<a href='/Cvs.do?op=update&context=${context}&path=${path}'
				target='command' title='CVS local update'><img
				src='/images/cvs-update.gif' alt='CVS local update' border='0'></a>
			<a href='/Cvs.do?op=rUpdate&context=${context}&path=${path}'
				target='command' title='CVS recursive update'><img
				src='/images/cvs-update.gif' alt='CVS recursive update' border='0'></a></th>
		</tr>

		<%-- Setting sortBy param for file sorting --%>
		<c:choose>
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

		<c:set var="absolutePath"><%=fileBrowser.getAbsolutePath()%></c:set>

		<mak:list from="File f"
			where="f.parentPath = :absolutePath and (length(f.path) >= length(f.row.rowpath))"
			orderBy="#{sortBy}">
			<%--Common values used by both file and cvs view --%>
			<mak:value expr="f.isDir" var="isDir" />
			<mak:value expr="f.name" printVar="fileName" />
			<mak:value expr="f.path" var="filePath" />
			<mak:value expr="f.onDisk" var="isOnDisk" />
			<mak:value expr="f.fileURL()" printVar="fileURL" />
			<c:set var="relativeFilePath"><%=fileBrowser.getPath(fileName)%></c:set>
			<c:set var="encodedURL"><%=fileBrowser.encode(fileURL)%></c:set>
			<tr>
				<%@include file="fileBrowserFile.jspf"%>
				<%@include file="fileBrowserCVS.jspf"%>
			</tr>
		</mak:list>
	</table>
	</body>
	</html>
</mak:object>