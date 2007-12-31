<html>
<head>
<title>${fileName} - ParaDe editor</title>

<script language="JavaScript">
<!--
function insert() {
strSelection = document.selection.createRange().text 
if (strSelection == "") { 
 	document.sourceEdit.source.selection.createRange().text =  strSelection + document.makHelper;
	return false; 
} 
else document.selection.createRange().text = "<b>" + strSelection 
+ "</b>" 
return;

}

// Javascript code for automatically resizing textarea
// Written by David W. Jeske and contributed to the public domain.

function onResize() {
  resizeTA(document.sourceEdit.source);
}

function resizeTA(TA) {
  var winW, winH;
  var usingIE = 0;

  // these paramaters have to match the font you specify with your
  // style tag on the textarea.
  var fontMetricWidth = 7;
  var fontMetricHeight = 14;

  // you don't want this smaller than 1,1
  var minWidthInCols = 20;
  var minHeightInRows = 7;

  // offset fudge factors. 
  // Making these bigger makes the textarea smaller.
  var leftOffsetFudge = 40;
  var topOffsetFudge = 20;


  if (parseInt(navigator.appVersion)>3) {
    if (navigator.appName=="Netscape") {
      winW = window.innerWidth;
      winH = window.innerHeight; 
    }
    if (navigator.appName.indexOf("Microsoft")!=-1) {
      winW = document.body.offsetWidth;
      winH = document.body.offsetHeight;
      usingIE = 1;
    }
  }

  if (! usingIE ) {
    return; // this javascript below does not work for netscape
  }

  // this code computes the upper-left corner offset
  // by walking all the elements in the html page  
  toffset = 0;
  loffset = 0;
  offsetobj = TA;
  while (offsetobj) {
    toffset += offsetobj.offsetTop + offsetobj.clientTop;
    loffset += offsetobj.offsetLeft + offsetobj.clientLeft;
    offsetobj = offsetobj.offsetParent;
  }

  // compute and set the width
  var overhead = loffset + leftOffsetFudge;
  var ta_width = ((winW - overhead))  / fontMetricWidth;
  if (ta_width < minWidthInCols) {
    ta_width = minWidthInCols;
  }
  TA.cols = ta_width;


  // compute and set the height
  var overhead = toffset + topOffsetFudge;
  var ta_height = (winH - overhead) / fontMetricHeight;
  if (ta_height < minHeightInRows) {
    ta_height = minHeightInRows;
  }
  TA.rows = ta_height;
}

function onLoad() {
  onResize();
  document.sourceEdit.source.focus();
  document.sourceEdit.pagestatus.value='Loaded.';
  document.sourceEdit.Submit.disabled=true;
}

// http://www.webreference.com/dhtml/diner/beforeunload/bunload4.html
function unloadMess(){
    mess = "You have unsaved changes."
    return mess;
}

function setBunload(on){
    window.onbeforeunload = (on) ? unloadMess : null;
}

// to be called when the content of the big textarea changes
function setModified(){
    document.sourceEdit.pagestatus.value='MODIFIED';
    document.sourceEdit.Submit.disabled=false;
    setBunload(true);
}

//-->
</script>

</head>
<body bgcolor="#dddddd" TOPMARGIN=0 LEFTMARGIN=0 RIGHTMARGIN=0 BOTTOMMARGIN=0 marginwidth=0 marginheight=0 STYLE="margin: 0px" onload="javascript:onLoad();" onresize="javascript:onResize();">
<form name="sourceEdit" method="post" action="/File.do?op=editFile&context=${rowName}&path=${path}&file=${fileName}&editor=old" style="margin:0px;">

<input type="submit" name="Submit" value="(S)ave!" ACCESSKEY="S" disabled onclick="javascript:setBunload(false);">
<a href="browse.jsp?context=${rowName}" target="_top" title="${rowName}">${rowName}</a>:<a href="/servlet/browse?display=file&context=${rowName}&path=${path}">${path}</a>/<b>${fileName}</b>
| <a href="/File.do?op=editFile&context=${rowName}&path=${path}&file=${fileName}" title="get the file from disk again, undo all changes since last save">Revert</a> 
| <input type="text" value="Loading..." name="pagestatus" disabled size="10" style="border:0px; background-color:#dddddd; font-color:red;">
<br>

<textarea name="source" style="width:100%;height:92%" cols="90" rows="23" wrap="virtual" onKeypress="setModified()" STYLE="font-face:Lucida Console; font-size:8pt">${content}</textarea>

</form>
</body>
</html>