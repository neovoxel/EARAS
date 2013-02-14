package com.sarae.view;

import com.sarae.R;
import com.sarae.model.DataManager;
import com.sarae.view.onglets.FicheAFPS;
import com.sarae.view.onglets.InfoChoix;
import com.sarae.view.onglets.RedigerRapport;
import com.sarae.view.onglets.ViewPhoto;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ActivityOnglets extends Activity {
	private int id_bat = 0;
	private Onglet onglet = Onglet.INFO;
	private LinearLayout myLayout;
	private ImageView OngletInfo;
	private ImageView OngletRapport;
	private ImageView OngletAFPS;
	private ImageView OngletPhoto;
	private InfoChoix info;
	private RedigerRapport rapport;
	private ViewPhoto camera;
	private FicheAFPS afps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_onglets);
		Bundle b = getIntent().getExtras();
		id_bat=b.getInt("id_bat");
		
		myLayout = (LinearLayout) findViewById(R.id.LinearLayoutOnglets);
		
		info = new InfoChoix(this, null, id_bat, this);
		rapport = new RedigerRapport(this, null, this, id_bat);
		camera = null;
		afps = new FicheAFPS(this, id_bat);
		
		info.addToLayout(myLayout);
		
		OngletInfo = (ImageView) findViewById(R.id.OngletInfo);
		OngletInfo.setImageBitmap(DataManager.getBitmapFromAsset(this, "onglets/OngletInfoRouge.png"));
		OngletInfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setOnglet(Onglet.INFO);
			}
		});
		
		OngletRapport = (ImageView) findViewById(R.id.OngletRapport);
		OngletRapport.setImageBitmap(DataManager.getBitmapFromAsset(this, "onglets/OngletRapport.png"));
		OngletRapport.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setOnglet(Onglet.RAPPORT);
			}
		});
		
		OngletAFPS = (ImageView) findViewById(R.id.OngletAFPS);
		OngletAFPS.setImageBitmap(DataManager.getBitmapFromAsset(this, "onglets/OngletAFPS.png"));
		OngletAFPS.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setOnglet(Onglet.AFPS);
			}
		});
		
		OngletPhoto = (ImageView) findViewById(R.id.OngletPhoto);
		OngletPhoto.setImageBitmap(DataManager.getBitmapFromAsset(this, "onglets/OngletPhoto.png"));
		OngletPhoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setOnglet(Onglet.PHOTO);
			}
		});
	}
	
	private void setOnglet(Onglet o) {
		if (onglet != o) {
			switch (onglet) {
				case RAPPORT:
					OngletRapport.setImageBitmap(DataManager.getBitmapFromAsset(this, "onglets/OngletRapport.png"));
					break;
				case AFPS:
					OngletAFPS.setImageBitmap(DataManager.getBitmapFromAsset(this, "onglets/OngletAFPS.png"));
					break;
				case PHOTO:
					OngletPhoto.setImageBitmap(DataManager.getBitmapFromAsset(this, "onglets/OngletPhoto.png"));
					break;
				case INFO:
				default:
					OngletInfo.setImageBitmap(DataManager.getBitmapFromAsset(this, "onglets/OngletInfo.png"));
					break;
			}
			onglet = o;
			switch (onglet) {
				case RAPPORT:
					OngletRapport.setImageBitmap(DataManager.getBitmapFromAsset(this, "onglets/OngletRapportRouge.png"));
					rapport.addtoLayout(myLayout);
					break;
				case AFPS:
					OngletAFPS.setImageBitmap(DataManager.getBitmapFromAsset(this, "onglets/OngletAFPSRouge.png"));
					afps.addtoLayout(myLayout);
					break;
				case PHOTO:
					OngletPhoto.setImageBitmap(DataManager.getBitmapFromAsset(this, "onglets/OngletPhotoRouge.png"));
					camera = new ViewPhoto(this,null,this, id_bat);
					camera.addtoLayout(myLayout);
					break;
				case INFO:
				default:
					OngletInfo.setImageBitmap(DataManager.getBitmapFromAsset(this, "onglets/OngletInfoRouge.png"));
					info.addToLayout(myLayout);
					break;
			}
		}
	}
	
	private enum Onglet {
		INFO, RAPPORT, AFPS, PHOTO;
	}
}
