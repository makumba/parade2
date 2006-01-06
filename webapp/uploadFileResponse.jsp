<%-- $Header$ --%>
<%@ page import="java.io.*" %>

<%@ taglib uri="http://www.makumba.org/presentation" prefix="mak" %>

<html>
<body>
<mak:attribute name="context" var="context"/>
<mak:attribute name="path" var="path"/>
<mak:attribute name="theFile_filename" var="file"/>

<h4>Uploading file:</h4>
file: <%=context%>/<%=path+File.separator+file%><br>
content type: <mak:attribute name="theFile_contentType" /><br>
content length: <mak:attribute name="theFile_contentLength" /><br>
Saving to file: <%=path+File.separator+file%>...

<% // we need to do this in order for the attribute to be extracted %>
<mak:attribute name="theFile" var="content" />

<jsp:useBean id="view" class="org.makumba.parade.view.managers.CommandViewManager" scope="application"/>

<% String result = view.uploadFile((String)path, (String)file, content, context);
	if(result.equals("")) out.println(result);
%>

<b>done</b>.

<script language="JavaScript">
<!-- 
top.frames["directory"].document.location.href="/servlet/file?context=<%=context%>&path=<%=path + java.io.File.separator%>"
// -->
</script>
</body>
</html>