package com.sarae.view;

import com.sarae.R;

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

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_pdf);
		web=(WebView) findViewById(R.id.webview_pdf);
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setBuiltInZoomControls(true);
		web.setBackgroundColor(Color.GRAY);
		web.loadDataWithBaseURL("file:///android_asset/","<html><center><img src=\"pdfTest.jpg\"></html>","text/html","utf-8","");
	
		
	}
	
	
	
	

}
