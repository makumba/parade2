<html>
<head>
<title>${fileName} - Code Press ParaDe editor</title>

<script src="/scripts/codepress/codepress.js" type="text/javascript"></script>

</head>
<body bgcolor="#dddddd" TOPMARGIN=0 LEFTMARGIN=0 RIGHTMARGIN=0 BOTTOMMARGIN=0 marginwidth=0 marginheight=0 STYLE="margin: 0px">

<form name="sourceEdit" method="post" action="/File.do?op=saveFile&context=${rowName}&path=${path}&file=${fileName}&editor=codepress" style="margin:0px;">

<input type="submit" name="Submit" value="(S)ave!" ACCESSKEY="S" onclick="myCpWindow.toggleEditor();">
<a href="browse.jsp?context=${rowName}" target="_top" title="${rowName}">${rowName}</a>:<a href="/servlet/browse?display=file&context=${rowName}&path=${path}">${path}</a>/<b>${fileName}</b>
| <a href="/File.do?op=editFile&context=${rowName}&path=${path}&file=${fileName}&editor=codepress" title="get the file from disk again, undo all changes since last save">Revert</a>
<br>

<div id="languages">
	<em>set language:</em> 
	<button onclick="myCpWindow.edit('myCpWindow','javascript')">JavaScript</button> 
	<button onclick="myCpWindow.edit('myCpWindow','java')">Java</button>
	<button onclick="myCpWindow.edit('myCpWindow','html')">HTML</button> 
	<button onclick="myCpWindow.edit('myCpWindow','css')">CSS</button>
</div>

<textarea id="myCpWindow" class="codepress java" name="source" style="width:100%;height:92%" cols="90" rows="23" wrap="virtual" STYLE="font-face:Lucida Console; font-size:8pt">${content}</textarea>


</form>

</body>
</html>