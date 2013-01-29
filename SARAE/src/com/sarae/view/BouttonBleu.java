package com.sarae.view;

import java.util.ArrayList;

import com.sarae.model.DataManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BouttonBleu extends ImageView implements SensorEventListener {
	Bitmap bleu;
	private float angle;
    private long lastOrientationUpdate = System.currentTimeMillis();
    private final static int SENSOR_REFRESH_MS = 500;
	
	public BouttonBleu(final Context context,int posX,int posY) {
		super(context);
		setClickable(true);
		
		bleu = DataManager.getBitmapFromAsset(context, "fleche.png");
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(posX-bleu.getWidth()/2, posY-bleu.getHeight()/2, 0, 0);
		setLayoutParams(lp);
		setImageBitmap(bleu);
      
		final Toast toast = Toast.makeText(context, "Ceci est VOUS", Toast.LENGTH_LONG);
		setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				toast.show();
			}
		});
		
		//SensorManager.getOrientation();
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		long currTime = System.currentTimeMillis();
		 
        float values[] = event.values;
 
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION:
 
                if (currTime - lastOrientationUpdate < SENSOR_REFRESH_MS)
                    break;
                lastOrientationUpdate = System.currentTimeMillis();
 
                angle = values[0];
                
                this.setRotation(angle);
                
                break;
 
        }
		
	}
}
