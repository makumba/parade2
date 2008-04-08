<% 
session.removeAttribute("org.makumba.parade.userObject");
session.removeAttribute("org.makumba.parade.user");
session.invalidate();
%>
<html>
<head><title>ParaDe logout</title></head>
<body>
<div align="centre">You are now logged out. Of course you can <a href="index.jsp">log in again</a></div>
</body>
</html>