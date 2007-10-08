<HTML><HEAD><TITLE>Welcome to ParaDe</TITLE></HEAD><BODY><CENTER>
<link rel='StyleSheet' href='/style/rowstore.css' type='text/css'>
<BORDER class='rowstore'>

<#if opResult != "">
	<#if success>
	<div class='success'>${opResult}</div>
	<#else>
	<div class='failure'>${opResult}</div>
	</#if>
</#if>

<TABLE class='rowstore'>
<tr>
<#list headers as header>
<th>${header}</th>
</#list>
</tr>

<#list rows as row>
<tr class="<#if (row_index % 2) = 0>odd<#else>even</#if>">
<td align='center'><a href='/browse.jsp?context=${row.rowstore.rowname}'>${row.rowstore.rowname}</a> <#if row.rowstore.rowname == '(root)'><a href=''>(Surf)</a></#if><#if row.rowstore.rowname != '(root)'><a href='${row.rowstore.rowname}'>(Surf)</a></#if><br>
<font style="font-size:smaller;">${row.rowstore.rowpath}</font></td>
<td>${row.rowstore.rowdescription}</td>
<td>${row.cvs.user}, <b>${row.cvs.module}</b>, ${row.cvs.branch}</td>
<td>${row.ant.buildfile}<br>
<#list row.ant.targets as target>
<a href="/Ant.do?context=${row.rowstore.rowname}&path=&op=${target}">${target}</a>
<#if target_has_next>, </#if>
</#list>
</td>
<td>${row.webapp.webappPath}, <#if row.webapp.status == 0>not installed</#if><#if row.webapp.status == 1>stopped</#if><#if row.webapp.status == 2>installed</#if><br>
<#if row.webapp.status = 2>
<a href='/Webapp.do?context=${row.rowstore.rowname}&path="+${row.webapp.path}&op=servletContextReload'>reload</a> 
<a href='/Webapp.do?context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextStop'>stop</a>        
</#if>
<#if row.webapp.status = 1>
<a href='/Webapp.do?context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextStart'>start</a>
</#if>
<#if row.webapp.status != 0>
<a href='/Webapp.do?context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextRemove'>uninstall</a>
</#if>
<#if row.webapp.status = 0>
<a href='/Webapp.do?context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextInstall'>install</a>
</#if>
</td>
<td>${row.mak.version}</td>
</tr>
</#list>

</TABLE>
<BR><BR>
<div class='command'><a href='/Rows.do?op=paraderefresh'>Refresh ParaDe</a></div>

</CENTER></BODY></HTML>
