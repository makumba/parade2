<html>
<head>
<title>Log view for row ${context}</title>
<link rel='StyleSheet' href='/style/log.css' type='text/css'>
</head>

<body>

<pre>
<#list entries as entry>
<strong>${entry.date}: user ${entry.user}@${entry.context} made an access to url: ${entry.url} with query string ${entry.queryString}</strong>
&nbsp;&nbsp;
<#list entry.logEntries as logentry>
${logentry.date} <span class="${logentry.level}">${logentry.message}</span>
</#list>

</#list>
</pre>

</body>
</html>