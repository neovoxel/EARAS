package com.sarae.view;

import com.sarae.model.Batiment;
import com.sarae.model.DataManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Bouttonbat extends ImageView {
	
	Activity myActivity;
	
	public Bouttonbat(final Context context,Activity act,int posX,int posY,final Batiment bat) {
		super(context);
		setClickable(true);
		myActivity = act;
		setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				Intent myIntent = new Intent(context, ActivityOnglets.class);
				myIntent.putExtra("id_bat", bat.id_batiment);
				myActivity.startActivity(myIntent);
			}
		});
		
		Bitmap tmp = DataManager.getBitmapFromAsset(context, "img/bouttonrouge.png");
		setImageBitmap(tmp);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(posX-tmp.getWidth()/2, posY-tmp.getHeight()/2, 0, 0);
		setLayoutParams(lp);
		
		
	}

}
