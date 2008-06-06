<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="/WEB-INF/lib/cewolf.tld" prefix="cewolf" %>
<%@page import="org.makumba.aether.percolation.ProgressionCurveDatasetProducer"%>

<%-- Makumba Generator - START OF  *** OBJECT ***  PAGE FOR OBJECT InitialPercolationRule --%>
<mak:response/>

<mak:object from="InitialPercolationRule initialPercolationRule" where="initialPercolationRule.id=:initialPercolationRule">
  <fieldset style="text-align:right;">
  <legend>InitialPercolationRule <i><mak:value expr="initialPercolationRule.objectType" /></i></legend
  <table>
  <%-- Makumba Generator - START OF NORMAL FIELDS --%>
    <tr>
      <th>objectType</th>
      <td><mak:value expr="initialPercolationRule.objectType"/></td>
    </tr>
    <tr>
      <th>action</th>
      <td><mak:value expr="initialPercolationRule.action"/></td>
    </tr>
    <tr>
      <th>userType</th>
      <td><mak:value expr="initialPercolationRule.userType"/></td>
    </tr>
    <tr>
      <th>initialLevel</th>
      <td><mak:value expr="initialPercolationRule.initialLevel"/></td>
    </tr>
    <tr>
      <th>percolationMode</th>
      <td><mak:value expr="initialPercolationRule.percolationMode"/></td>
    </tr>
    <tr>
      <th>active</th>
      <td><mak:value expr="initialPercolationRule.active"/></td>
    </tr>
    <tr>
      <th>relationQueries</th>
      <td><mak:list from="IN(initialPercolationRule.relationQueries) rq"><mak:value expr="rq.query"/></mak:list></td>
    </tr>
    
<mak:value expr="initialPercolationRule.focusProgressionCurve" printVar="focusCurve"/>
<mak:value expr="initialPercolationRule.nimbusProgressionCurve" printVar="nimbusCurve"/>

<%
int curveScale = ProgressionCurveDatasetProducer.DAY_IN_HOURS;
Object scaleParam = request.getParameter("scale");
if(scaleParam != null) {
	String scale = (String) scaleParam;
	if(scale.equals("day")) {
		curveScale = ProgressionCurveDatasetProducer.DAY_IN_HOURS;
	} else if(scale.equals("week")) {
		curveScale = ProgressionCurveDatasetProducer.WEEK_IN_HOURS;
	} else if(scale.equals("month")) {
		curveScale = ProgressionCurveDatasetProducer.MONTH_IN_HOURS;
	}
}
ProgressionCurveDatasetProducer focus = new ProgressionCurveDatasetProducer(focusCurve, curveScale);
ProgressionCurveDatasetProducer nimbus = new ProgressionCurveDatasetProducer(nimbusCurve, curveScale);
pageContext.setAttribute("focusCurveProvider", focus);
pageContext.setAttribute("nimbusCurveProvider", nimbus);
%>
<tr><td>Scale</td>
<td><form action="initialPercolationRuleView.jsp">
<select name="scale">
  <option value ="day">Day</option>
  <option value ="week">Week</option>
  <option value ="month">Month</option>
</select>
<input type="hidden" name="initialPercolationRule" value="<mak:value expr="initialPercolationRule.id"/>"/>
<input type="submit" value="Change"/>
</form></td>
</tr>
    <tr>
      <th>Focus progression curve</th>
      <td>
        <mak:value expr="initialPercolationRule.focusProgressionCurve"/><br><br>
        <cewolf:chart id="focusGraph" title="Focus progression curve" type="xy" xaxislabel="Time (hours)" yaxislabel="Energy">
          <cewolf:data>
            <cewolf:producer id="focusCurveProvider"/>
          </cewolf:data>
        </cewolf:chart>
        <cewolf:img chartid="focusGraph" renderer="/cewolf" width="400" height="300"/>
      </td>
    </tr>
    <tr>
      <th>Nimbus progression curve</th>
      <td>
        <mak:value expr="initialPercolationRule.nimbusProgressionCurve" /><br><br>
        <cewolf:chart id="nimbusGraph" title="Nimbus progression curve" type="xy" xaxislabel="Time (hours)" yaxislabel="Energy">
          <cewolf:data>
            <cewolf:producer id="nimbusCurveProvider"/>
          </cewolf:data>
        </cewolf:chart>
        <cewolf:img chartid="nimbusGraph" renderer="/cewolf" width="400" height="300"/>
      </td>
    </tr>

  </table>
</fieldset>
  <%-- Makumba Generator - END OF NORMAL FIELDS --%>

  <%-- Makumba Generator - START OF SETS --%>

  <%-- Makumba Generator - END OF SETS --%>

</table>
<a href="initialPercolationRuleList.jsp">Back to initialPercolationRule list</a>
</fieldset>
</mak:object>

<%-- Makumba Generator - END OF *** OBJECT ***  PAGE FOR OBJECT InitialPercolationRule --%>
