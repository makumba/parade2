<html>
<head>
<title>Log view for row ${context}</title>
<link rel='StyleSheet' href='/style/log.css' type='text/css'>
<script type="text/javascript">
function pointToBottom(){
  window.location=window.location.href.replace( /(#.*)?$/,'')+'#bottomlink';
document.getElementById('myInput').value='';
}
</script>
<body onLoad="pointToBottom();">

<pre>
<#list entries as entry>
<#if entry.serverRestart><hr></#if>
${entry.date} <span class="${entry.level}">${entry.user}@${entry.context}:  ${entry.message}</span>
</#list>
</pre>
<a name="bottomlink">&nbsp;</a>
</body>
</html>