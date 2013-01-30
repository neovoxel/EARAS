function parseur()
{
	var tab = document.getElementsByTagName("input");
	var com = document.getElementsByTagName("textarea");
	var radio = "";
	var checkbox = "";
	var text = "";
	var resultat = "";
	var textarea = "";
	
	for (var i = 0; i < tab.length; i++) 
	{
		if(tab[i].type == "radio")
		{
			if(tab[i].checked)
			{
				radio += tab[i].id + ',;,';
			}
		}
		else if(tab[i].type == "checkbox")
		{
			if(tab[i].checked)
			{
				checkbox += tab[i].id + ',;,';
			}
		}
		else if(tab[i].type == "text")
		{
			if(tab[i].value == "")
			{
				text += tab[i].id + ":NULL,;,";
			}
			else
			{
				text += tab[i].id + ':' + tab[i].value + ',;,';
			}
			
		}
		
	}
	for (var j = 0; j < com.length; j++) 
	{
		if(com[j].value == "")
		{
			textarea += com[j].id + ":NULL,;,";
		}
		else
		{
			textarea += com[j].id + ':' + com[j].value + ',;,';
		}	
	}

	resultat = "____Radio____,;," + radio + "____Checkbox____,;," + checkbox + "____Text____,;,"+ text + "____Textarea____,;," + textarea;
	resultat = resultat.slice(0,-3);
	window.MyJavaScriptInterface.saveResultat(resultat);
}
