<%-- Aether view: fetches "my work" and "work of others" view using an AJAX callback --%>
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<html>
<head><title>Aether awareness information</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/layout/style/bottom.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/prototype.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/rowSelection.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax.js"></script>
</head>

<body>
<table width="100%" border="1" cellspacing="0" cellpadding="0" class="table_border">
 <tr>
  <td width="35%" valign="top" class="table_header">
     <p class="text_header">My work</p>
  </td>
  <td width="65%" valign="top" class="table_header">
    <p class="text_header">Work of others</p>
  </td>
 </tr>
<tr>
  <td valign="top" class="table_inner">
     <div id="mywork" >
		<jsp:include page="aetherMyWork.jsp" flush="false" />
	</div>
  </td>
  <td valign="top" class="table_inner">
    <div id="workofothers" >
		<jsp:include page="aetherWorkOfOthers.jsp" flush="false" />
	</div>
  </td>
 </tr>
</table>

</body>
</html>