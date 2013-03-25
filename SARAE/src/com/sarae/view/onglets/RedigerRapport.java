package com.sarae.view.onglets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RedigerRapport{
	private EditText text;
	private Button save;
	private int id_bat=0;
	private Activity myActivity;
	private Context context;
	
	public RedigerRapport(Context _context,AttributeSet attrs,Activity act, int id) {
		context = _context;
		id_bat= id;
		myActivity=act;
		
		LinearLayout.LayoutParams layoutParamText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.1f);
		LinearLayout.LayoutParams layoutParambutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f - 0.1f);
		
		save = new Button(context,attrs);
		save.setText("Enregister");
		save.setLayoutParams(layoutParambutton);
		save.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				enregistrer();
			}
		});
		
		text = new EditText(context,attrs);
		text.setText(lireFichier(id_bat+".rap"));
		text.setLayoutParams(layoutParamText);
		text.setGravity(Gravity.TOP);
	}

	
	private void enregistrer() {
		ecrireFicher(id_bat+".rap", text.getText().toString());
	}

	 private String lireFichier(String nomFichier) {

         File dir = myActivity.getDir("Rapports",Activity.MODE_PRIVATE);
         File newfile = new File(dir.getAbsolutePath() + File.separator + nomFichier);
         String monText="";
         BufferedReader input = null;

         try {
           input = new BufferedReader(new InputStreamReader( new FileInputStream(newfile)));
           String line;

           StringBuffer buffer = new StringBuffer();

           while ((line = input.readLine()) != null) {
                buffer.append(line);
           }

            monText = buffer.toString();
         }
         catch (Exception e) { e.printStackTrace(); }
         finally {
	         if (input != null) {
		         try { input.close(); }
		         catch (IOException e) { e.printStackTrace(); }
	         }
         }
       return monText;

       }
	private void ecrireFicher(String nomFichier,String monText) {

        BufferedWriter writer = null;

        try {
		   File dir = myActivity.getDir("Rapports",myActivity.MODE_PRIVATE);
		   File newfile = new File(dir.getAbsolutePath() + File.separator + nomFichier);
		   newfile.createNewFile();
		   writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newfile)));
		   writer.write(monText);
        }
        catch (Exception e)
        { e.printStackTrace(); }
        finally {
               if (writer != null) {
                      try {
                             writer.close();
                         	Toast.makeText(context,"Le rapport a été enregistré avec succès.", Toast.LENGTH_LONG).show();
                      } catch (IOException e) {
                             e.printStackTrace();
                      }
               }
        }
  }
	
	public void addtoLayout(LinearLayout layout)
	{
		layout.removeAllViews();
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(text);
		layout.addView(save);
	}
	
	
}
