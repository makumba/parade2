<HTML><HEAD><TITLE>Command view for ${rowName}</TITLE>
<link rel="StyleSheet" href="/style/parade.css" type="text/css">
<link rel="StyleSheet" href="/style/command.css" type="text/css">
</HEAD><BODY class="command">
<form target="command" action="/Cvs.do" method="POST">
<input type=hidden value="${rowName}" name="context">
<input type=hidden value="commit" name="op">
<input type=hidden value="${path}" name="params">
<input type=hidden value="${fileAbsolutePath}" name="params">
Committing <strong>${fileName}</strong> with message:<br>
<textarea rows="3" cols="40" name="params"></textarea><br>
<input type=submit value=Commit>
</form>
</body></html>
