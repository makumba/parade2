<form id="newFileForm" action="#" method="GET">
<input type=hidden value="${rowName}" name=context>
<input type=hidden value="newFile" name="op">
Create new file: <input type="text" name="params">
<input type=hidden value="${path}" name="params">

<button type="button" onclick="
$('progress').innerHTML = '<b>Creating...</b>';
new Ajax.Request('/Command.do', {
  method: 'get',
  parameters: $('newFileForm').serialize(true),
  onComplete: function(transport) {
    $('progress').innerHTML = 'Create'
    setAndEvalResponse('directory', transport)
  }
});"><span id="progress">Create</span></button>

<br><font style="font-size: smaller;">(in ${rowName}${path})</font>
</form>