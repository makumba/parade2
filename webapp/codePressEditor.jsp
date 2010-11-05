<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://www.makumba.org/presentation" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<jsp:useBean id="fileEditorBean"
	class="org.makumba.parade.view.beans.FileEditorBean" />

<c:set var="context" value="${param.context}" />
<c:if test="${empty context}">
	<c:set var="context" value="${requestScope.context}" />
</c:if>

<c:set var="path" value="${param.path}" />
<c:if test="${empty path}">
	<c:set var="path" value="${requestScope.path}" />
</c:if>

<c:set var="file" value="${param.file}" />
<c:if test="${empty file}">
	<c:set var="file" value="${requestScope.file}" />
</c:if>

<c:set var="source" value="${param.source}" />
<c:if test="${empty source}">
	<c:set var="source" value="${requestScope.source}" />
</c:if>

<jsp:setProperty name="fileEditorBean" property="context"
	value="${context}" />
<jsp:setProperty name="fileEditorBean" property="path" value="${path}" />
<jsp:setProperty name="fileEditorBean" property="file" value="${file}" />
<jsp:setProperty name="fileEditorBean" property="source"
	value="${source}" />

<html>
<head>
<title>File editor - ${file}</title>

<link rel="StyleSheet"
	href="${pageContext.request.contextPath}/layout/style/parade.css"
	type="text/css">

<script
	src="${pageContext.request.contextPath}/scripts/codepress/codepress.js"
	type="text/javascript"></script>
</head>

<body bgcolor="#dddddd" TOPMARGIN="0" LEFTMARGIN="0" RIGHTMARGIN="0"
	BOTTOMMARGIN="0" marginwidth="0" marginheight="0" STYLE="margin: 0px">

<form name="sourceEdit" method="post"
	action="/Edit.do?op=saveFile&editor=codePress&context=${context}&path=${path}&file=${file}"
	style="margin: 0px;"><input type="submit" name="Submit"
	value="(S)ave!" ACCESSKEY="S" onclick="getEditorCode();"> <a
	href="browse.jsp?context=${context}&getPathFromSession=false"
	target="_top" title="${context}">${context}</a>:<a
	href="/fileView/fileBrowser.jsp?context=${context}&path=${path}">${path}</a>/<b>${file}</b>
| <a
	href="/Edit.do?op=revertFile&editor=codePress&context=${context}&path=${path}&file=${file}"
	title="get the file from disk again, undo all changes since last save">Revert</a>
<br>

<div id="languages"><em>set language:</em>
<button onclick="myCpWindow.edit('myCpWindow','javascript')">JavaScript</button>
<button onclick="myCpWindow.edit('myCpWindow','java')">Java</button>
<button onclick="myCpWindow.edit('myCpWindow','html')">HTML</button>
<button onclick="myCpWindow.edit('myCpWindow','css')">CSS</button>
</div>

<textarea id="myCpWindow" class="codepress java"
	style="width: 100%; height: 92%" cols="90" rows="23" wrap="virtual"
	STYLE="font-face:Lucida Console; font-size:8pt">${fileEditorBean.content}</textarea>
<input name="source" type="hidden"></input> <script
	type="text/javascript">
	function getEditorCode() {
		sourceEdit.source.value = (myCpWindow.getCode())
	}
</script></form>

</body>
</html>