<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>

<html>
<head><title>Edit user information of ${user_nickname}</title></head>
<body>



<h2>Hi ${user_name}!</h2>
Here you can edit your user information.<br><br>
<mak:object from="User u" where="u.login=:user_login">
<mak:form action="userView.jsp" method="post" message="Profile information saved">
<mak:editForm object="u">
<table>
<tr>
  <td>Name</td>
  <td><mak:input field="name"/></td>
</tr>
<tr>
  <td>Surname</td>
  <td><mak:input field="surname"/></td>
</tr>
<tr>
  <td>Nickname</td>
  <td><mak:input field="nickname"/></td>
</tr>
<tr>
  <td>E-mail</td>
  <td><mak:input field="email"/></td>
</tr>
<tr>
  <td>CVS user</td>
  <td><mak:input field="cvsuser"/></td>
</tr>
<mak:list from="Row r" where="r.user = u">
<mak:editForm object="r">
<tr>
  <td>Automatic CVS update of row <mak:value expr="r.rowname"/></td>
  <td><mak:input field="automaticCvsUpdate"/> (this means that if one file is commited by someone and you didn't touch it, ParaDe will CVS update it for you)</td>
</tr>
</mak:editForm>
</mak:list>
<tr>
  <td colspan="2"><input type="submit" value="Save">&nbsp; &nbsp; <input type="button" onClick="javascript:back();" value="Cancel"></td>
</tr>
</table>
</mak:editForm>
</mak:form>
</mak:object>


</body>
</html>