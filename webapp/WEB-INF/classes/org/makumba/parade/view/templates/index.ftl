<CENTER>

<#if opResult != "">
  <#if displaySuccess>
	<div class='<#if success>success<#else>failure</#if>'>${opResult}</div><br/>
  <#else>
    <div class='result'>${opResult}</div><br/>
  </#if>
</#if>
<table width="100%" cellpadding="0" cellspacing="0" >
<tr>
<td align="left">
<a class="icon_list" title="All the logs" href="log.jsp?context=all">[Log]</a>&nbsp;&nbsp;
<a class="icon_project" title="All the action logs" href="actionLog.jsp?context=all">[Action log] (beta)</a>&nbsp;&nbsp;
<strong><a class="icon_members" title="People who were active in the 20 past minutes">Currently online:</a></strong> <#list onlineUsers as user><a href='userView.jsp?user=${user[0]}'>${user[1]}</a>&nbsp;&nbsp;</#list>
</td>
<td align="right">
<a class="icon_user_edit" title="See and modify your profile here" href="userView.jsp">My profile</a>&nbsp;&nbsp;
<a class="icon_bug" href="mailto:parade-developers@lists.sourceforge.net" title="Report a bug">Report a bug</a>
<a class="icon_logout" href="logout.jsp">Logout</a>&nbsp;&nbsp;
</td>
</tr>
</table>

<br>
Hi ${userNickName}! Have a nice time on ParaDe!</div><br><br>

<BORDER class='rowstore'>

<br>
<TABLE class='rowstore'>
<tr>
<#list headers as header>
<th class='rowstore'>${header}</th>
</#list>
</tr>

<#list rows as row>
<tr class="<#if !row.rowstore.watchedByJNotify && row.rowstore.rowname != '(root)'>notWatched<#else><#if (row_index % 2) = 0>odd<#else>even</#if></#if>">
<td align='center'>
<#if !row.rowstore.watchedByJNotify && row.rowstore.rowname != '(root)'><img src="/images/exclamation.gif">&nbsp;This row is not watched by JNotify and won't work properly! Please restart ParaDe and read the logs to get more information.<br><br></#if>
<a href='/browse.jsp?context=${row.rowstore.rowname}'>${row.rowstore.rowname}</a> <#if row.rowstore.rowname == '(root)'><a href=''>(Surf)</a></#if><#if row.rowstore.rowname != '(root)'><#if row.webapp.status == 2><a href='${row.rowstore.rowname}'>(Surf)</a><#else>(Surf)</#if></#if>&nbsp;<a href='log.jsp?context=${row.rowstore.rowname}' target='_new'>(log)</a><br>
<font style="font-size:smaller;">${row.rowstore.rowpath}</font></td>
<td>${row.rowstore.rowdescription}</td>
<td>${row.cvs.user}</td>
<td>${row.cvs.module}</td>
<td>${row.cvs.branch}</td>
<td>${row.ant.buildfile}<br>
<#list row.ant.targets as target>
<a href="/Ant.do?display=index&context=${row.rowstore.rowname}&path=&op=${target}">${target}</a>
<#if target_has_next>,</#if>
</#list>
</td>
<td>${row.webapp.webappPath}</td>
<td>
<#if row.webapp.status == 0><div class="notinstalled">not installed</div></#if>
<#if row.webapp.status != 0><div class="installed">installed</div></#if>
<#if row.webapp.status == 1><div class="stopped">stopped</div></#if>
<#if row.webapp.status == 2><div class="started">started</div></#if>

<#if row.webapp.status == 2>
<a href='/Webapp.do?display=index&context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextReload'>reload</a>
<a href='/Webapp.do?display=index&context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextStop'>stop</a>        
</#if>
<#if row.webapp.status == 1>
<a href='/Webapp.do?display=index&context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextStart'>start</a>
</#if>
<#if row.webapp.status != 0>
<a href='/Webapp.do?display=index&context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextRedeploy'>redeploy</a>  
<a href='/Webapp.do?display=index&context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextRemove'>uninstall</a>
</#if>
<#if row.webapp.status == 0>
<a href='/Webapp.do?display=index&context=${row.rowstore.rowname}&path=${row.webapp.path}&op=servletContextInstall'>install</a>
</#if>
</td>
<td <#if row.mak.versionError>class="error"</#if>>${row.mak.version}<br>
<font style="font-size:smaller;">${row.mak.database}</font>
</td>
</tr>
</#list>

</TABLE>
<BR><BR>
<!-- <div class='command'><a href='/Rows.do?op=paraderefresh'>Refresh ParaDe</a></div> -->
<!--
<script type="text/javascript" src="/scripts/tickertape/TickerTape.js?v101"></script>
<script type="text/javascript">var ticker = new TickerTape('/servlet/ticker', 'horizontalTickerTape', 3000, true);</script>
-->

</CENTER>

<br>
<a title="ParaDe TODO list" href="todo.jsp">ParaDe</a>
