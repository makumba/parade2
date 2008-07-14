<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<mak:list from="ALE a" where="a.user = :user_login and a.focus > 20" orderBy="a.focus desc">
<mak:value expr="a.objectURL" printVar="objectURL"/>
<% String fileName = objectURL.substring(objectURL.lastIndexOf("/")+1); %>
<%=fileName %> (<mak:value expr="a.focus" />)<br>
</mak:list>