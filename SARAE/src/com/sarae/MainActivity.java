package com.sarae;

import com.sarae.model.DataManager;
import com.sarae.model.Reseau;
import com.sarae.view.AfficherCarte_Activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ImageView Bconnexion;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    Bconnexion = (ImageView) findViewById(R.id.BouttonConnexion);
	    Bconnexion.setImageBitmap(DataManager.getBitmapFromAsset(this, "connexion.png"));
	    Bconnexion.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean ok = true;
				//ok = Reseau.ping();
				if (!Reseau.ping())
					Toast.makeText(MainActivity.this, "Erreur lors  du ping", Toast.LENGTH_SHORT).show();
				if (ok)
					ok = Reseau.chargerDonnees(MainActivity.this);
				if (ok)
					startActivity(new Intent(MainActivity.this, AfficherCarte_Activity.class));
				
				if (!ok)
					Toast.makeText(MainActivity.this, "Erreur lors  de la connexion", Toast.LENGTH_SHORT).show();
			}
	    });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
