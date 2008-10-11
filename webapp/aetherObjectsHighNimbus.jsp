<%-- Aether high nimbus objects: displays the objects of high nimbus --%>
<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="org.makumba.parade.aether.ObjectTypes"%>
<%@page import="org.makumba.commons.ReadableFormatter"%>
<%@page import="java.util.Date"%>
<%@page import="org.makumba.parade.aether.ActionTypes"%>

<mak:list from="ALE ale" id="0"><mak:value expr="max(ale.nimbus)" var="maxNimbus"/></mak:list>

<mak:list from="ALE ale" where="ale.user = :user_login and ale.nimbus > 20" id="1"
  orderBy="ale.nimbus desc" limit="10">
  <mak:value expr="ale.objectURL" printVar="objectURL" />
  <mak:value expr="ale.nimbus" var="nimbus" />


<c:choose>
  <c:when test="${(nimbus / maxNimbus) * 100 == 100}"><c:set var="objectWeight" value="large"/></c:when>
  <c:when test="${(nimbus / maxNimbus) * 100 > 75 and (nimbus / maxNimbus) * 100 < 100}"><c:set var="objectWeight" value="medium"/></c:when>
  <c:when test="${(nimbus / maxNimbus) * 100 > 50 and (nimbus / maxNimbus) * 100 < 75}"><c:set var="objectWeight" value="small"/></c:when>
  <c:when test="${(nimbus / maxNimbus) * 100 > 25 and (nimbus / maxNimbus) * 100 < 50}"><c:set var="objectWeight" value="x-small"/></c:when>

</c:choose>

  <strong><font style="font-size: ${objectWeight};"><a title="<mak:value expr="ale.objectURL" />"><%=ObjectTypes.objectNameFromURL(objectURL)%></a></strong> (<%=ObjectTypes.%>)<br>

  <mak:list from="MatchedAetherEvent mae, PercolationStep ps "
    where="ps.matchedAetherEvent = mae AND ps.objectURL = ale.objectURL" groupBy="ale.id, mae.id"
    orderBy="sum(ps.nimbus) asc">
    <mak:value expr="mae.eventDate" var="eventDate" />
    <%=ReadableFormatter.readableAge(new Date().getTime() - ((Date) eventDate).getTime())%> ago
    <mak:object from="User u" where="u.login = mae.actor">
      <mak:value expr="u.nickname" printVar="actorNickname" />
      <c:if test="${actorNickname eq '' or empty actorNickname}"><c:set var="actorNickname"><mak:value expr="u.name" /> <mak:value expr="u.surname" /></c:set></c:if>
      ${actorNickname}
    </mak:object>
    <mak:value expr="mae.action" printVar="action"/><%=ActionTypes.getReadableAction(action) %> <mak:value expr="mae.objectURL" printVar="actionObject"/><%=ObjectTypes.objectNameFromURL(actionObject) %>
    <br>
  </mak:list>
</font>

</mak:list>