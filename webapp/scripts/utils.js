/*******************************************************************************
 * Dynamic Ajax Content- © Dynamic Drive DHTML code library
 * (www.dynamicdrive.com) This notice MUST stay intact for legal use Visit
 * Dynamic Drive at http://www.dynamicdrive.com/ for full source code
 ******************************************************************************/

var bustcachevar = 1
// bust potential caching of external pages after initial request? (1=yes, 0=no)
var loadedobjects = ""
var rootdomain = "http://" + window.location.hostname
var bustcacheparameter = ""

function ajaxpage(url, containerid) {
	var page_request = false
	if (window.XMLHttpRequest) // if Mozilla, Safari etc
		page_request = new XMLHttpRequest()
	else if (window.ActiveXObject) { // if IE
		try {
			page_request = new ActiveXObject("Msxml2.XMLHTTP")
		} catch (e) {
			try {
				page_request = new ActiveXObject("Microsoft.XMLHTTP")
			} catch (e) {
			}
		}
	} else
		return false
	page_request.onreadystatechange = function() {
		loadpage(page_request, containerid)
	}
	if (bustcachevar) // if bust caching of external page
		bustcacheparameter = (url.indexOf("?") != -1) ? "&"
				+ new Date().getTime() : "?" + new Date().getTime()
	page_request.open('GET', url + bustcacheparameter, true)
	page_request.send(null)
}

function loadpage(page_request, containerid) {
	if (page_request.readyState == 4
			&& (page_request.status == 200 || window.location.href
					.indexOf("http") == -1)) {
		// hack by manu
		// we need to evaluate the JS of the page when loading it
		var div = document.getElementById(containerid)
		div.innerHTML = page_request.responseText
		var x = div.getElementsByTagName("script")
	    for(var i=0;i<x.length;i++)
	    {
	       eval(x[i].text)
	    }
	}
}

function setAndEvalResponse(containerid, transport) {
	$(containerid).innerHTML = transport.responseText
	var x = $(containerid).getElementsByTagName("script")
    for(var i=0;i<x.length;i++)
    {
       eval(x[i].text)
    }
}

function loadobjs() {
	if (!document.getElementById)
		return
	for (i = 0; i < arguments.length; i++) {
		var file = arguments[i]
		var fileref = ""
		if (loadedobjects.indexOf(file) == -1) { // Check to see if this
			// object has not already
			// been added to page before
			// proceeding
			if (file.indexOf(".js") != -1) { // If object is a js file
				fileref = document.createElement('script')
				fileref.setAttribute("type", "text/javascript");
				fileref.setAttribute("src", file);
			} else if (file.indexOf(".css") != -1) { // If object is a css
				// file
				fileref = document.createElement("link")
				fileref.setAttribute("rel", "stylesheet");
				fileref.setAttribute("type", "text/css");
				fileref.setAttribute("href", file);
			}
		}
		if (fileref != "") {
			document.getElementsByTagName("head").item(0).appendChild(fileref)
			loadedobjects += file + " " // Remember this object as being already
					// added to page
		}
	}
}

/*******************************************************************************
 * Ajax Includes script- © Dynamic Drive DHTML code library
 * (www.dynamicdrive.com) This notice MUST stay intact for legal use Visit
 * Dynamic Drive at http://www.dynamicdrive.com/ for full source code
 ******************************************************************************/

// To include a page, invoke ajaxinclude("afile.htm") in the BODY of page
// Included file MUST be from the same domain as the page displaying it.
function ajaxinclude(url) {
	var page_request = false
	if (window.XMLHttpRequest) // if Mozilla, Safari etc
		page_request = new XMLHttpRequest()
	else if (window.ActiveXObject) { // if IE
		try {
			page_request = new ActiveXObject("Msxml2.XMLHTTP")
		} catch (e) {
			try {
				page_request = new ActiveXObject("Microsoft.XMLHTTP")
			} catch (e) {
			}
		}
	} else
		return false
	page_request.open('GET', url, false) // get page synchronously
	page_request.send(null)
	writecontent(page_request)
}

function writecontent(page_request) {
	if (window.location.href.indexOf("http") == -1
			|| page_request.status == 200)
		document.write(page_request.responseText)
}

// Basic Ajax Routine- Author: Dynamic Drive (http://www.dynamicdrive.com)
// Last updated: Jan 15th, 06'

function createAjaxObj() {
	var httprequest = false
	if (window.XMLHttpRequest) { // if Mozilla, Safari etc
		httprequest = new XMLHttpRequest()
		if (httprequest.overrideMimeType)
			httprequest.overrideMimeType('text/xml')
	} else if (window.ActiveXObject) { // if IE
		try {
			httprequest = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				httprequest = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {
			}
		}
	}
	return httprequest
}

var ajaxpack = new Object()
ajaxpack.basedomain = "http://" + window.location.hostname
ajaxpack.ajaxobj = createAjaxObj()
ajaxpack.filetype = "txt"
ajaxpack.addrandomnumber = 0
// Set to 1 or 0. See documentation.

ajaxpack.getAjaxRequest = function(url, parameters, callbackfunc, filetype) {
	ajaxpack.ajaxobj = createAjaxObj() // recreate ajax object to defeat cache
	// problem in IE
	if (ajaxpack.addrandomnumber == 1) // Further defeat caching problem in IE?
		var parameters = parameters + "&ajaxcachebust=" + new Date().getTime()
	if (this.ajaxobj) {
		this.filetype = filetype
		this.ajaxobj.onreadystatechange = callbackfunc
		this.ajaxobj.open('GET', url + "?" + parameters, true)
		this.ajaxobj.send(null)
	}
}

ajaxpack.postAjaxRequest = function(url, parameters, callbackfunc, filetype) {
	ajaxpack.ajaxobj = createAjaxObj() // recreate ajax object to defeat cache
	// problem in IE
	if (this.ajaxobj) {
		this.filetype = filetype
		this.ajaxobj.onreadystatechange = callbackfunc;
		this.ajaxobj.open('POST', url, true);
		this.ajaxobj.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");
		this.ajaxobj.setRequestHeader("Content-length", parameters.length);
		this.ajaxobj.setRequestHeader("Connection", "close");
		this.ajaxobj.send(parameters);
	}
}

// ACCESSIBLE VARIABLES (for use within your callback functions):
// 1) ajaxpack.ajaxobj //points to the current ajax object
// 2) ajaxpack.filetype //The expected file type of the external file ("txt" or
// "xml")
// 3) ajaxpack.basedomain //The root domain executing this ajax script, taking
// into account the possible "www" prefix.
// 4) ajaxpack.addrandomnumber //Set to 0 or 1. When set to 1, a random number
// will be added to the end of the query string of GET requests to bust file
// caching of the external file in IE. See docs for more info.

// ACCESSIBLE FUNCTIONS:
// 1) ajaxpack.getAjaxRequest(url, parameters, callbackfunc, filetype)
// 2) ajaxpack.postAjaxRequest(url, parameters, callbackfunc, filetype)

// /////////END OF ROUTINE HERE////////////////////////

// ////EXAMPLE USAGE ////////////////////////////////////////////
/*
 * Comment begins here
 * 
 * //Define call back function to process returned data function
 * processGetPost(){ var myajax=ajaxpack.ajaxobj var
 * myfiletype=ajaxpack.filetype if (myajax.readyState == 4){ //if request of
 * file completed if (myajax.status==200 ||
 * window.location.href.indexOf("http")==-1){ if request was successful or
 * running script locally if (myfiletype=="txt") alert(myajax.responseText) else
 * alert(myajax.responseXML) } } }
 * 
 * /////1) GET Example- alert contents of any file (regular text or xml file):
 * 
 * ajaxpack.getAjaxRequest("example.php", "", processGetPost, "txt")
 * ajaxpack.getAjaxRequest("example.php", "name=George&age=27", processGetPost,
 * "txt") ajaxpack.getAjaxRequest("examplexml.php", "name=George&age=27",
 * processGetPost, "xml")
 * ajaxpack.getAjaxRequest(ajaxpack.basedomain+"/mydir/mylist.txt", "",
 * processGetPost, "txt")
 * 
 * /////2) Post Example- Post some data to a PHP script for processing, then
 * alert posted data:
 * 
 * //Define function to construct the desired parameters and their values to
 * post via Ajax function getPostParameters(){ var
 * namevalue=document.getElementById("namediv").innerHTML //get name value from
 * a DIV var agevalue=document.getElementById("myform").agefield.value //get age
 * value from a form field var poststr = "name=" + encodeURI(namevalue) +
 * "&age=" + encodeURI(agevalue) return poststr }
 * 
 * var poststr=getPostParameters()
 * 
 * ajaxpack.postAjaxRequest("example.php", poststr, processGetPost, "txt")
 * ajaxpack.postAjaxRequest("examplexml.php", poststr, processGetPost, "xml")
 * 
 * Comment Ends here
 */


