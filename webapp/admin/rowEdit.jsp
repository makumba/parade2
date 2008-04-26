<%@ taglib uri="http://www.makumba.org/presentation" prefix="mak"%>

<html>
<head>
<title>ParaDe administration interface: row configuration</title>
</head>
<body>

<mak:object from="parade.Row r" where="r = $row">
<mak:editForm object="r" action="rowList.jsp" method="post">
User: <mak:input field="user"/>
<input type="submit" value="Save">&nbsp;&nbsp;<input type="button" value="Back" onClick="javascript:back();">
</mak:editForm>
</mak:object>

</body>
</html>