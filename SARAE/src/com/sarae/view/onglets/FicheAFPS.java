package com.sarae.view.onglets;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FicheAFPS {
	
	WebView myweb;
	Button save;
	Context context;
	
	public FicheAFPS(Context _context)
	{
		context=_context;
		
		LinearLayout.LayoutParams layoutParamWeb = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.1f);
		LinearLayout.LayoutParams layoutParambutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f - 0.1f);
		
		myweb=new WebView(context);
		myweb.getSettings().setJavaScriptEnabled(true);
		myweb.loadUrl("file:///android_asset/webview/fiche_AFPS.html");
		myweb.setLayoutParams(layoutParamWeb);
		
		save = new Button(context);
		save.setText("Enregister");
		save.setLayoutParams(layoutParambutton);
		save.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				//enregistrer();
				Toast.makeText(context, "Quand ça marchera, ça enregistrera", Toast.LENGTH_LONG).show();
			}
		});
		
		
	}
	
	public void addtoLayout(LinearLayout layout)
	{
		layout.removeAllViews();
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(myweb);
		layout.addView(save);
	}

}
