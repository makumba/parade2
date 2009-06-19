/* Update only when no checkboxes are selected */
function getMyWork() 
{
	var flag = false;
	var num_checkboxes = document.forms[0].elements.length;
	
	for (i = 0; i < num_checkboxes; i++) {		
		if (document.forms[0].elements[i].type == 'checkbox') {
			if (document.forms[0].elements[i].checked == true)
			{
				flag = true;
			}
		}
	}
	
	if (!flag) {
		new Ajax.Updater('mywork', '/aetherMyWork.jsp', {evalScripts: true});
	}
}

/* Update work of others every time, also when checkboxes are selected */
function getWorkOfOthers() 
{
    new Ajax.Updater('workofothers', '/aetherWorkOfOthers.jsp', {evalScripts: true});
}

new PeriodicalExecuter(getMyWork, 5);
new PeriodicalExecuter(getWorkOfOthers, 10);