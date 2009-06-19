/* Highlight Row */
function highlight(id, currentClass, checkboxId) {
	var checkbox_element = document.getElementById(checkboxId);
	var row_element = document.getElementById(id);

	row_element.className = checkbox_element.checked ? 'table_row_selected' : currentClass;
}

/* Check All */
function check_all() {
	var num_checkboxes = document.forms[0].elements.length;

	for (i = 0; i < num_checkboxes; i++) {
		
		if (document.forms[0].elements[i].type == 'checkbox') {
			document.forms[0].elements[i].checked = true;
		}
	}
	highlight_all(num_checkboxes);
}

/* Uncheck All */
function uncheck_all() {
	var num_checkboxes = document.forms[0].elements.length;

	for (i = 0; i < num_checkboxes; i++) {
		if (document.forms[0].elements[i].type == 'checkbox') {
			document.forms[0].elements[i].checked = false;
		}
	}
	remove_highlight_for_all(num_checkboxes);
}

/* Highlight All Rows (this happens when 'check_all' is triggered */
function highlight_all(num) {
	for (i = 1; i <= num; i++) {
		document.getElementById('tr' + i).className = 'table_row_selected';
	}
}

/* Remove highlight for all Rows (this happens when 'uncheck_all' is triggered */
function remove_highlight_for_all(num) {
	for (i = 1; i <= num; i++) {
		var initial_class = (i % 2) ? 'table_row_2' : 'table_row_1';
		document.getElementById('tr' + i).className = initial_class;
	}
}