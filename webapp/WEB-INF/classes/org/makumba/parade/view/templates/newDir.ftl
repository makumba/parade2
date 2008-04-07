<HTML><HEAD><TITLE>Command view for ${rowName}</TITLE>
<link rel="StyleSheet" href="/style/parade.css" type="text/css">
<link rel="StyleSheet" href="/style/command.css" type="text/css">
</HEAD><BODY class="command">
<form action="/Command.do" target="directory" method="POST">
<input type=hidden value="${rowName}" name="context">
<input type=hidden value="newDir" name="op">
Create new directory: <input type="text" name="params">
<input type=hidden value="${path}" name="params">
<input type=submit value=Create>
<br><font style="font-size: smaller;">(in ${rowName}${path})</font>
</form>
</body></html>