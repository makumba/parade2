<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:html locale="true">
<head>
<title>ParaDe file upload</title>
<base href="<%
String referer = request.getHeader("Referer");
String rurl = referer.substring(0, referer.indexOf("?"));
String context = request.getParameter("context");
if(rurl.startsWith("http://")) rurl=rurl.substring(7);
rurl = rurl.substring(0, rurl.indexOf("/"));
if(!rurl.endsWith("/")) rurl += "/";
out.print("http://"+rurl + context); %>" >
</head>
<body bgcolor="white">
<html:form action="/FileUpload" method="post" enctype="multipart/form-data">
Choose file: <html:file property="theFile"/> 
<input type="hidden" name="context" value="${param.context}"/>
<input type="hidden" name="path" value="${param.path}"/>
<input type="hidden" name="op" value="upload"/>
<html:submit>Upload File</html:submit>
<br><font style="font-size: smaller;">(in ${param.context}/${param.path})</font>

</html:form>
</body>
</html:html>