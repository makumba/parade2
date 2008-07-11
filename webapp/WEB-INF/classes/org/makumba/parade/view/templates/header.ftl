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
<a href='javascript:ajaxpage("/log.jsp?context=all","directory");' title='Server log'>all logs</a> 
<a href='javascript:ajaxpage("/tomcat-docs","directory");' title='Tomcat documentation'>Tomcat</a> 
<a href='javascript:ajaxpage("http://www.makumba.org","directory");' title='Makumba documentation'>Makumba</a></td>

<td valign=top>
<td valign="top">&nbsp; ant: <#list antTargets as target><a href="/Ant.do?getPathFromSession=true&display=command&context=${rowName}&path=&op=${target}">${target}</a><#if target_has_next>, </#if></#list></td>
<td valign="top">
<#if webapp.status = 2>
&nbsp; webapp:
<a href='javascript:ajaxpage("/Webapp.do?display=command&context=${rowName}&path=${webapp.path}&op=servletContextReload&getPathFromSession=true","command");'>reload</a> 
<a href='javascript:ajaxpage("/Webapp.do?display=command&context=${rowName}&path=${webapp.path}&op=servletContextStop&getPathFromSession=true","command");'>stop</a>        
</#if>
<#if webapp.status = 1>
<a href='javascript:ajaxpage("/Webapp.do?display=command&context=${rowName}&path=${webapp.path}&op=servletContextStart&getPathFromSession=true","command");'>start</a>
</#if>
<#if webapp.status != 0>
<a href='javascript:ajaxpage("/Webapp.do?display=index&context=${rowName}&path=${webapp.path}&op=servletContextRedeploy'>redeploy</a>  
<a href='javascript:ajaxpage("/Webapp.do?display=command&context=${rowName}&path=${webapp.path}&op=servletContextRemove&getPathFromSession=true","command");'>uninstall</a>
</#if>
<#if webapp.status = 0>
<a href='javascript:ajaxpage("/Webapp.do?display=command&context=${rowName}&path=${webapp.path}&op=servletContextInstall&getPathFromSession=true","command");'>install</a>
</#if>
<a href="javascript:ajaxpage('/systemInfo.jsp','directory');">System</a>
</td>

</tr></table>