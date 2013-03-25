package com.sarae;

import com.sarae.model.DataManager;
import com.sarae.model.Reseau;
import com.sarae.view.AfficherCarte_Activity;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ImageButton Bconnexion;
	private ImageButton Bemission;
	Reseau reseau;
	private double x=0,y=0;
	private ProgressDialog progressDialog;
	private Thread tmpThread;
	public Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch(msg.what){
				case 0: //Transfert terminé
				progressDialog.dismiss();
				
				boolean isnull = false ;
				for(int i = 0 ; i< DataManager.imgZone.size(); i++)
				{
					if (DataManager.imgZone.get(i) == null){isnull = true;}
				}
				if(isnull)
				{
					AlertDialog.Builder adb11 = new AlertDialog.Builder(MainActivity.this);
				     adb11.setTitle("Erreur Transfert");
				     adb11.setCancelable(false);
				     adb11.setMessage("Une erreur est survenu lors du transfert, veuillez réessayer");
				     adb11.setIcon(android.R.drawable.ic_dialog_alert);
				     adb11.setPositiveButton("OK", null);
				     adb11.setNegativeButton("Réessayer", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							lancerProgress();
							
						}
					});
				     adb11.show();
				}
				else
				{
					Toast.makeText(MainActivity.this, "Récupération terminée", Toast.LENGTH_SHORT).show();
					 MainActivity.this.startActivity(new Intent(MainActivity.this, AfficherCarte_Activity.class));
				}
				break;
				
				case 409: //Erreur
					progressDialog.dismiss();
					Toast.makeText(MainActivity.this, "Erreur 409 : Conflict", Toast.LENGTH_SHORT).show();
					break;
					
				case 400: //Erreur
					progressDialog.dismiss();
					Toast.makeText(MainActivity.this, "Erreur 400 : Bad Request", Toast.LENGTH_SHORT).show();
					break;
					
				case 404: //Erreur
					progressDialog.dismiss();
					Toast.makeText(MainActivity.this, "Erreur 404 : Not Found", Toast.LENGTH_SHORT).show();
					break;
				case 500://Erreur
					progressDialog.dismiss();
					Toast.makeText(MainActivity.this, "Erreur 500 : Internal Serveur error", Toast.LENGTH_SHORT).show();
					break;
				case 1: //Connexion Impossible
					progressDialog.dismiss();
					 AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
				     adb.setTitle("Erreur de connexion");
				     adb.setCancelable(false);
				     adb.setMessage("Impossible de se connecter au serveur");
				     adb.setIcon(android.R.drawable.ic_dialog_alert);
				     adb.setPositiveButton("OK", null);
				     adb.show();
					//Toast.makeText(MainActivity.this, "Impossible de se connecter au serveur", Toast.LENGTH_SHORT).show();
					break;
				case 2: //debut Progress Carte
					progressDialog.dismiss();
					progressDialog = new ProgressDialog(MainActivity.this);
					progressDialog.setCancelable(false);
					progressDialog.setTitle("Récupération des cartes de la zone");
					progressDialog.setMessage("Recupération en cours...");
					progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					progressDialog.setMax(Reseau.nombreTileMap);
					progressDialog.show();
					break;
				case 3://Increment ProgressBar Carte
					progressDialog.setProgress(Reseau.TileCourant);
					break;
				case 4://Recuparation des info batiment
					progressDialog.dismiss();
					progressDialog=new ProgressDialog(MainActivity.this);
					progressDialog.setMessage("Récupération des informations des bâtiments");
					progressDialog.setCancelable(false);
					progressDialog.show();
					break;
				case 5: //Emmision terminée
					progressDialog.dismiss();
					AlertDialog.Builder adb1 = new AlertDialog.Builder(MainActivity.this);
				     adb1.setTitle("Emission réussie");
				     adb1.setCancelable(false);
				     adb1.setMessage("Toutes les données ont été envoyées avec succès");
				     adb1.setIcon(android.R.drawable.ic_dialog_info);
				     adb1.setPositiveButton("OK", null);
				     adb1.show();
					//Toast.makeText(MainActivity.this, "Toutes les données ont était envoyées avec succés", Toast.LENGTH_SHORT).show();
					break;
				case 6:
					progressDialog.dismiss();
					AlertDialog.Builder adb11 = new AlertDialog.Builder(MainActivity.this);
				     adb11.setTitle("Aucune donnée");
				     adb11.setCancelable(false);
				     adb11.setMessage("Aucune donnée à envoyer au serveur");
				     adb11.setIcon(android.R.drawable.ic_dialog_alert);
				     adb11.setPositiveButton("OK", null);
				     adb11.show();
					//Toast.makeText(MainActivity.this, "Aucune donnée é envoyer au serveur", Toast.LENGTH_SHORT).show();
					break;
					
			}
		};
		};

	

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    reseau = Reseau.getInstance();
	    progressDialog = new ProgressDialog(this);
	    LocationManager lManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
	  		lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
	  			
	  			public void onStatusChanged(String provider, int status, Bundle extras) {
	  				// TODO Auto-generated method stub
	  				
	  			}
	  			
	  			public void onProviderEnabled(String provider) {
	  				// TODO Auto-generated method stub
	  				
	  			}
	  			
	  			public void onProviderDisabled(String provider) {
	  				// TODO Auto-generated method stub
	  				
	  			}
	  			

				public void onLocationChanged(Location location) {
					if (location!=null)
					{
						x=location.getLatitude();
						y=location.getLongitude();
					}
					
				}
	  			});
	      
	  	Bemission = (ImageButton) findViewById(R.id.BouttonEmission);
		Bemission.setImageBitmap(DataManager.getBitmapFromAsset(this, "newexport.png"));
	    Bconnexion = (ImageButton) findViewById(R.id.BouttonConnexion);
	    Bconnexion.setImageBitmap(DataManager.getBitmapFromAsset(this, "newcon.png"));
	    Bconnexion.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
					lancerProgress();
		    		
				}
				
	    });
	    
	    Bemission.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				AlertDialog.Builder adb11 = new AlertDialog.Builder(MainActivity.this);
			     adb11.setTitle("Confimer Emission ?");
			     adb11.setCancelable(false);
			     adb11.setMessage("L'émission des données enverra tous les documents présents sur la tablette vers le serveur. Les documents"+
			    		 			" seront ensuite effacés de la mémoire. Voulez-vous continuer?");
			     adb11.setIcon(android.R.drawable.ic_dialog_info);
			     adb11.setPositiveButton("Continuer", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						lancerEmission();
						
					}
				});
			     adb11.setNegativeButton("Annuler", null);
			     adb11.show();

				
					
		    		
				}
				
	    });
    }
    
    private void lancerEmission()
    {
    	progressDialog.setMessage("Connexion");
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		tmpThread = new Thread(new Runnable() {
			
			public void run() {		
			//if(!reseau.emettreDonnees(MainActivity.this))
				//handler.sendEmptyMessage(1);
				reseau.emettreDonnees(MainActivity.this);

			}});

			tmpThread.start();
    	
    }
    
    private void lancerProgress()
    {
    	

		progressDialog.setMessage("Connexion");
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		tmpThread = new Thread(new Runnable() {
		
		public void run() {		
		if(reseau.chargerDonnees(4.639505,43.672328,MainActivity.this,MainActivity.this))
			handler.sendEmptyMessage(0);
		else
			handler.sendEmptyMessage(1);

		}});

		tmpThread.start();
		
		
		
		
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}


