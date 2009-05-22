<%-- Aether view: fetches "my work" and "work of others" view using an AJAX callback --%>
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<html>
<head><title>Aether awareness information</title>

<script src="${pageContext.request.contextPath}/scripts/prototype.js"></script>

<script type="text/javascript">

getWorkOfOthers = function() {
    new Ajax.Updater('workofothers', '/aetherWorkOfOthers.jsp', {evalScripts: true});
}

getMyWork = function() {
	var c_value = 1;
	for (var i=0; i < document.fileform.file.length && c_value!=0; i++)
	   {
	   if (document.fileform.file[i].checked)
	      {
	      c_value = 0;
	      }
	   }
	 if(c_value)
	 {
		 new Ajax.Updater('mywork', '/aetherMyWork.jsp', {evalScripts: true});
	 }		
}

new PeriodicalExecuter(getMyWork, 3);
new PeriodicalExecuter(getWorkOfOthers, 8);

</script>
</head>

<body>
<h2>My work</h2>
<div id="mywork" >
<jsp:include page="aetherMyWork.jsp" flush="false" />
</div>

<h2>Work of others</h2>
<div id="workofothers" >
<jsp:include page="aetherWorkOfOthers.jsp" flush="false" />
</div>

</body>
</html>