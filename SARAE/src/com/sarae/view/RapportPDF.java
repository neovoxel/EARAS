package com.sarae.view;

import com.sarae.R;
import com.sarae.model.Batiment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.webkit.WebView;

public class RapportPDF extends Activity {
	WebView web;
	Batiment bat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_pdf);
		web=(WebView) findViewById(R.id.webview_pdf);
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setBuiltInZoomControls(true);
		web.setBackgroundColor(Color.GRAY);
		String content = "<html><center>";
		for(int i = 0 ; i < 1 ; i++) //Changer class batiment pour qu'elle recoive plusieurs Rapports
			content += "<div><img src=\""+bat.docPDF+"\"></div><br />";
		content +="</center></html>";
		web.loadDataWithBaseURL("file:///android_asset/",content,"text/html","utf-8","");
	}
}
