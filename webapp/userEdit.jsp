<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>

<html>
<head><title>Edit user information of ${user_nickname}</title></head>
<body>



<h2>Hi ${user.name}!</h2>
Here you can edit your user information.<br><br>
<mak:object from="parade.User u" where="u.login=:user_login">
<mak:editForm object="u" action="userView.jsp" method="post" message="Profile information saved">
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
  <td colspan="2"><input type="submit" value="Save">&nbsp; &nbsp; <input type="button" onClick="javascript:back();" value="Cancel"></td>
</tr>
</table>
</mak:editForm>
</mak:object>


</body>
</html>