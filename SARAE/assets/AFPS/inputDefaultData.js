function inputDefaultData()
{

	var str = window.MyJavaScriptInterface.getDefaultData();
	var index = "";
	var tab = str.split(",;,");

	for(var i = 0 ; i < tab.length ; i++)
	{
		index = tab[i].split(":");
		document.getElementById(index[0]).value = index[1];
	}
}
