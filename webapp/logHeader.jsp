<%-- ParaDe log header: provides options for filtering Log-s or ActionLog-s --%>
<%@page import="java.util.Calendar"%>
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<jsp:include page="/layout/header.jsp?class=header" />

<c:set var="year" value="${param.year}" />
<c:set var="month" value="${param.month}" />
<c:set var="day" value="${param.day}" />
<c:set var="filter" value="${param.filter}" />
<c:set var="logtype" value="${param.logtype}" />

<%
	Calendar now = Calendar.getInstance();
%>
<c:if test="${empty year || year == 'null'}">
	<c:set var="year"><%=Integer.valueOf(now.get(Calendar.YEAR))
							.toString()%></c:set>
</c:if>
<c:if test="${empty month || month == 'null'}">
	<c:set var="month"><%=Integer.valueOf(now.get(Calendar.MONTH))
							.toString()%></c:set>
</c:if>
<c:if test="${empty day || day == 'null'}">
	<c:set var="day"><%=Integer.valueOf(now.get(Calendar.DAY_OF_MONTH))
							.toString()%></c:set>
</c:if>
<c:if test="${empty filter || filter == 'null'}">
	<c:set var="filter">none</c:set>
</c:if>
<c:if test="${empty logtype || logtype == 'null'}">
	<c:set var="logtype">log</c:set>
</c:if>


<a href="/" target="_top" title="back to front page">&lt;</a>

<script language="JavaScript">
<!--
	var cal = new CalendarPopup();
	cal.setReturnFunction("setMultipleValues2");
	function setMultipleValues2(y, m, d) {
		document.forms[0].year.value = y;
		document.forms[0].month.value = LZ(m);
		document.forms[0].day.value = LZ(d);
	}
//-->
</script>

<form class="nobreak" target="logview"
	action="<c:choose><c:when test="${logtype == 'log'}">/logs?logtype=log&view=log</c:when><c:when test="${logtype == 'actionlog'}">/actionLogList.jsp?a=b</c:when></c:choose>&years=${year}&months=${month}&days=${day}&filter=${filter}">
Show logs since:
<input name="day" value="${day}" size="3" type="text"> /
<input name="month" value="${month}" size="3" type="text"> /
<input name="year" value="${year}" size="5" type="text">
<a href="#" onclick="cal.showCalendar('calanchor'); return false;" title="cal.showCalendar('calanchor'); return false;" name="calanchor" id="calanchor">select</a>
&nbsp;&nbsp;in context <select SIZE='1' NAME='context'>
<option value="all"<c:if test="${'all' == param.context}"> selected</c:if>>all</option>
<mak:list from="Row r" where="r.moduleRow = false" orderBy="r.rowname">
<c:set var="rowName"><mak:value expr="r.rowname"/></c:set>
<option value="${rowName}"<c:if test="${rowName == param.context}"> selected</c:if>><mak:value expr="r.rowname"/></option>
</mak:list>
</select>
&nbsp;&nbsp;
Quick filter: <select size="1" name="filter">
<option value="none"<c:if test="${filter == 'none'}"> selected</c:if>>-Pick one-</option>
<option value="hour"<c:if test="${filter == 'hour'}"> selected</c:if>>Last hour</option>
<option value="restart"<c:if test="${filter == 'restart'}"> selected</c:if>>Last server restart</option>
<option value="day"<c:if test="${filter == 'day'}"> selected</c:if>>Last day</option>
<option value="week"<c:if test="${filter == 'week'}"> selected</c:if>>Last week</option>
</select>

<input type="submit" value="Filter">
</form>

<jsp:include page="/layout/footer.jsp" />