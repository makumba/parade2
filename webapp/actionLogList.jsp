<%@page import="java.util.Calendar"%>
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<jsp:include page="/layout/header.jsp?class=log"/>

<c:set var="year" value="${param.year}" />
<c:set var="month" value="${param.month}" />
<c:set var="day" value="${param.day}" />
<c:set var="filter" value="${param.filter}" />
<% Calendar now = Calendar.getInstance(); %>
<c:if test="${empty year || year == 'null'}"><c:set var="year"><%=now.get(Calendar.YEAR)%></c:set></c:if>
<c:if test="${empty month || month == 'null'}"><c:set var="month"><%=now.get(Calendar.MONTH)%></c:set></c:if>
<c:if test="${empty day || day == 'null'}"><c:set var="day"><%=now.get(Calendar.DAY_OF_MONTH)%></c:set></c:if>
<c:if test="${empty filter || filter == 'null'}"><c:set var="filter">none</c:set></c:if>

<c:set var="alWhere">day(al.logDate) >= :day and month(al.logDate) >= :month and year(al.logDate) >= :year</c:set>
<c:if test="${not empty param.context}">
  <c:if test="${param.context != 'all'}">
    <c:set var="alWhere">${alWhere} and al.context = :context</c:set>
  </c:if>
</c:if>


<mak:list from="ActionLog al" where="#{alWhere}" orderBy="al.logDate desc" limit="500">
<mak:value expr="al.paradecontext" printVar="paradeContext"/>
<mak:value expr="al.logDate" format="yyyy-MM-dd HH:mm" /> <mak:value expr="al.user"/>@<c:choose><c:when test="${empty paradeContext}"><mak:value expr="al.context"/></c:when><c:otherwise>${paradeContext}</c:otherwise></c:choose> : <mak:value expr="al.action"/> <mak:value expr="al.objectType"/> <mak:value expr="al.file"/><br>
<div style="padding-left: 20px;">
<mak:list from="Log l" where="l.actionLog.id = al.id" orderBy="l.logDate desc">
<div class="<mak:value expr="l.level"/>"><mak:value expr="l.message"/></div>
</mak:list>
</div>
</mak:list>

<jsp:include page="/layout/footer.jsp"/>
