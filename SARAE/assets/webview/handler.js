var tab = document.getElementById('tabfixed');
window.onscroll = function(oEvent){
	if(document.body.scrollTop > 150){
		tab.style.position = 'fixed';
		tab.style.top = '0px';
	}
	else if(document.body.scrollTop <= 150){
		tab.style.position = 'relative';
		tab.style.top = '0px';
	}
};