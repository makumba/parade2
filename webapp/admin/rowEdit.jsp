<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>

<html>
<head>
<title>ParaDe administration interface: row configuration</title>
</head>
<body>

<mak:object from="Row r" where="r = :row">
<h2>Edit row <mak:value expr="r.rowname"/></h2>

<mak:editForm object="r" action="rowList.jsp" method="post">
User: <mak:input field="user"/>
<input type="submit" value="Save">&nbsp;&nbsp;<input type="button" value="Back" onClick="javascript:back();">
</mak:editForm>
</mak:object>

</body>
</html>