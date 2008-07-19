<jsp:useBean id="workComputer" class="org.makumba.parade.aether.AetherBean" />
<jsp:setProperty property="user" name="workComputer" value="${sessionScope.user_login}"/>

<jsp:getProperty property="workOfOthers" name="workComputer"/>