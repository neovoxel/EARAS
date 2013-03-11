package com.sarae.view.onglets;

import com.sarae.model.DataManager;
import com.sarae.view.RapportPDF;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class InfoChoix {
	private ImageButton buttonPDF;
	private ImageButton button3D;
	private Activity activity;
	private Context context;
	
	public InfoChoix(Context _context, AttributeSet attrs, final int id_bat, Activity a) {
		activity = a;
		context = _context;
		
		LinearLayout.LayoutParams layoutParamsPDF = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParamsPDF.setMargins(0, 0, 10, 0);
		
		buttonPDF = new ImageButton(context, attrs);
		buttonPDF.setImageBitmap(DataManager.getBitmapFromAsset(context, "buttonPDF.png"));
		buttonPDF.setLayoutParams(layoutParamsPDF);
		buttonPDF.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (DataManager.getBatimentById(id_bat).docPDF != null) {
					Intent myIntent = new Intent(context, RapportPDF.class);
					myIntent.putExtra("id_bat", id_bat);
					activity.startActivity(new Intent(context, RapportPDF.class));
				}
				else
					Toast.makeText(activity, "Impossible d'afficher le rapport PDF pour ce bâtiment : données insuffisantes.", Toast.LENGTH_SHORT).show();
			}
		});
		
		LinearLayout.LayoutParams layoutParams3D = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams3D.setMargins(10, 0, 0, 0);
		
		button3D = new ImageButton(context, attrs);
		button3D.setImageBitmap(DataManager.getBitmapFromAsset(context, "button3D.png"));
		button3D.setLayoutParams(layoutParams3D);
		button3D.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (DataManager.getBatimentById(id_bat).getNbNiveaux() > 0) {
					Intent myIntent = new Intent(context, Activity3D.class);
					myIntent.putExtra("id_bat", id_bat);
					activity.startActivity(myIntent);
				}
				else
					Toast.makeText(activity, "Impossible d'afficher la vue 3D pour ce bâtiment : données insuffisantes.", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void addToLayout(LinearLayout layout) {
		layout.removeAllViews();
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(buttonPDF);
		layout.addView(button3D);
	}
}
