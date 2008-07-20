<!-- CVS -->
<td>
<#if file.cvsIsNull>
<#if file.isDir>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=add' title='CVS add dir'><img src='/images/cvs-add-dir.gif' alt='add'></a>
<#else>
<#if file.isConflictBackup><a title='Backup of your working file, can be deleted once you resolved its conflicts with CVS'>Conflict Backup</a>
<#else>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=add' title='CVS add text file'><img src='/images/cvs-add.gif' alt='add'></a>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=addbin' title='CVS add binary/image file'><img src='/images/cvs-add-binary.gif' alt='add binary'></a>
</#if>
</#if>
<#else>

<#if file.cvsNewerExists && file.cvsStatus != 5>
<#if file.cvsConflictOnUpdate>
<img src='/images/exclamation.gif' title='There is a newer file on the repository with revision ${file.cvsNewRevision}, and updating will provoke a CVS conflict.'>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=updatefile' title='CVS update this file. This will lead to a conflict.'><img src='/images/cvs-update.gif' alt='CVS update this file. This will lead to a conflict.'></a>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=overridefile' title='Override local changes and replace with file from repository'><img src='/images/cvs-override.gif' alt='Override local changes and replace with file from repository'></a>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=diff' title='CVS diff (compare with repository version)'><img src='/images/cvs-diff.gif' alt='CVS diff'></a>
<#else>
<img src='/images/error.gif' title='There is a newer file on the repository with revision ${file.cvsNewRevision}. Consider updating this file.'>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=updatefile' title='CVS update file from repository'><img src='/images/cvs-update.gif' alt='CVS update'></a>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=diff' title='CVS diff (compare with repository version)'><img src='/images/cvs-diff.gif' alt='CVS diff'></a>
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
<a target='command' href='/servlet/browse?context=${rowName}&path=${path}&file=${file.fileURIEncoded}&display=command&view=commit' title='CVS commit (place new file version on repository)'><img src='/images/cvs-committ.gif' alt='CVS commit'></a>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=diff' title='CVS diff (compare with repository version)'><img src='/images/cvs-diff.gif' alt='CVS diff'></a>
</#if>
<#break>

<!-- NEEDS_CHECKOUT -->
<#case 2>
<#if file.isDir>
<a href='${file.cvsWebLink}' title='CVS log'>(dir)</a>
<#else>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=updatefile' title='CVS update file from repository'><img src='/images/cvs-update.gif' alt='CVS checkout'></a>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=deletefile' title='CVS delete file from repository'><img src='/images/cvs-remove.gif' alt='CVS remove'></a>
</#if>
<#break>

<!-- NEEDS_UPDATE -->
<#case 3>
<#if file.isDir>
<a href='${file.cvsWebLink}' title='CVS log'>(dir)</a>
<#else>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=updatefile' title='CVS update file from repository'><img src='/images/cvs-update.gif' alt='CVS update'></a>
</#if>
<#break>

<!-- ADDED -->
<#case 4>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
<a target='command' href='/servlet/browse?context=${rowName}&path=${path}&file=${file.path}&display=command&view=commit' title='CVS commit to repository (will add file to repository)'><img src='/images/cvs-committ.gif' alt='CVS commit'></a>
<#break>

<!-- DELETED -->
<#case 5>
<a href='${file.cvsWebLink}' title='CVS log'>${file.cvsRevision}</a>
<a target='command' href='/servlet/browse?context=${rowName}&path=${path}&file=${file.path}&display=command&view=commit' title='CVS commit to repository (will delete file from repository)'><img src='/images/cvs-committ.gif' alt='CVS commit'></a>
<#break>

<!-- CONFLICT -->
<#case 6>
<a href='${file.cvsWebLink}' title='CVS log'><b><font color='red'>Conflict</font></b></a>
<a target='command' href='/servlet/browse?context=${rowName}&path=${path}&file=${file.path}&display=command&view=commit' title='CVS commit to repository'><img src='/images/cvs-committ.gif' alt='CVS commit'></a>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=overridefile' title='Override local changes and replace with file from repository'><img src='/images/cvs-override.gif' alt='Override local changes and replace with file from repository'></a>
<a target='command' href='/Cvs.do?context=${rowName}&path=${path}&file=${file.path}&op=diff' title='CVS diff (compare with repository version)'><img src='/images/cvs-diff.gif' alt='CVS diff'></a>
<#break>

</#switch>
</#if>
</#if>
</td>
