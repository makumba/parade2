<table border="0"><tr><td>
<form style="display: inline;" target="command" action="/Cvs.do" method="GET">
<input type=hidden value="${rowName}" name="context">
<input type=hidden value="commit" name="op">
<input type=hidden value="${path}" name="params">
<input type=hidden value="${file}" name="params">
Committing <strong>${fileName}</strong> with message:<br>
<textarea rows="3" cols="40" name="params"></textarea><br>
<input type=submit value=Commit>
</form>
</td><td>&nbsp;</td>
<td>
<b>Some guidelines for the commit message:</b><br>
<ul>
<li>if committing from context that is not yours, make sure to include your name</li>
<li>make sure there are no line breaks on the commit message</li>
<li>make sure that the commit message is meaningful</li>
</ul>
</td></tr></table>
