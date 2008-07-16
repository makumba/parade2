<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<html>
<head>
<script src="${pageContext.request.contextPath}/scripts/prototype.js"></script>

<script type="text/javascript">
getActionEffect = function(objectURL, user, iteration) {
    var params = '?objectURL='+objectURL+'&user='+user+'&objectType=FILE&action=save';
    $('effects'+iteration).style.visibility='visible';
    new Ajax.Updater('effects'+iteration, '/aetherGetActionEffects.jsp', {parameters: params});
  }

</script>
</head>
<mak:list from="ALE a" where="a.user = :user_login and a.focus > 20" orderBy="a.focus desc">
<mak:value expr="a.objectURL" printVar="objectURL"/>
<% String fileName = objectURL.substring(objectURL.lastIndexOf("/")+1); %>
<strong><%=fileName %> (<mak:value expr="a.focus" />)</strong><br>
<a href="javascript:getActionEffect('<%=objectURL %>', '<%=request.getSession().getAttribute("user_login") %>', '${mak:count() }');">Whom do I affect if I change this file?</a>
<div id="effects${mak:count() }" style="visibility: hidden;">Computing...</div>
</mak:list>