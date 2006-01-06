<%-- $Header$ --%>
<%@ taglib uri="http://www.makumba.org/presentation" prefix="mak" %>
<%
String context=request.getParameterValues("context")[0];

String path="";
if(request.getParameterValues("path")!=null)
	path=request.getParameterValues("path")[0];

%>
<mak:form action="/uploadFileResponse.jsp" method="post"  
	message="file uploaded">
	
	<% // this is the upload, it can be called anything else than upload
	  // it will automatically add the form ENCTYPE=multipart/form-data %>
	Upload a file: 
	<mak:input name="theFile" dataType="text" type="file" />
	<% // test of normal http parameters %>
	<input type=hidden name=context value="<%=context%>">
	<input type=hidden name=path value="<%=path%>">
	<input type=checkbox name=binary checked id=bin disabled><label for=bin>Binary</label>
	<input type=checkbox name=overwrite checked id=ovr disabled><label for=ovr>Overwrite</label>
	<input type=submit value=Upload>
</mak:form>
