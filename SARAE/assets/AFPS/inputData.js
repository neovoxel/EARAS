function inputData()
{
	var resultat = window.MyJavaScriptInterface.getData();

	if(resultat != "")
	{
		var tab = "";
		var tableau = resultat.split(",;,");

		for(var i = 0 ; i < tableau.length ; i++)
		{
			if(tableau[i] == "____Radio____")	// Type Input Radio
			{
				i++;
				while(tableau[i] != "____Checkbox____")
				{
					document.getElementById(tableau[i]).checked = true;
					i++;
				}
				i--;		
			}
			else if(tableau[i] == "____Checkbox____")	// Type Input Checkbox
			{
				i++;
				while(tableau[i] != "____Text____")
				{
					document.getElementById(tableau[i]).checked = true;
					i++;
				}
				i--;
			}
			else if(tableau[i] == "____Text____")	// Type Input Text
			{
				i++;
				while(tableau[i] != "____Textarea____")
				{
					tab = tableau[i].split(":");
					if(tab[1] != "NULL")
						document.getElementById(tab[0]).value = tab[1];
					i++;
				}
				i--;
			}
			else if(tableau[i] == "____Textarea____")	// Type Input Textarea
			{
				i++;
				while(i < tableau.length)
				{
					tab = tableau[i].split(":");
					if(tab[1] != "NULL")
						document.getElementById(tab[0]).value = tab[1];
					i++;
				}
			}
		}
	}
}

