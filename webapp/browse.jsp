<%@ page buffer="64kb" %>

<%  String context = request.getParameterValues("context")[0];
  if(context == null)
      context = (String) request.getAttribute("context");
  String opResult = request.getParameter("opResult");
  if(opResult == null)
      opResult = (String) request.getAttribute("opResult");
  
    String path = null;
    String getPathFromSession = request.getParameter("getPathFromSession");
    if(getPathFromSession != null) {
        path = (String) request.getSession().getAttribute("path");
    } else {
        path = request.getParameter("path");
    }
    if (path == null)
        path = (String) request.getAttribute("path");
    if(path == null)
        path = "/";
%>

<jsp:include page="/layout/header.jsp" flush="false" />

<div id="browse-header">
  <%String header = "/servlet/browse?display=header&getPathFromSession=true&context=" + context; %>
  <jsp:include page="<%=header %>"/>
</div>
<div>
  <div id="tree">
    <% String tree = "/servlet/browse?display=tree&context=" + context; %>
    <jsp:include page="<%=tree %>" />
  </div>
  <div>
    <div id="directory">
      <% String directory = "/servlet/browse?display=file&getPathFromSession=true&context=" + context + "&opResult=" + opResult + "&path=" + path; %>
      <jsp:include page="<%=directory %>"/>
    </div>
    <div id="command">
      <script type="text/javascript">
      ajaxinclude('/servlet/browse?display=command&context=<%=context %>');
      </script>
    </div>
  </div>
</div>

<jsp:include page="/layout/footer.jsp" flush="false" />

<%--

<div id="browse-header">
  <script type="text/javascript">
  ajaxinclude('/servlet/browse?display=header&context=<%=context %>&getPathFromSession=true');
  </script>
</div>
<div>
  <div id="tree">
    <script type="text/javascript">
    ajaxinclude('/servlet/browse?display=tree&context=<%=context %>');
    </script>
  </div>
  <div>
    <div id="directory">
      <script type="text/javascript">
      ajaxinclude('/servlet/browse?display=file&context=<%=context %>&opResult=<%=opResult %>&path=<%=path %>&getPathFromSession=true');
      </script>
    </div>
    <div id="command">
      <script type="text/javascript">
      ajaxinclude('/servlet/browse?display=command&context=<%=context %>');
      </script>
    </div>
  </div>
</div>

--%>