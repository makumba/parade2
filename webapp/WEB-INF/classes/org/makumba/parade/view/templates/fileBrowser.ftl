<#if opResult != "">
	<#if success>
	<div class='success'>${opResult}</div>
	<#else>
	<div class='failure'>${opResult}</div>
	</#if>
</#if>

<h2 class="files">[<a href='/servlet/browse?display=file&context=${rowName}'>${rowName}</a>]/<#list parentDirs as parentDir><a href='/servlet/browse?display=file&context=${rowName}&path=${parentDir.path}'>${parentDir.directoryName}</a>/</#list><img src='/images/folder-open.gif'></h2>
<div class='pathOnDisk'>${pathOnDisk}</div>
<table class='files'>
<tr><th></th>
<th colspan='2'>
<a href='/commandView/newDir.jsp?context=${rowName}&path=${path}' target='command' title='Create a new directory'><img src='/images/newfolder.gif' align='right'></a>
<a href='/commandView/uploadFile.jsp?context=${rowName}&path=${path}' target='command' title='Upload a file'><img src='/images/uploadfile.gif' align='right'></a>
<a href='/commandView/newFile.jsp?context=${rowName}&path=${path}' target='command' title='Create a new file'><img src='/images/newfile.gif' align='right'></a>
<a href='/servlet/browse?display=file&context=${rowName}&path=${path}&order=name' title='Order by name'>Name</a>
</th>
<th><a href='/servlet/browse?display=file&context=${rowName}&path=${path}&order=age' title='Order by age'>Age</a></th>
<th><a href='/servlet/browse?display=file&context=${rowName}&path=${path}&order=size' title='Order by size'>Size</a></th>

<script language="JavaScript">
<!--
function deleteFile(path, name) {
if(confirm('Are you sure you want to delete the file '+name+' ?'))
{
	url='/File.do?display=file&context=${rowName}&path=${pathEncoded}&op=deleteFile&params='+encodeURIComponent(name);
	location.href=url;
}
}
-->
</script>
<th>CVS <a href='/Cvs.do?op=check&context=${rowName}&params=${path}' target='command' title='CVS check status'><img src='/images/cvs-query.gif' alt='CVS check status' border='0'></a>
<a href='/Cvs.do?op=update&context=${rowName}&params=${path}' target='command' title='CVS local update'><img src='/images/cvs-update.gif' alt='CVS local update' border='0'></a>
<a href='/Cvs.do?op=rupdate&context=${rowName}&params=${path}' target='command' title='CVS recursive update'><img src='/images/cvs-update.gif' alt='CVS recursive update' border='0'></a></th>
</tr>

<#list fileViews as file>
<tr class="<#if (file_index % 2) = 0>odd<#else>even</#if>">

<#include "fileBrowserFile.ftl">
<#include "fileBrowserCVS.ftl" >

</tr>
        
</#list>

</TABLE>