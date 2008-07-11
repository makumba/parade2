<#if opResult != "">
	<#if success>
	<div class='success'>${opResult}</div>
	<#else>
	<div class='failure'>${opResult}</div>
	</#if>
</#if>

<h2 class="files">[<a href='javascript:ajaxpage("/servlet/browse?display=file&context=${rowName}","directory");'>${rowName}</a>]/<#list parentDirs as parentDir><a href='javascript:ajaxpage("/servlet/browse?display=file&context=${rowName}&path=${parentDir.path}","directory");'>${parentDir.directoryName}</a>/</#list><img src='/images/folder-open.gif'></h2>
<div class='pathOnDisk'>${pathOnDisk}</div>
<table class='files'>
<tr><th></th>
<th colspan='2'>
<a href='javascript:ajaxpage("/File.do?display=command&view=newDir&context=${rowName}&path=${path}","command");' title='Create a new directory'><img src='/images/newfolder.gif' align='right'></a>
<a href='javascript:ajaxpage("/uploadFile.jsp?context=${rowName}&path=${path}","command");' title='Upload a file'><img src='/images/uploadfile.gif' align='right'></a>
<a href='javascript:ajaxpage("/File.do?display=command&view=newFile&context=${rowName}&path=${path}","command");' title='Create a new file'><img src='/images/newfile.gif' align='right'></a>
<a href='javascript:ajaxpage("/servlet/browse?display=file&context=${rowName}&path=${path}&order=name","command");' title='Order by name'>Name</a>
</th>
<th><a href='javascript:ajaxpage("/servlet/browse?display=file&context=${rowName}&path=${path}&order=age","directory");' title='Order by age'>Age</a></th>
<th><a href='javascript:ajaxpage("/servlet/browse?display=file&context=${rowName}&path=${path}&order=size","directory");' title='Order by size'>Size</a></th>

<script language="JavaScript">
<!--
function deleteFile(path, name) {
  if(confirm('Are you sure you want to delete the file '+name+' ?')) {
	ajaxpage('/File.do?getPathFromSession=false&display=file&context=${rowName}&path=${pathEncoded}&op=deleteFile&params='+encodeURIComponent(name),'directory');
  }
}
-->
</script>
<th>CVS <a href='javascript:ajaxpage("/Cvs.do?op=check&context=${rowName}&params=${path}","command");' title='CVS check status'><img src='/images/cvs-query.gif' alt='CVS check status' border='0'></a>
<a href='javascript:ajaxpage("/Cvs.do?op=update&context=${rowName}&params=${path}","command");' title='CVS local update'><img src='/images/cvs-update.gif' alt='CVS local update' border='0'></a>
<a href='javascript:ajaxpage("/Cvs.do?op=rupdate&context=${rowName}&params=${path}","command");' title='CVS recursive update'><img src='/images/cvs-update.gif' alt='CVS recursive update' border='0'></a></th>
</tr>

<#list fileViews as file>
<tr class="<#if (file_index % 2) = 0>odd<#else>even</#if>">

<!-- FILE -->
<#if file.isDir>
<td><img src='/images/folder.gif'></td>
<td colspan='2'><a href='javascript:ajaxpage("/File.do?browse&display=file&context=${rowName}&path=${file.path}","directory");'>${file.name}</a></td>
<#else>
<td><img src='/images/${file.image}.gif'></td>
<td>
<#if file.isLinked>
<a href='${file.address}'>${file.name}</a>
<#else>${file.name}</#if>
</td>
<td align='right'>
<#if file.isOnDisk>
<a href='javascript:ajaxpage("/File.do?op=editFile&context=${rowName}&path=${path}&file=${file.nameEncoded}&editor=codepress","directory");'><img src='/images/edit.gif' alt='Edit ${file.name} with new editor'></a>
<a href='javascript:ajaxpage("/File.do?op=editFile&context=${rowName}&path=${path}&file=${file.nameEncoded}&editor=normal","directory");' title='Edit ${file.name} with old editor'>(E)</a>
&nbsp;&nbsp;
<a href="javascript:deleteFile('${path}','${file.name}')"><img src='/images/delete.gif' alt='Delete ${file.name}'></a>
</td>
</#if>
</#if>
<#if file.isDir><td></td><#else><td><a title='${file.dateLong}'>${file.dateNice}</a></td></#if>
<#if !file.isDir>
<#if !file.isEmpty><td><a title='${file.sizeLong} bytes'>${file.sizeNice}</a><#else><td><i>empty<i></td></#if>
<#else>
</td><td>
</#if>
</td>

<!-- CVS -->
<td>
<#if file.cvsIsNull>
<#if file.isDir>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=add","command");' title='CVS add dir'><img src='/images/cvs-add-dir.gif' alt='add'></a>
<#else>
<#if file.isConflictBackup><a title='Backup of your working file, can be deleted once you resolved its conflicts with CVS'>Conflict Backup</a>
<#else>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=add","command");' title='CVS add text file'><img src='/images/cvs-add.gif' alt='add'></a>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=addbin","command");' title='CVS add binary/image file'><img src='/images/cvs-add-binary.gif' alt='add binary'></a>
</#if>
</#if>
<#else>

<#if file.cvsNewerExists && file.cvsStatus != 5>
<#if file.cvsConflictOnUpdate>
<img src='/images/exclamation.gif' title='There is a newer file on the repository with revision ${file.cvsNewRevision}, and updating will provoke a CVS conflict.'>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=updatefile","command");' title='CVS update this file. This will lead to a conflict.'><img src='/images/cvs-update.gif' alt='CVS update this file. This will lead to a conflict.'></a>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=overridefile","command");' title='Override local changes and replace with file from repository'><img src='/images/cvs-override.gif' alt='Override local changes and replace with file from repository'></a>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=diff","command");' title='CVS diff (compare with repository version)'><img src='/images/cvs-diff.gif' alt='CVS diff'></a>
<#else>
<img src='/images/error.gif' title='There is a newer file on the repository with revision ${file.cvsNewRevision}. Consider updating this file.'>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=updatefile","command");' title='CVS update file from repository'><img src='/images/cvs-update.gif' alt='CVS update'></a>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=diff","command");' title='CVS diff (compare with repository version)'><img src='/images/cvs-diff.gif' alt='CVS diff'></a>
</#if>
<#else>
<#switch file.cvsStatus>

<#case 101>
<!-- IGNORED -->
<div class='cvs-ignored'>ignored</div>
<#break>

<!-- UNKNOWN -->
<#case -1>
???
<#break>
<!-- UP_TO_DATE -->

<#case 100>
<#if file.isDir>
<a href='${file.cvsWebLink}' title='CVS log'>(dir)</a>
<#else>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
</#if>
<#break>

<!-- LOCALLY_MODIFIED -->
<#case 1>
<#if file.isDir>
<a href='${file.cvsWebLink}' title='CVS log'>(dir)</a>
<#else>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
<a href='javascript:ajaxpage("/servlet/browse?context=${rowName}&path=${path}&file=${file.path}&display=command&view=commit","command");' title='CVS commit (place new file version on repository)'><img src='/images/cvs-committ.gif' alt='CVS commit'></a>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=diff","command");' title='CVS diff (compare with repository version)'><img src='/images/cvs-diff.gif' alt='CVS diff'></a>
</#if>
<#break>

<!-- NEEDS_CHECKOUT -->
<#case 2>
<#if file.isDir>
<a href='${file.cvsWebLink}' title='CVS log'>(dir)</a>
<#else>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=updatefile","command");' title='CVS update file from repository'><img src='/images/cvs-update.gif' alt='CVS checkout'></a>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=deletefile","command");' title='CVS delete file from repository'><img src='/images/cvs-remove.gif' alt='CVS remove'></a>
</#if>
<#break>

<!-- NEEDS_UPDATE -->
<#case 3>
<#if file.isDir>
<a href='${file.cvsWebLink}' title='CVS log'>(dir)</a>
<#else>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=updatefile","command");' title='CVS update file from repository'><img src='/images/cvs-update.gif' alt='CVS update'></a>
</#if>
<#break>

<!-- ADDED -->
<#case 4>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
<a href='javascript:ajaxpage("/servlet/browse?context=${rowName}&path=${path}&file=${file.path}&display=command&view=commit","command");' title='CVS commit to repository (will add file to repository)'><img src='/images/cvs-committ.gif' alt='CVS commit'></a>
<#break>

<!-- DELETED -->
<#case 5>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
<a href='javascript:ajaxpage("/servlet/browse?context=${rowName}&path=${path}&file=${file.path}&display=command&view=commit","command");' title='CVS commit to repository (will delete file from repository)'><img src='/images/cvs-committ.gif' alt='CVS commit'></a>
<#break>

<!-- CONFLICT -->
<#case 6>
<a href='${file.cvsWebLink}' title='CVS log'><b><font color='red'>Conflict</font></b></a>
<a href='javascript:ajaxpage("/servlet/browse?context=${rowName}&path=${path}&file=${file.path}&display=command&view=commit","command");' title='CVS commit to repository'><img src='/images/cvs-committ.gif' alt='CVS commit'></a>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=overridefile","command");' title='Override local changes and replace with file from repository'><img src='/images/cvs-override.gif' alt='Override local changes and replace with file from repository'></a>
<a href='javascript:ajaxpage("/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=diff","command");' title='CVS diff (compare with repository version)'><img src='/images/cvs-diff.gif' alt='CVS diff'></a>
<#break>

</#switch>
</#if>
</#if>

</td>
</tr>
        
</#list>

</TABLE>