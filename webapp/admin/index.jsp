<html>
<head>
<title>ParaDe administration interface</title>
</head>
<body>

<% String result = "";
if((result = (String)request.getAttribute("result")) != null) {%>
<font color = "green">${result}</font>
<%}%>

<h1>ParaDe administration interface</h1>
<p>
<div style="color: red; font-size: large">Make sure you know
exactly what you are doing, because otherwise you may harm the system
and an army of mad developers will come kick your bottom.</div>
</p>

<h2>Row management</h2>
<p>Here you can manage things in relation to parade rows.</p>

<h3>Users and Rows</h3>
<p>Here you can configure who owns a row, and who does Unison (or synchronisation in general) on it (also temporarily!)</p>
<a href="rowList.jsp">Edit the row-user relations</a><br>

<h2>Cache management</h2>
<p>Here you can manage ParaDe's cache. For the moment you can't do
much, but this should change.</p>

<h3>Register new rows</h3>
<p>If you added a new row in rows.properties, you can register it
here.</p>
<form action="/Admin.do"><input type="hidden" name="op"
	value="newRow" /> <input type="hidden" name="display" value="index" />
<input type="submit" value="Register new rows!"><br>
<br>
</form>

<h3>Row refresh</h3>
<form action="/Admin.do"><input type="hidden" name="op"
	value="refreshRow" /> <input type="hidden" name="display"
	value="index" /> Refresh cache of row <input name="context"
	type="text"> <input type="submit" value="Do it!">
(enter the exact name of the row, e.g. manu-k).<br>
<br>
<div style="color: red; font-weight: bold">Warning: this takes
time. Be patient.</div>
</form>

<h3>Application cache refresh</h3>
<p>
Each application, meaning each CVS module that is used by one or more
rows of ParaDe, has a cache of all the files and their latest revision
in the repository. This is usually kept up-to-date by ParaDe's CVS hook,
and is refreshed at each ParaDe restart, but you may want to refresh it
for some other reason.
</p>
<form action="/Admin.do"><input type="hidden" name="op"
	value="refreshApplications" /> <input type="hidden" name="display" value="index" />
<input type="submit" value="Refresh Application cache"><br>
<br>
</form>

<h3>Directory refresh</h3>
Still to come.

<h2>Cache status</h2>
Still to come.

<h1>Aether administration</h1>

<h3>Global relation crawling</h3>
To make Aether crawl through all contexts, <a href="/Admin.do?op=crawlRows">click here</a>.

<h3>Global relation cache refresh</h3>
To make Aether forget about all the crawled files, <a href="/Admin.do?op=resetCrawlStatus">click here</a>.

<h3>Per row relation cache refresh</h3>
<form action="/Admin.do"><input type="hidden" name="op"
	value="resetRowCrawlStatus" /> <input type="hidden" name="display"
	value="index" /> Refresh crawl status of row <input name="context"
	type="text"> <input type="submit" value="Do it!">
(enter the exact name of the row, e.g. manu-k).<br>
<br>
</form>

</body>
</html>