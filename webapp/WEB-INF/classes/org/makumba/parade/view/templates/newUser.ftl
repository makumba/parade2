<html>
<head>
  <title>ParaDe: new user</title>
</head>
<body>

<#if opResult != "">
	<div class='result'>${opResult}</div><br/>
</#if>

<h2>User account creation</h2>
Hi ${niceUserName}! It looks like ParaDe doesn't know you that well yet.<br>
Please provide some more information about yourself!<br>
<font style="font-size: smaller;">(note that this information will be connected to your login <i>${login}</i>)</font><br>
<br>
<form action="/User.do?op=newUser" method="POST">
<table>
<tr>
  <td>Name</td>
  <td><input type="text" name="name"></td>
</tr>
<tr>
  <td>Surname</td>
  <td><input type="text" name="surname"></td>
</tr>
<tr>
  <td>Nickname</td>
  <td><input type="text" name="nickname"></td>
</tr>
<tr>
  <td>E-mail address</td>
  <td><input type="text" name="email"> (preferrably Gmail if you have one - maybe we'll have a ParaDe Gtalk bot someday)</td>
</tr>
<tr><td colspan="2">&nbsp;</td></tr>
<tr><td colspan="2" align="centre"><input type="submit" value="Create my account!"></td></tr>
</table>
</form>
</body>

</html>