<HTML>
<HEAD>
<TITLE>Command view for ${rowName}</TITLE>
<link rel='StyleSheet' href='/style/parade.css' type='text/css'>
<link rel='StyleSheet' href='/style/header.css' type='text/css'>
<base target='command'>
</HEAD>

<BODY class='header'>

<img src='/images/win-x.gif' align=right alt='remove frames' border=0 hspace=1 vspace=1 onMouseDown="src='/images/win-x2.gif' onMouseUp=\"src='/images/win-x.gif'; top.location=top.directory.location;">
<table class='header'>

<form ACTION='/browse.jsp?' TARGET='_top' style='margin:0px;'>
<tr>
<td valign="top">
<a HREF='/' TARGET='_top' title='back to front page'>&lt;</a>
<select SIZE='1' NAME='context' onChange="javascript:form.submit();">
<#list rows as row>
<option value="${row}"<#if row = rowName> selected</#if>>${row}</option>
</#list>
</select><input TYPE='submit' VALUE='Go!'>
</td>
</form>
<td valign=top>[<a href='log?context=${rowName}' title='${rowName} log' target='directory'>log</a>] 
<a href='/logs/server-output.txt' title='Server log' target='directory'>all-log</a>-<a href='/logs' title='other logs' target='directory'>s</a> 
<a href='/tomcat-docs' title='Tomcat documentation' target='directory'>Tomcat</a> 
<a href='/makumba-docs' title='Makumba documentation' target='directory'>Makumba</a></td>

<td valign=top>
<script language='JavaScript'>
<!--
function icqNewWin() {
  var leftpos = (screen.availWidth - 200)-40;
  resiz = (navigator.appName=='Netscape') ? 0 : 1;
  window.open('http://lite.icq.com/icqlite/web/0,,,00.html', 'TOFI','width=177,height=446,top=40,left='+leftpos+',directories=no,location=no,menubar=no,scroll=no,status=no,titlebar=no,toolbar=no,resizable='+resiz+'');
  } //-->
</script>
<a href='#start ICQ Lite' target='header' onClick='javascript:icqNewWin();'><img src='/images/icq-online.gif' border=0 alt='Launch ICQ Lite'></a></td>
<td valign="top"><a href='ssh/ssh.jsp' target='command' title='Secure shell'>ssh</a></td>
<td valign="top">&nbsp; ant: <#list antTargets as target><a href="/Ant.do?context=${rowName}&path=&op=${target}">${target}</a><#if target_has_next>, </#if></#list></td>
<td valign="top">
<#if webapp.status = 2>
&nbsp; webapp:
<a href='/Webapp.do?context=${rowName}&path="+${webapp.path}&op=servletContextReload'>reload</a> 
<a href='/Webapp.do?context=${rowName}&path=${webapp.path}&op=servletContextStop'>stop</a>        
</#if>
<#if webapp.status = 1>
<a href='/Webapp.do?context=${rowName}&path=${webapp.path}&op=servletContextStart'>start</a>
</#if>
<#if webapp.status != 0>
<a href='/Webapp.do?context=${rowName}&path=${webapp.path}&op=servletContextRemove'>uninstall</a>
</#if>
<#if webapp.status = 0>
<a href='/Webapp.do?context=${rowName}&path=${webapp.path}&op=servletContextInstall'>install</a>
</#if>
</td>

</tr></table></body></html>