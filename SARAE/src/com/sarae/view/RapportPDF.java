package com.sarae.view;

import java.io.ByteArrayOutputStream;

import com.sarae.R;
import com.sarae.model.Batiment;
import com.sarae.model.DataManager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.webkit.WebView;

public class RapportPDF extends Activity {
	private WebView web;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
        
		setContentView(R.layout.activity_pdf);
        
        final Batiment bat = DataManager.getBatimentById(20);
		
		web=(WebView) findViewById(R.id.webview_pdf);
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setBuiltInZoomControls(true);
		web.setBackgroundColor(Color.GRAY);
		String content = "<html>";
		for(int i = 0 ; i < 1 ; i++)
		{//Changer class batiment pour qu'elle recoive plusieurs Rapports
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bat.docPDF.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			content += "<img style=\"min-width: 100%;\" src=\"data:image/jpeg;base64,"+Base64.encodeToString(byteArray, 0)+"\"><br />";
		}
		content +="</html>";
		System.out.println(content);
		web.loadDataWithBaseURL("file:///android_asset/",content,"text/html","utf-8","");
	}
}
