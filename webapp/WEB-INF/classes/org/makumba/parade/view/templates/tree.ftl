<div id="menuLayer${rowName}"></div>

<script language="javascript" type="text/javascript">
objTreeMenu = new TreeMenu('menuLayer${rowName}', '/scripts/treeMenu/imagesCompact', 'objTreeMenu', '');
<#list branches as branch>
${branch.treeRow} = new TreeNode('${branch.fileName}', 'folder.gif', 'javascript:ajaxpage(\'/servlet/browse?display=file&context=${rowName}&path=${branch.filePath}\',\'directory\');', false);
</#list>
objTreeMenu.drawMenu()
objTreeMenu.resetBranches()
</script>
