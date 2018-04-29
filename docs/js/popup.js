// Use this script in combination with popup.css for in-window pop-ups.

function hidePopup(id) {
	document.getElementById(id).className = "popupHidden";
}
function showPopup(id) {
	var popupDiv=document.getElementById(id);

//	add a close button ONCE
	if(!(popupDiv.childNodes[0].tagName=="BUTTON") && !(popupDiv.childNodes[0].className=="popupX")) {
		var closeButton = document.createElement("BUTTON");
		var closePopupAttri = document.createAttribute("class"); 
		closePopupAttri.value="popupX";
		closeButton.setAttributeNode(closePopupAttri);
		closeButton.appendChild(document.createTextNode("X"));
		closeButton.addEventListener('click', hidePopup.bind(null, id));
		popupDiv.insertBefore(closeButton, popupDiv.childNodes[0]);
	}
	
	popupDiv.className = "popupVisibly";
}