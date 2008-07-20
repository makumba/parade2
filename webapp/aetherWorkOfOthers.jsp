<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="org.makumba.parade.aether.ObjectTypes"%>
<%@page import="org.makumba.commons.ReadableFormatter"%>
<%@page import="java.util.Date"%>
<%@page import="org.makumba.parade.aether.ActionTypes"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Iterator"%>

<jsp:useBean id="aetherBean" class="org.makumba.parade.aether.AetherBean" />
<%
	Vector<Integer> maxFinder = new Vector<Integer>();
%>

<mak:list from="MatchedAetherEvent mae, PercolationStep ps"
  where="ps.matchedAetherEvent = mae and ps.objectURL in (select ale.objectURL from ALE ale where ale.user = :user_login and ale.nimbus > 20)"
  groupBy="mae.id" id="0">
  <mak:value expr="sum(ps.nimbus)" printVar="maxNimb" />
  <%
  	maxFinder.add(Integer.parseInt(maxNimb));
  %>
</mak:list>

<%
	int max = 0;
	Iterator<Integer> i = maxFinder.iterator();
	while (i.hasNext()) {
		int next = i.next();
		if (next > max) {
			max = next;
		}
		request.setAttribute("max", max);
	}
%>

<mak:list from="MatchedAetherEvent mae, PercolationStep ps "
  where="ps.matchedAetherEvent = mae and ps.objectURL in (select ale.objectURL from ALE ale where ale.user = :user_login and ale.nimbus > 20)"
  groupBy="mae.id" orderBy="sum(ps.nimbus) desc">
  <mak:value expr="mae.eventDate" var="eventDate" />
  <mak:value expr="sum(ps.nimbus)" var="sumNimbus" />
  <c:choose>
    <c:when test="${(sumNimbus / max) * 100 == 100}">
      <c:set var="objectWeight" value="large" />
    </c:when>
    <c:when test="${((sumNimbus / max) * 100) > 75 and ((nimbus / maxNimbus) * 100) < 100}">
      <c:set var="objectWeight" value="medium" />
    </c:when>
    <c:when test="${((sumNimbus / max) * 100) > 50 and ((nimbus / maxNimbus) * 100) < 75}">
      <c:set var="objectWeight" value="small" />
    </c:when>
    <c:when test="${((sumNimbus / max) * 100) > 25 and ((nimbus / maxNimbus) * 100 )< 50}">
      <c:set var="objectWeight" value="x-small" />
    </c:when>
    <c:when test="${((sumNimbus / max) * 100) >= 0 and ((nimbus / maxNimbus) * 100) < 25}">
      <c:set var="objectWeight" value="xx-small" />
    </c:when>

  </c:choose>

  <font style="font-size: ${objectWeight};"> <%=ReadableFormatter.readableAge(new Date().getTime()
								- ((Date) eventDate).getTime())%> ago <mak:object from="User u" where="u.login = mae.actor">
    <mak:value expr="u.nickname" printVar="actorNickname" />
    <c:if test="${actorNickname eq '' or empty actorNickname}">
      <c:set var="actorNickname">
        <mak:value expr="u.name" />
        <mak:value expr="u.surname" />
      </c:set>
    </c:if>
      ${actorNickname}
    </mak:object> <mak:value expr="mae.action" printVar="action" /><%=ActionTypes.getReadableAction(action)%> <mak:value
    expr="mae.objectURL" printVar="actionObject" /> <a target="directory"
    href="<%=aetherBean.getResourceLink(actionObject, false)%>"><%=ObjectTypes.objectNameFromURL(actionObject)%></a> <br>
  </font>
</mak:list>

