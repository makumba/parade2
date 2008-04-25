<% /* $Id$ */ 
%><%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" 
%><%@ page import="java.util.*, org.makumba.Text" 
%><%@page import="java.io.File"%>
<%@page import="java.io.FileWriter"%>
<mak:object from="parade.User u" where="u.id = :user"><mak:value expr="u.jpegPhoto" var="photo"/><mak:value expr="u.nickname" var="nickname"/><% 
	  Text image = (Text) photo;
      response.setContentType("image/jpg");
      String title = "Image of "+nickname;
      response.setHeader("Content-Disposition", "inline; filename="+title.replace(' ','_').replace('.','_')+".jpg");
      response.setHeader("Cache-Control","no-cache");
      java.io.OutputStream os = response.getOutputStream();
      
      
      try{ 
         ( image).writeTo( os ); 
      } catch(java.io.IOException e1) {  
         // we ignore the user pressing Cancel or Stop 
      }

// make sure there is nothing after the end of the scriplet!

%></mak:object>