package com.sarae.view.onglets;

import com.sarae.R;
import com.sarae.model.Batiment;
import com.sarae.model.DataManager;
import com.sarae.view.onglets.schema3d.VortexView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Activity3D extends Activity {
	private LinearLayout myLayout;
	private Button retour3D;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        
        setContentView(R.layout.activity3d);
        
        final Batiment bat = DataManager.getBatiment(b.getInt("id_bat"));
        
        retour3D = new Button(this);
        retour3D.setText("Retour 3D");
        retour3D.setVisibility(View.GONE);
        retour3D.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				myLayout.removeViewAt(1);
				myLayout.addView(new VortexView(Activity3D.this, null, bat), 1);
				retour3D.setVisibility(View.GONE);
			}
		});
        
        myLayout = (LinearLayout) findViewById(R.id.monLayout);
        myLayout.addView(new VortexView(this, null, bat), 1);
        
        LinearLayout plans2D = (LinearLayout) findViewById(R.id.layoutPlans2D);
        
        for (int i = 0 ; i < bat.getNbNiveaux() ; ++i) {
        	final int j = i;
        	Button bouton = new Button(this);
            bouton.setText("Etage " + bat.niveaux.elementAt(i).numEtage);
            if (bat.niveaux.elementAt(i).plan2D == null)
            	bouton.setEnabled(false);
            bouton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					myLayout.removeViewAt(1);
					
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
					//layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
					
					ImageView plan = new ImageView(Activity3D.this);
					plan.setImageBitmap(bat.niveaux.elementAt(j).plan2D);
					plan.setLayoutParams(layoutParams);
					myLayout.addView(plan, 1);
					
					retour3D.setVisibility(View.VISIBLE);
				}
			});
            plans2D.addView(bouton);
        }
        
        plans2D.addView(retour3D);
    }
}
