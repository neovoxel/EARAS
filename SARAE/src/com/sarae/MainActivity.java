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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ImageView Bconnexion;
	private ProgressBar progressBar;
	private TextView textProgress;
	private LinearLayout Layout;
	private TextView connexion;
	Reseau reseau;
	private double x=0,y=0;
	private ProgressDialog progressDialog;
	private Thread tmpThread;
	public Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch(msg.what){
				case 0: //Transfert terminé
				progressDialog.dismiss();
				Toast.makeText(MainActivity.this, "Thread terminé", Toast.LENGTH_SHORT).show();
				MainActivity.this.startActivity(new Intent(MainActivity.this, AfficherCarte_Activity.class));
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
					Toast.makeText(MainActivity.this, "Impossible de se connecter au serveur", Toast.LENGTH_SHORT).show();
					break;
			}
		};
		};

	

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    progressDialog = new ProgressDialog(this);
	    Layout= (LinearLayout) findViewById(R.id.LinearLayout_connec);
	    connexion=(TextView) findViewById(R.id.TexteConnexion);
	    progressBar = (ProgressBar) findViewById(R.id.progressBar1);
	    progressBar.setVisibility(ProgressBar.INVISIBLE);
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
	      
	    
	    textProgress = (TextView) findViewById(R.id.textProgress);
	    textProgress.setVisibility(TextView.INVISIBLE);
	    Bconnexion = (ImageView) findViewById(R.id.BouttonConnexion);
	    Bconnexion.setImageBitmap(DataManager.getBitmapFromAsset(this, "connexion.png"));
	    Bconnexion.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
					lancerProgress();
					
					/*reseau = Reseau.getInstance();
					progressDialog.setMessage("Coucou");
					progressDialog.show();

					
					Thread tmpthread = new Thread(new Runnable() {
					public void run() {		
					reseau.chargerDonnees(4.639686,43.672070,MainActivity.this,MainActivity.this);
					progressDialog.dismiss();

					}});
					
					
					tmpthread.start();
					

					
					/*try {
						tmpthread.join();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					System.out.println("Thread reseau terminé");
					startActivity(new Intent(MainActivity.this, AfficherCarte_Activity.class));
					
					/*progressBar.setVisibility(ProgressBar.GONE);
					textProgress.setVisibility(TextView.INVISIBLE);
		    		Layout.removeAllViews();
		    		Layout.addView(Bconnexion);
		    		Layout.addView(connexion);*/
					/*while(reseau.getStatus()!=Status.STANDBY)
					{
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						textProgress.setText(reseau.getStatus().toString());
					}*/
						
					 //Toast.makeText(MainActivity.this, "Transfert terminée", Toast.LENGTH_SHORT).show();
		    		
				}
				
	    });
    }
    
    public void mySetText(String s)
    {
    	//textProgress.setText(s);
    }
    
    private void lancerProgress()
    {
    	reseau = Reseau.getInstance();

		progressDialog.setMessage("Connexion");
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		tmpThread = new Thread(new Runnable() {
		
		public void run() {		
		if(reseau.chargerDonnees(4.639686,43.672070,MainActivity.this,MainActivity.this))
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


