<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<html>
<head><title>Aether awareness information</title>

<script src="${pageContext.request.contextPath}/scripts/prototype.js"></script>

<script type="text/javascript">

getMyWork = function() {
  new Ajax.Updater('mywork', '/aetherMyWork.jsp', {evalScripts: true});
}
getWorkOfOthers = function() {
    new Ajax.Updater('workofothers', '/aetherWorkOfOthers.jsp', {evalScripts: true});
}

new PeriodicalExecuter(getMyWork, 10);
new PeriodicalExecuter(getWorkOfOthers, 10);

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