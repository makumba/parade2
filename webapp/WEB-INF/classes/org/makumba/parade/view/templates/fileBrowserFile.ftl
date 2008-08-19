<!-- FILE -->
<#if file.isDir>
<td><img src='/images/folder.gif'></td>
<td colspan='2'><a href='/File.do?browse&display=file&context=${rowName}&path=${file.path}'>${file.name}</a></td>
<#else>
<td><img src='/images/${file.image}.gif'></td>
<td>
<#if file.isLinked>
<a href='${file.address}'>${file.name}</a>
<#else>${file.name}</#if>
</td>
<td align='right'>
<#if file.isOnDisk>
<a href='/codePressEditor.jsp?context=${rowName}&path=${path}&file=${file.nameEncoded}'><img src='/images/edit.gif' alt='Edit ${file.name} with javascript editor'></a>
<a href='/simpleFileEditor.jsp?context=${rowName}&path=${path}&file=${file.nameEncoded}' title='Edit ${file.name} with old editor'>(E)</a>
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
