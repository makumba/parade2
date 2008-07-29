<form target="directory" action="/Command.do" method="GET">
<input type=hidden value="${rowName}" name=context>
<input type=hidden value="newFile" name="op">
Create new file: <input type="text" name="params">
<input type=hidden value="${path}" name="params">
<input type=submit value=Create>
<br><font style="font-size: smaller;">(in ${rowName}/${path})</font>
</form>