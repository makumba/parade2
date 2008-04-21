<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>

<mak:list from="parade.User u">
User name: <mak:value expr="u.name"/><br>
</mak:list>