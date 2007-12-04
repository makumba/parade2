<html>
<head>
<title>Log menu for context ${context}</title>
<SCRIPT LANGUAGE="JavaScript" SRC="/scripts/CalendarPopup.js"></SCRIPT>
<link rel='StyleSheet' href='/style/log.css' type='text/css'>
<link rel='StyleSheet' href='/style/header.css' type='text/css'>
</head>

<body class="header">
<form target="logview" action="/logs?view=logs&context=${context}&years=${year}&months=${month}&days=${day}>
<script language="JavaScript">
<!--
var cal = new CalendarPopup();
cal.setReturnFunction("setMultipleValues2");
function setMultipleValues2(y,m,d) {
     document.forms[0].year.value=y;
     document.forms[0].month.value=LZ(m);
     document.forms[0].day.value=LZ(d);
     }
     -->
</script>
Show logs since:
<input name="day" value="${day}" size="3" type="text"> /
<input name="month" value="${month}" size="3" type="text"> /
<input name="year" value="${year}" size="5" type="text">
<a href="#" onclick="cal.showCalendar('calanchor'); return false;" title="cal.showCalendar('calanchor'); return false;" name="calanchor" id="calanchor">select</a>
&nbsp;&nbsp;in context <select SIZE='1' NAME='context'>
<#list rows as row>
<option value="${row}"<#if row = context> selected</#if>>${row}</option>
</#list>
</select>


<input type="submit" value="Show">
</form>

</body>
</html>