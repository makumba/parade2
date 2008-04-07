<HTML><HEAD><TITLE>Command view for ${rowName}</TITLE>
<link rel="StyleSheet" href="/style/parade.css" type="text/css">
<link rel="StyleSheet" href="/style/command.css" type="text/css">
</HEAD>
<BODY class="command">
<form target="directory" action="/Command.do" method="POST">
<input type=hidden value="${rowName}" name=context>
<input type=hidden value="newFile" name="op">
Create new file: <input type="text" name="params">
<input type=hidden value="${path}" name="params">
<input type=submit value=Create>
<br><font style="font-size: smaller;">(in ${rowName}${path})</font>
</form>
</body>
</html>
