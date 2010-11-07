<pre>
<#list entries as entry>
<#if entry.serverRestart><hr></#if>
${entry.date} <span class="${entry.level}">${entry.user}@${entry.context}:  ${entry.message}</span>
</#list>
</pre>
<a name="bottomlink">&nbsp;</a>
