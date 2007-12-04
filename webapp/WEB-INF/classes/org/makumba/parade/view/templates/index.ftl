<HTML><HEAD><TITLE>Welcome to ParaDe</TITLE></HEAD><BODY><CENTER>
<link rel='StyleSheet' href='/style/rowstore.css' type='text/css'>
<BORDER class='rowstore'>

<#if opResult != "">
	<div class='result'>${opResult}</div><br/>
</#if>

<TABLE class='rowstore'>
<tr>
<#list headers as header>
<th>${header}</th>
</#list>
</tr>

<#list rows as row>
<tr class="<#if (row_index % 2) = 0>odd<#else>even</#if>">
<td align='center'><a href='/browse.jsp?context=${row.rowstore.rowname}'>${row.rowstore.rowname}</a> <#if row.rowstore.rowname == '(root)'><a href=''>(Surf)</a></#if><#if row.rowstore.rowname != '(root)'><#if row.webapp.status == 2><a href='${row.rowstore.rowname}'>(Surf)</a><#else>(Surf)</#if></#if><br>
<font style="font-size:smaller;">${row.rowstore.rowpath}</font></td>
<td>${row.rowstore.rowdescription}</td>
<td>${row.cvs.user}</td>
<td>${row.cvs.module}</td>
<td>${row.cvs.branch}</td>
<td>${row.ant.buildfile}<br>
<#list row.ant.targets as target>
<a href="/Ant.do?display=index&context=${row.rowstore.rowname}&path=&op=${target}">${target}</a>
<#if target_has_next>, </#if>
</#list>
</td>
<td>${row.webapp.webappPath}</td>
<td>
<#if row.webapp.status == 0><div class="notinstalled">not installed</div></#if>
<#if row.webapp.status != 0><div class="installed">installed</div></#if>
<#if row.webapp.status == 1><div class="stopped">stopped</div></#if>
<#if row.webapp.status == 2><div class="started">started</div></#if>

<#if row.webapp.status == 2>
<a href='/Webapp.do?display=index&context=${row.rowstore.rowname}&path="+${row.webapp.path}&op=servletContextReload'>reload</a> 
<a href='/Webapp.do?display=index&context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextStop'>stop</a>        
</#if>
<#if row.webapp.status == 1>
<a href='/Webapp.do?display=index&context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextStart'>start</a>
</#if>
<#if row.webapp.status != 0>
<a href='/Webapp.do?display=index&context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextRemove'>uninstall</a>
</#if>
<#if row.webapp.status == 0>
<a href='/Webapp.do?display=index&context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextInstall'>install</a>
</#if>
</td>
<td>${row.mak.version}<br>
<font style="font-size:smaller;">${row.mak.database}</font>
</td>
</tr>
</#list>

</TABLE>
<BR><BR>
<!-- <div class='command'><a href='/Rows.do?op=paraderefresh'>Refresh ParaDe</a></div> -->

</CENTER></BODY></HTML>
