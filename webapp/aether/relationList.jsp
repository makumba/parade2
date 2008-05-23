<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<h1>Relations</h1>

<table border="0">
<mak:list from="org.makumba.devel.relations.Relation r">
<tr><td><mak:value expr="r.fromURL"/></td><td><mak:value expr="r.type"/></td><td><mak:value expr="r.toURL"/></td></tr>
</mak:list>
</table>