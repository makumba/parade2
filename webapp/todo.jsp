<%@page import="java.io.BufferedReader"%>
<%@page import="org.makumba.parade.init.ParadeProperties"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileReader"%>
<pre>
<%
	BufferedReader r = new BufferedReader(
			new FileReader(new File(ParadeProperties.getParadeBase()
					+ File.separator + "TODO")));
	String line = "";
	while ((line = r.readLine()) != null) {
		out.println(line);
	}
	r.close();
%>
</pre>