package com.sarae.view.onglets;
import com.sarae.model.Batiment;
import com.sarae.model.DataManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FicheAFPS {
	
	WebView myweb;
	Button save;
	Context context;
	private int id_bat;
	private String resultat;
	private String[] data;
	private int nb, enterrer, niveau;
	private String str;
	private boolean already_load;
	
	/**
	 * Constructeur de la class Fiche AFPS
	 * @param Prend en paramètre le context et l'identifiant du bâtiment.
	 */
	public FicheAFPS(Context _context, int id)
	{
		context = _context;
		
		LinearLayout.LayoutParams layoutParamWeb = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.1f);
		LinearLayout.LayoutParams layoutParambutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f - 0.1f);
		
		str = "";
        nb = 8;
        id_bat = id;
		Batiment bat = DataManager.getBatiment(id_bat);
        already_load = false;
        enterrer = niveau = 0;
        
        File dir_fiche = context.getDir("Fiche_AFPS",Context.MODE_PRIVATE);
        File fiche = new File(dir_fiche.getAbsolutePath() + File.separator + id_bat + ".afps");
        
        if(fiche.exists())
        	resultat = lireFichier(id_bat + ".afps");
        else
        	resultat = "";
        
        for(int i = 0 ; i < bat.getNbNiveaux() ; i++)
        {
        	if(bat.niveaux.elementAt(i).numEtage < 0)
        		enterrer++;
        	else
        		niveau++;
        }	
        
        if(bat.libelle == null)
        	bat.libelle = "";
        data = new String[nb];
        data[0] = "lat:" + bat.position.x + ",;,";
        data[1] = "long:" + bat.position.y + ",;,";
        data[2] = "nb_niveau:" + niveau + ",;,";
        data[3] = "enterrer:" + enterrer + ",;,";
        data[4] = "longueur:" +  bat.largeur + ",;,";
        data[5] = "largeur:" + bat.profondeur + ",;,";
        data[6] = "surf_sol:" + bat.superficie + ",;,";
        data[7] = "nom_bat:" + bat.libelle;
        
        for(int i = 0 ; i < nb ; i++)
        	str += data[i];
        
        /**
         * Inner Class JavaScript Interface permettant au fichier .js d'appeller ses fonctions.  
         * @author benenuts
         */
        class MyJavaScriptInterface
        {   
        	/**
        	 * Fonction appeller par le fichier parseur.js 
        	 * qui appelle elle-même la fonction ecrireFichier(String nomFichier,String monText) 
        	 * qui à son tour enregistre dans un fichier texte les données de la fiche AFPS
        	 * @param Prends en paramètre la String contenant la chaine du résultat
        	 */
            public void saveResultat(String resultat)  
            { ecrireFicher(id_bat + ".afps", resultat); }
            
            /**
             * Fonction appeler par le fichier inputDefaultData.js
             * qui permet d'ajouter à la fiche AFPS les données qui n'ont pas besoin d'être rempli
             * tel que les positions GPS, longueur, largeur du bâtiment etc..
             * @return Retourne la String des données par défaut
             */
            public String getDefaultData()
            { return str; }
            
            /**
             * Fonction appeler par le fichier inputData.js
             * qui permet d'ajouter à la fiche AFPS les données qui ont déjà été rempli
             * ultérieurement.
             * @return Retourne la String des données utltérieurement saisi
             */
            public String getData()  
            { return resultat; }
        }
		
		myweb = new WebView(context);
		myweb.getSettings().setJavaScriptEnabled(true);
		
		/* Register a new JavaScript interface called MyJavaScriptInterface */  
        myweb.addJavascriptInterface(new MyJavaScriptInterface(), "MyJavaScriptInterface"); 
		myweb.loadUrl("file:///android_asset/AFPS/fiche_AFPS.html");
		myweb.setLayoutParams(layoutParamWeb);
		
		myweb.setWebViewClient(new WebViewClient()
        {
			/**
			 * Fonction qui appele les fichiers inputDefaultData.js 
			 * et inputData.js une fois que la fiche AFPS à terminé de s'ouvrir
			 * pour pouvoir implenté les données nécessaire
			 */
        	public void onPageFinished(WebView view, String url)
        	{
        		if(!already_load)
        		{
        			myweb.loadUrl("javascript:inputDefaultData();");
					myweb.loadUrl("javascript:inputData();");	
					already_load = true;
        		}	
        	}
        });
		
		save = new Button(context);
		save.setText("Enregister");
		save.setLayoutParams(layoutParambutton);
		save.setOnClickListener(new OnClickListener() 
		{
			/**
			 * Lorsqu'on clique sur le bouton Enregistrer
			 * cette fonction appele le fichier parseur.js qui va
			 * récupérer les données saisis sur la fiche AFPS et appeler
			 * la fonction saveResultat(String resultat) pour les stocker dans un fichier.
			 */
			public void onClick(View arg0) 
			{
				myweb.loadUrl("javascript:parseur();");
			}
		});
		
		
	}
	
	public void addtoLayout(LinearLayout layout)
	{
		layout.removeAllViews();
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(myweb);
		layout.addView(save);
	}
	
	/**
	 * Fonction qui permet d'ouvrir un fichier contenant les données 
	 * de la fiche AFPS.
	 * @param Prend en paramètre le nom du fichier.
	 * @return Retourne la string que contient le fichier.
	 */
	private String lireFichier(String nomFichier) 
    {
        File dir = context.getDir("Fiche_AFPS",Context.MODE_PRIVATE);
        File newfile = new File(dir.getAbsolutePath() + File.separator + nomFichier);
        String monText="";
        BufferedReader input = null;

        try 
        {
          input = new BufferedReader(new InputStreamReader( new FileInputStream(newfile)));
          String line;

          StringBuffer buffer = new StringBuffer();

          while ((line = input.readLine()) != null) 
          {
               buffer.append(line);
          }

           monText = buffer.toString();
        }
        catch (Exception e) { e.printStackTrace(); }
        finally 
        {
	         if (input != null) {
		         try 
		         { input.close(); }
		         catch (IOException e) 
		         { 
		        	 Toast.makeText(context,"Impossible d'ouvrir la fiche AFPS.", Toast.LENGTH_LONG).show();
		        	 e.printStackTrace(); 
		         }
	         }
        }
        return monText;

      }

	/**
	 * Fonction qui permet d'enregistrer dans un fichier les données 
	 * de la fiche AFPS.
	 * @param Prend en paramètre le nom du fichier et la string que l'on veut enregistrer.
	 */
	private void ecrireFicher(String nomFichier,String monText) 
	{
       BufferedWriter writer = null;

       try 
       {
		   File dir = context.getDir("Fiche_AFPS",Context.MODE_PRIVATE);
		   File newfile = new File(dir.getAbsolutePath() + File.separator + nomFichier);
		   newfile.createNewFile();
		   writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newfile)));
		   writer.write(monText);
       }
       catch (Exception e)
       { e.printStackTrace(); }
       finally 
       {
              if (writer != null) 
              {
                 try 
                 {
                	 writer.close();
                	 Toast.makeText(context,"La fiche AFPS a été sauvegardé avec succès.", Toast.LENGTH_LONG).show();
                 } 
                 catch (IOException e) 
                 {
                	 Toast.makeText(context,"Erreur lors de la sauvegarde de la Fiche AFPS.", Toast.LENGTH_LONG).show();
                	 e.printStackTrace();
                 }
              }
       }
	}

}
