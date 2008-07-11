<%
String context=null;
Object ctxValues = request.getParameterValues("context");
if(ctxValues != null)
  context = (String)(((Object[])ctxValues))[0];
if(context == null)
      context = "all";
%>

<jsp:include page="/layout/header.jsp" flush="false" />

<div class="log-header">
  <% String header =  "/logHeader.jspf?logtype=actionlog&context=" + context + "&year=" + request.getParameter("year") + "&month=" + request.getParameter("month") + "&day=" + request.getParameter("day"); %>
  <jsp:include page="<%=header %>" />
</div>
<div id="logview" class="log-view">
  <% String view = "/actionLogList.jsp?context=" + context + "&year=" + request.getParameter("year") + "&month=" + request.getParameter("month") + "&day=" + request.getParameter("day"); %>
  <jsp:include page="<%=view %>" />
</div>

<jsp:include page="/layout/footer.jsp" flush="false" />