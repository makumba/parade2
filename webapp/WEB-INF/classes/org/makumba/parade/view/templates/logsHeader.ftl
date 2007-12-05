<html>
<head>
<title>Log menu for context ${context}</title>
<SCRIPT LANGUAGE="JavaScript" SRC="/scripts/CalendarPopup.js"></SCRIPT>
<link rel='StyleSheet' href='/style/log.css' type='text/css'>
<link rel='StyleSheet' href='/style/header.css' type='text/css'>
</head>

<body class="header">
<form class="nobreak" target="logview" action="/logs?view=logs&context=${context}&years=${year}&months=${month}&days=${day}&filter=${filter}>
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
&nbsp;&nbsp;
Quick filter: <select size="1" name="filter">
<option value="none"<#if filter = 'none'> selected</#if>>-Pick one-</option>
<option value="hour"<#if filter = 'hour'> selected</#if>>Last hour</option>
<option value="restart"<#if filter = 'restart'> selected</#if>>Last server restart</option>
<option value="day"<#if filter = 'day'> selected</#if>>Last day</option>
<option value="week"<#if filter = 'week'> selected</#if>>Last week</option>
</select>

<input type="submit" value="Filter">
</form>

</body>
</html>