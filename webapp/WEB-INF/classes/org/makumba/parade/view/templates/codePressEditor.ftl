<body bgcolor="#dddddd" TOPMARGIN=0 LEFTMARGIN=0 RIGHTMARGIN=0 BOTTOMMARGIN=0 marginwidth=0 marginheight=0 STYLE="margin: 0px">

<form name="sourceEdit" method="post" action="#" style="margin:0px;">

<button type="button" onclick="
$('progress').innerHTML = '<b>(S)aving...</b>';
new Ajax.Request('/File.do', {
  method: 'get',
  parameters: 'source='+$('myCpWindow').getValue()+'&op=saveFile&path=${path}&context=${rowName}&file=${fileName}&editor=codepress',
  onComplete: function(transport) {
    $('progress').innerHTML = '(S)ave!';
  }
});"><span id="progress">(S)ave!</span></button>
<a href="browse.jsp?context=${rowName}" title="${rowName}">${rowName}</a>:<a href="javascript:ajaxpage('/servlet/browse?display=file&context=${rowName}&path=${path}','directory');">${path}</a>/<b>${fileName}</b>
| <a href="javascript:ajaxpage('/File.do?op=editFile&context=${rowName}&path=${path}&file=${fileName}&editor=codepress','directory');" title="get the file from disk again, undo all changes since last save">Revert</a>
<br>

<div id="languages">
	<em>set language:</em> 
	<button onclick="myCpWindow.edit('myCpWindow','javascript')">JavaScript</button> 
	<button onclick="myCpWindow.edit('myCpWindow','java')">Java</button>
	<button onclick="myCpWindow.edit('myCpWindow','html')">HTML</button> 
	<button onclick="myCpWindow.edit('myCpWindow','css')">CSS</button>
</div>

<textarea name="source" id="myCpWindow" class="codepress java" style="width:100%;height:92%" cols="90" rows="23" wrap="virtual" STYLE="font-face:Lucida Console; font-size:8pt">${content}</textarea>

</form>