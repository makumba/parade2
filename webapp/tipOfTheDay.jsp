<table width="100%">
<tr><td>
<font size="+1"><b>Did you know...</b></font><br>
</td><td align="right">
<a href="ajaxpage('/tipOfTheDay.jsp','command');">more wisdom</a>
</td>
<tr><td colspan=2>
<img src="/images/litebulb.gif" align="left">
<%
java.util.Random rnd=new java.util.Random(); 
switch(rnd.nextInt(18)) {
case 0: { 
    %>You can edit any non-binary file in the built-in online editor by clicking <img src="/images/edit.gif"> icon next to it.<% break; }
case 1: { 
    %>You can delete any file by clicking <img src="/images/delete.gif"> icon next to it. Of course you are asked whether you're sure before anything happens.<% break; }
case 2: { 
    %>Clicking on the version number takes you to the complete history log of that particular file, showing you all previous versions.<% break; }
case 3: { 
    %>You can easily start the tracker on any file or folder by clicking the "track" part of the "not tracked" message.<% break; }
case 4: { 
    %>You can start your ICQ Lite by clicking on the <img src="/images/icq-online.gif" alt="ICQ"> icon on top of your screen.<% break; }
case 5: { 
    %>You can log in to the <%=request.getServerName()%> by clicking on the "ssh login" link on top of your screen.<% break; }
case 6: { 
    %>You can see server logs by clicking "server output" on top of your screen.<% break; }
case 7: { 
    %>You can chat with and share a whiteboard with other developers by clicking <img src="/images/chat.gif"> icon on top of your screen.<% break; }
case 8: { 
    %>You can easily switch to other contexts by choosing it in the drop-down and pressing "Go!" button on top-left of your screen.<% break; }
case 9: { 
    %>You are using <%=System.getProperty("java.runtime.name")%> from <%=System.getProperty("java.vm.vendor")%>, version <%=System.getProperty("java.vm.version")%>. Find out more by clicking <a href="/systemInfo.jsp" target="directory">System Info</a> on top of your screen.<% break; }
case 10: { 
    %>You can resize frames by dragging their borders.<% break; }
case 11: { 
    %>You can extend the functionality of parade to your needs.<% break; }
case 12: { 
    %><a href="http://www.best.eu.org" target="_new">BEST</a> members really can drink Palinka.<% break; }
case 13: { 
    %><a href="http://www.makumba.org" target="_new">Makumba</a> is another great tool made by the same people.<% break; }
case 14: { 
    %>You should commit your changes to CVS as soon as they are stable, so other developers can take advantage of your goodies. Never commit a file that doesn't even compile :)<% break; }
case 15: { 
    %>You should update your working tree before starting hacking, so you get all the goodies hacked by other people merged into your sources.<% break; }
case 16: { 
    %>When you create a new file you should add it to the CVS, then commit it once it becomes stable.<% break; }
case 17: { 
    %>Amongst real files you see also <i>ghost</i> files and folders that are not existing yet. They are just tracked or in CVS so far, but not physically existing in your checkout.<% break; }
case 18: { 
    %>You can see the changes in each file by clicking "diff" next to it.<% break; }
case 19: {
	%>Some <a href="http://www.best.eu.org" target="_new">BEST</a> members can even drink pepper Palinka<% break;
}
case 20: {
	%>You can create a new folder by clicking on the "New folder" icon in the browser view<% break;
}
case 21: {
	%>You can track a file by clicking on the Track link next to the file name<% break;
}
case 99: { 
    %>A new idea.<% break; }
} %>

</td></tr>
</table>