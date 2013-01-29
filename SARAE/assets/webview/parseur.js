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
				radio += tab[i].name + ' : ' + tab[i].value + '\n';
			}
		}
		else if(tab[i].type == "checkbox")
		{
			if(tab[i].checked)
			{
				checkbox += tab[i].name + ' : ' + tab[i].value + '\n';
			}
		}
		else if(tab[i].type == "text")
		{
			if(tab[i].value == "")
			{
				tab[i].value = 'NULL';
				text += tab[i].name + ' : ' + tab[i].value + '\n';
			}
			else
			{
				text += tab[i].name + ' : ' + tab[i].value + '\n';
			}
			
		}
		
	}
	for (var j = 0; j < com.length; j++) 
	{
		if(com[j].value == "")
		{
			com[j].value = 'NULL';
			textarea += com[j].name + ' : ' + com[j].value + '\n';
		}
		else
		{
			textarea += com[j].name + ' : ' + com[j].value + '\n';
		}	
	}
	resultat = "____Radio____\n" + radio + "____Checkbox____\n" + checkbox + "____Text____\n"+ text + "____Textarea____\n" + textarea;
	alert("____Radio____\n" + radio + "____Checkbox____\n" + checkbox + "____Text____\n"+ text + "____Textarea____\n" + textarea);
}
