<html>
<head>
<title>Log view for row ${context}</title>
<link rel='StyleSheet' href='/style/log.css' type='text/css'>
</head>

<body>

<pre>
<#list entries as entry>
${entry.date} <#if entry.serverRestart><hr></#if><span class="${entry.level}">${entry.user}@${entry.context}:  ${entry.message}</span>
</#list>
</pre>

</body>
</html>