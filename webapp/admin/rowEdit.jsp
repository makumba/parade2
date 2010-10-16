<%@ taglib uri="http://www.makumba.org/view-hql" prefix="mak"%>
<jsp:include page="/layout/header.jsp?pageTitle=Edit row" />

<mak:object from="Row r" where="r = :row">
	<h2>Edit row <mak:value expr="r.rowname" /></h2>

	<mak:editForm object="r" action="rowList.jsp" method="post">
		<table>
			<tr>
				<td><strong>Row owner</strong></td>
				<td><mak:input field="user">
					<mak:list from="User u" orderBy="u.name, u.surname" id="1">
						<mak:option value="u.id">
							<mak:value expr="u.name" />
							<mak:value expr="u.surname" /> (<mak:value expr="u.nickname" />) </mak:option>
					</mak:list>
				</mak:input></td>
			</tr>
			<tr>
				<td><strong>Who does Unison (or any other external
				synchronization) on this row at the moment</strong></td>
				<td><mak:input field="externalUser">
					<mak:list from="User u" orderBy="u.name, u.surname" id="2">
						<mak:option value="u.id">
							<mak:value expr="u.name" />
							<mak:value expr="u.surname" /> (<mak:value expr="u.nickname" />) </mak:option>
					</mak:list>
				</mak:input></td>
			</tr>
			<tr>
				<td colspan="2" align="middle"><input type="submit"
					value="Save">&nbsp;&nbsp;<input type="button" value="Back"
					onClick="javascript:back();"></td>
			</tr>
		</table>
	</mak:editForm>
</mak:object>

<jsp:include page="/layout/footer.jsp" />