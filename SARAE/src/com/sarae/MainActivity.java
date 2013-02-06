package com.sarae;

import com.sarae.model.DataManager;
import com.sarae.model.Reseau;
import com.sarae.view.AfficherCarte_Activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ImageView Bconnexion;
	private ProgressBar progressBar;
	private TextView textProgress;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    progressBar = (ProgressBar) findViewById(R.id.progressBar1);
	    progressBar.setVisibility(ProgressBar.INVISIBLE);
	    
	    textProgress = (TextView) findViewById(R.id.textProgress);
	    textProgress.setVisibility(TextView.INVISIBLE);
	    Bconnexion = (ImageView) findViewById(R.id.BouttonConnexion);
	    Bconnexion.setImageBitmap(DataManager.getBitmapFromAsset(this, "connexion.png"));
	    Bconnexion.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean ok = true;
				//ok = Reseau.ping();
				Reseau reseau = Reseau.getInstance();
				reseau.start();
				if (!ok)
					Toast.makeText(MainActivity.this, "Erreur lors  de la connexion", Toast.LENGTH_SHORT).show();
				else if (ok)
				{
					//while(reseau.getStatus()==Reseau.Status.DOWN);
					ok = reseau.chargerDonnees(4.639458,43.672647,MainActivity.this);
					progressBar.setVisibility(ProgressBar.VISIBLE);
					textProgress.setText("Connexion");
					textProgress.setVisibility(TextView.VISIBLE);
					CountDownTimer timer= new CountDownTimer(3000,1000) {
		    			
		    			@Override
		    			public void onTick(long millisUntilFinished) {
		    				// TODO Auto-generated method stub
		    				
		    			}
		    			
		    			@Override
		    			public void onFinish() {
		    				
		    				progressBar.setVisibility(ProgressBar.GONE);
		    				textProgress.setVisibility(TextView.INVISIBLE);
		    				startActivity(new Intent(MainActivity.this, AfficherCarte_Activity.class));
	
		    				
		    			}
		    			
		    		}.start();
				}
				
				
			}
	    });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}

