<img src='/images/win-x.gif' align=right alt='remove frames' border=0 hspace=1 vspace=1 onMouseDown="src='/images/win-x2.gif' onMouseUp=\"src='/images/win-x.gif'; top.location=top.directory.location;">
<table class='header'>

<form ACTION='/browse.jsp?getPathFromSession=true' TARGET='_top' style='margin:0px;'>
<tr>
<td valign="top">
<a HREF='/' TARGET='_top' title='back to front page'>&lt;</a>
<select SIZE='1' NAME='context' onChange="javascript:form.submit();">
<#list rows as row>
<option value="${row}"<#if row = rowName> selected</#if>>${row}</option>
</#list>
</select>
<input TYPE='submit' VALUE='Go!'>
</td>
</form>
<td valign=top>[<a href='/log.jsp?context=${rowName}' title='${rowName} log' target='_new'>log</a>] 
<a href='/log.jsp?context=all' title='Server log' target='directory'>all-log</a>-<a href='/log.jsp?context=all' title='other logs' target='directory'>s</a> 
<a href='/tomcat-docs' title='Tomcat documentation' target='directory'>Tomcat</a> 
<a href='http://www.makumba.org' title='Makumba documentation' target='directory'>Makumba</a></td>

<td valign=top>
<script language='JavaScript'>
<!--
function icqNewWin() {
  var leftpos = (screen.availWidth - 200)-40;
  resiz = (navigator.appName=='Netscape') ? 0 : 1;
  window.open('http://lite.icq.com/icqlite/web/0,,,00.html', 'TOFI','width=177,height=446,top=40,left='+leftpos+',directories=no,location=no,menubar=no,scroll=no,status=no,titlebar=no,toolbar=no,resizable='+resiz+'');
  } //-->
</script>
<td valign="top">&nbsp; ant: <#list antTargets as target><a href="/Ant.do?getPathFromSession=true&display=command&context=${rowName}&path=&op=${target}">${target}</a><#if target_has_next>, </#if></#list></td>
<td valign="top">
<#if webapp.status = 2>
&nbsp; webapp:
<a href='/Webapp.do?display=command&context=${rowName}&path=${webapp.path}&op=servletContextReload&getPathFromSession=true'>reload</a> 
<a href='/Webapp.do?display=command&context=${rowName}&path=${webapp.path}&op=servletContextStop&getPathFromSession=true'">stop</a>        
</#if>
<#if webapp.status = 1>
<a href='/Webapp.do?display=command&context=${rowName}&path=${webapp.path}&op=servletContextStart&getPathFromSession=true'>start</a>
</#if>
<#if webapp.status != 0>
<a href='/Webapp.do?display=command&context=${rowName}&path=${webapp.path}&op=servletContextRemove&getPathFromSession=true'>uninstall</a>
</#if>
<#if webapp.status = 0>
<a href='/Webapp.do?display=command&context=${rowName}&path=${webapp.path}&op=servletContextInstall&getPathFromSession=true'>install</a>
</#if>
<a href="/systemInfo.jsp" target="directory">System</a>
</td>

</tr></table>