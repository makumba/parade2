<html><head><title>${rowName} tree</title>
<link rel='StyleSheet' href='/style/parade.css' type='text/css'>
<link rel='StyleSheet' href='/style/tree.css' type='text/css'>
</head>

<body class='tree'>

<script src="/treeMenu/sniffer.js"></script>
<script src="/treeMenu/TreeMenu.js"></script>

<div id="menuLayer${rowName}"></div>

<script language="javascript" type="text/javascript">
objTreeMenu = new TreeMenu('menuLayer${rowName}', '/treeMenu/imagesCompact', 'objTreeMenu', 'directory');
objTreeMenu.n[0] = new TreeNode('<#if rowName = "">(root)<#else>${rowName}</#if>','folder.gif', '/File.do?display=file&context=${rowName}', false);
<#list branches as branch>
${branch.treeRow} = new TreeNode('${branch.fileName}', 'folder.gif', '/servlet/browse?display=file&context=${rowName}&path=${branch.filePath}', false);
</#list>
objTreeMenu.drawMenu()
objTreeMenu.resetBranches()
</script>
</body></html>