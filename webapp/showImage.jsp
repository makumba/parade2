<% /* $Id$ */ 
%><%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%><%@ page
	import="java.util.*, org.makumba.Text"%>
<%@page import="java.io.Writer"%>
<%@page import="java.io.InputStream"%>
<mak:object from="User u" where="u.id = :user">
	<mak:value expr="u.jpegPhoto" var="photo" />
	<mak:value expr="u.nickname" var="nickname" />
	<% 
	  Text image = new Text((byte[])photo);
	  response.setContentType("image/jpg");
      String title = "Image of "+nickname;
      response.setHeader("Content-Disposition", "inline; filename="+title.replace(' ','_').replace('.','_')+".jpg");
      response.setHeader("Cache-Control","no-cache");

      Writer w = response.getWriter();
      
      try {
    	InputStream s = image.toBinaryStream();
	    int c = 0;
    	while((c = s.read()) != -1) {
    	  w.write(c);
    	}
    	w.flush();
    	  
      } catch(java.io.IOException e1) {  
         // we ignore the user pressing Cancel or Stop 
      }

// make sure there is nothing after the end of the scriplet!

%>
</mak:object>