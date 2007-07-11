<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:html locale="true">
<head>
<title>ParaDe file upload</title>
<html:base/>
</head>
<body bgcolor="white">
<html:form action="/FileUpload" method="post" enctype="multipart/form-data">
Choose file: <html:file property="theFile"/> 

<input type="hidden" name="context" value="${param.context}"/>
<input type="hidden" name="path" value="${param.path}"/>
<input type="hidden" name="op" value="upload"/>

<html:submit>Upload File</html:submit>

</html:form>
</body>
</html:html>