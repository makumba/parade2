<html>
<head>
	<title>ParaDe administration interface</title>
</head>
<body>

<h1>ParaDe administration interface</h1>
<p><div style="color: red; font-size: large">Make sure you know exactly what you are doing, because otherwise you may harm the system and an army of mad developers will come kick your bottom.</div></p>

<h2>Cache management</h2>
<p>Here you can manage ParaDe's cache. For the moment you can't do much, but this should change.</p>

<h3>Register new rows</h3>
<p>If you added a new row in rows.properties, you can register it here.<br>
<form action="/Rows.do">
<input type="hidden" name="op" value="newRow" />
<input type="hidden" name="display" value="index" />
<input type="submit" value="Register new rows!"><br><br>
</form>

<h3>Row refresh</h3>
<form action="/Rows.do">
<input type="hidden" name="op" value="row" />
<input type="hidden" name="display" value="index" />
Refresh cache of row <input name="context" type="text"> <input type="submit" value="Do it!">
(enter the exact name of the row, e.g. manu-k).<br><br>
<div style="color: red; font-weight: bold">Warning: this takes time. Be patient.</div>
</form>

<h3>Directory refresh</h3>
Still to come.

<h2>Cache status</h2>
Still to come.
</body>
</html>