<link rel="StyleSheet" href="/style/parade.css" type="text/css">
<link rel="StyleSheet" href="/style/command.css" type="text/css">
</HEAD><BODY class="command">
<form id="newDirForm" action="#" method="GET">
<input type=hidden value="${rowName}" name="context">
<input type=hidden value="newDir" name="op">
Create new directory: <input type="text" name="params">
<input type=hidden value="${path}" name="params">
<button type="button" onclick="
$('progress').innerHTML = '<b>Creating...</b>';
new Ajax.Request('/Command.do', {
  method: 'get',
  parameters: $('newDirForm').serialize(true),
  onComplete: function(transport) {
    $('progress').innerHTML = 'Create'
    setAndEvalResponse('directory', transport)
  }
});"><span id="progress">Create</span></button>

<br><font style="font-size: smaller;">(in ${rowName}${path})</font>
</form>