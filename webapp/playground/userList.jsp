<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>

<mak:list from="parade.User u">
User name: <mak:value expr="u.name"/><br>
User surname: <mak:value expr="u.surname"/><br>
User nickname: <mak:value expr="u.nickname"/><br>
User email: <mak:value expr="u.email"/><br>
</mak:list>