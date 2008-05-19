<%@page import="org.makumba.parade.aether.ObjectTypes"%>
<%@page import="java.util.Set"%>
<%@page import="org.makumba.aether.UserTypes"%>
<%@page import="org.makumba.parade.aether.ActionTypes"%>

<table>
  <tr>
  <td><strong>ParaDe object types</strong></td>
  <td><% Set<String> types = ObjectTypes.getObjectTypes();
  for(String t : types) {
	  out.print(t + "&nbsp;");
  }
  %></td></tr>
  <tr>
  <td><strong>ParaDe user types</strong></td>
  <td><% Set<String> utypes = UserTypes.getUserTypes();
  for(String t : utypes) {
	  out.print(t + "&nbsp;");
  }
  %></td>
  </tr>
  <tr>
  <td><strong>ParaDe action types</strong></td>
  <td><% Set<String> atypes = ActionTypes.getActions();
  for(String t : atypes) {
	  out.print(t + "&nbsp;");
  }
  %></td>
  </tr>
  </table>