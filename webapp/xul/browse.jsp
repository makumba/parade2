<%-- $Id$ 
--%><%@ include file="xulHeader.jspf" %>

<window id="browser" title="... browser"
        xmlns:html="http://www.w3.org/1999/xhtml"
        xmlns="http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul">
<toolbox>
  <menubar id="main-menubar">
  <%--toolbar id="toolbar"--%>
    <toolbarbutton label="&lt;"/>
  <menulist label="Bus">
  <menupopup>
    <menuitem label="Car"/>
    <menuitem label="Taxi"/>
    <menuitem label="Bus" selected="true"/>
    <menuitem label="Train"/>
  </menupopup>
  </menulist>

    <toolbarbutton label="Go"/>

    <%--menu id="file-menu" label="File">
      <menupopup id="file-popup">
        <menuitem label="New"/>
        <menuitem label="Open"/>
        <menuitem label="Save"/>
        <menuseparator/>
        <menuitem label="Exit"/>
      </menupopup>
    </menu>
    <menu id="edit-menu" label="Edit">
      <menupopup id="edit-popup">
        <menuitem label="Undo"/>
        <menuitem label="Redo"/>
      </menupopup>
    </menu--%>
    <menu id="row-menu" label="Row">
      <menupopup id="row-popup">
        <menuitem label="Switch to..."/>
        <menuseparator/>
        <menuitem label="Properties"/>
      </menupopup>
    </menu>
    <menu id="tools-menu" label="Tools">
      <menupopup id="tools-popup">
        <menuitem label="SSH"/>
        <menuitem label="Chat"/>
        <menuitem label="ICQ"/>
      </menupopup>
    </menu>
    <menu id="ant-menu" label="Ant">
      <menupopup id="ant-popup">
        <menuitem label="compile"/>
        <menuitem label="clean"/>
        <menuitem label="doc"/>
        <menuitem label="getMakumba"/>
      </menupopup>
    </menu>
    <menu id="webapp-menu" label="WebApp">
      <menupopup id="webapp-popup">
        <menuitem label="Start"/>
        <menuitem label="Reload"/>
        <menuitem label="Stop"/>
        <menuitem label="Uninstall"/>
      </menupopup>
    </menu>
    <spacer flex="1"/>
    <menu id="help-menu" label="Help" accesskey="h">
      <menupopup id="help-popup">
        <menuitem label="System info"/>
        <menuitem label="About ParaDe"/>
      </menupopup>
    </menu>
  </menubar>
  <%--/toolbar--%>

</toolbox>        
<hbox flex="1">
<tree datasource="treeRdf.jsp" ref="http://parade.sf.net/tree" width="200">

  <treecols>
    <treecol id="name" label="Folder name" flex="1"/>
    <treecol id="size" label="Size"/>
  </treecols>

  <template>

    <rule>
      <treechildren>
       <treeitem uri="rdf:*">
         <treerow>
           <treecell label="rdf:http://parade.sf.net/tree#name"/>
           <treecell label="rdf:http://parade.sf.net/tree#size"/>
         </treerow>
       </treeitem>
      </treechildren>
    </rule>
      
  </template>
</tree>
  <splitter collapse="before" resizeafter="flex">
    <grippy/>
  </splitter>
  <vbox flex="1">
	  <browser id="directory"  src="files.jsp?context=test2-k" flex="1" />
	  <splitter collapse="before" resizeafter="flex">
	    <grippy/>
	  </splitter>
	  <iframe id="command" src="command.jsp" height="100" />
  </vbox>
</hbox>

</window>
