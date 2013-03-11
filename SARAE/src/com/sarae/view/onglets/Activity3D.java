package com.sarae.view.onglets;

import com.sarae.R;
import com.sarae.model.Batiment;
import com.sarae.model.DataManager;
import com.sarae.view.onglets.schema3d.VortexView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Représente la vue 3D.
 * C'est cette classe qui affiche la VortexView et les boutons plans 2D.
 * @author Dark Ghost
 *
 */
public class Activity3D extends Activity {
	private LinearLayout myLayout;
	private Button[] boutonsEtages;
	private Button retour3D;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        
        setContentView(R.layout.activity3d);
        
        final Batiment bat = DataManager.getBatimentById(b.getInt("id_bat"));
        boutonsEtages = new Button[bat.getNbNiveaux()];
        
        retour3D = new Button(this);
        retour3D.setText("Retour 3D");
        retour3D.setVisibility(View.GONE);
        retour3D.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				myLayout.removeViewAt(1);
				myLayout.addView(new VortexView(Activity3D.this, null, bat), 1);
				retour3D.setVisibility(View.GONE);
				for (int k = 0 ; k < bat.getNbNiveaux() ; ++k) {
					boutonsEtages[k].setTextSize(14.f);
					boutonsEtages[k].setTextColor(Color.BLACK);
				}
			}
		});
        
        myLayout = (LinearLayout) findViewById(R.id.monLayout);
        myLayout.addView(new VortexView(this, null, bat), 1);
        
        LinearLayout plans2D = (LinearLayout) findViewById(R.id.layoutPlans2D);
        
        for (int i = 0 ; i < bat.getNbNiveaux() ; ++i) {
        	final int j = i;
        	boutonsEtages[i] = new Button(this);
        	boutonsEtages[i].setText("Etage " + bat.niveaux.elementAt(i).numEtage);
        	
            if (bat.niveaux.elementAt(i).plan2D == null)
            	boutonsEtages[i].setEnabled(false);
            
            boutonsEtages[i].setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					myLayout.removeViewAt(1);
					
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
					
					ImageView plan = new ImageView(Activity3D.this);
					plan.setImageBitmap(bat.niveaux.elementAt(j).plan2D);
					plan.setLayoutParams(layoutParams);
					myLayout.addView(plan, 1);
					
					for (int k = 0 ; k < bat.getNbNiveaux() ; ++k) {
						boutonsEtages[k].setTextSize(14.f);
						boutonsEtages[k].setTextColor(Color.BLACK);
					}
					
					boutonsEtages[j].setTextSize(19.f);
					boutonsEtages[j].setTextColor(Color.rgb(139,90,43));
					
					retour3D.setVisibility(View.VISIBLE);
				}
			});
            plans2D.addView(boutonsEtages[i]);
        }
        
        plans2D.addView(retour3D);
    }
}
