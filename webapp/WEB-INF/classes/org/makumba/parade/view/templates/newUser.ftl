<html>
<head>
  <title>ParaDe: new user</title>
</head>
<body>

<#if opResult != "">
	<div class='result'>${opResult}</div><br/>
</#if>

<h2>New user account creation</h2>
Hi ${niceUserName}! It looks like the system doesn't know you that well yet.<br>
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
  <td>E-mail address (preferrably Gmail if you have one)</td>
  <td><input type="text" name="email"></td>
</tr>
<tr>
  <td>BEST Private Area profile pointer</td>
  <td><input type="text" name="PAptr"></td>
</tr>
<tr><td colspan="2">&nbsp;</td></tr>
<tr><td colspan="2" align="centre"><input type="submit" value="Create my account!"></td></tr>
</table>
</form>
</body>

</html>