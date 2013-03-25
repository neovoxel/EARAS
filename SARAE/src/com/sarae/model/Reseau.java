package com.sarae.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.*;

import com.sarae.MainActivity;
import com.sarae.model.Batiment.Niveau;
import com.sarae.view.onglets.BitmapLoader;
import com.sarae.view.onglets.schema3d.TextureEtage;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;


public class Reseau{

    private byte[] reponse;
    public String commande;
    private double x,y;
    public static int nombreTileMap=0;
    public static int TileCourant=0;
    
    private static Reseau singleton = new Reseau();
    
   
	
	private Reseau() {}
	
	
	///*
	public boolean chargerDonnees(double _x, double _y,MainActivity myActivity, Context context) {
		//*
		
		/*
		 * ENVOIE DE COORDONNEE
		 */
		x=_x;y=_y;
		
		DataManager.setLatitude(x);
		DataManager.setLongitude(y);
		DataManager.clearZone();
		DataManager.clearBatiment();
			
		
		try {
			envoie("Handling.php?x="+x+"&y="+y+"",myActivity);
		} catch (Exception e) {
			return false;
		} //On demande une liste d'image
		System.out.println(new String(reponse));
		String blocs[] = new String(reponse).split("@");
		System.out.println("Bloc 0:"+blocs[0]);
		String links[] = new String(blocs[0]).split(";");
		System.out.println(reponse);
		if (links[0]=="")return false;
		DataManager.origin_Latitude=Double.parseDouble(links[0]);//Latitude
		DataManager.origin_Longitude=Double.parseDouble(links[1]);//longitude
		DataManager.map_largeur=Integer.parseInt(links[2]);//largeur map en nb tiles
		System.out.println("origin_x : "+ DataManager.origin_Latitude);
		System.out.println("origin_y : "+ DataManager.origin_Longitude);
		System.out.println("largeur : "+ DataManager.map_largeur);
		System.out.println("nombre d'image a dl : "+(links.length-3));
		nombreTileMap=links.length-3;
		TileCourant =0;
		myActivity.handler.sendEmptyMessage(2); //debut progressbar 
		for (int i=3; i<links.length;i++)//AJOUT DES IMAGES DE LA CARTE
		{
			System.out.println("FICHIER:"+links[i]);
			try {
				DataManager.addZone(getBitmap("map/Z18/"+links[i],myActivity));
			} catch (Exception e) {
				return false;
			} 
			TileCourant++;
			myActivity.handler.sendEmptyMessage(3);
		}
		
		myActivity.handler.sendEmptyMessage(4);//récupération infos batiment
		JSONObject json=null;
		try {
			json = new JSONObject(blocs[1]);
		} catch (JSONException e1) {
			e1.printStackTrace();
			return false;			
		}
		JSONArray batiments=null;
		try {
				// récupérer la liste de tous les batiments
				batiments = json.getJSONArray("batiment"); 
				// parcourir toute la liste des bat
				for (int i = 0; i < batiments.length(); i++) {
					// récupérer un bat de type JSONObject
					
					JSONObject bat = batiments.getJSONObject(i);
					DataManager.addBatiment(new Batiment(bat.getInt("0"),(float) bat.getDouble("hauteur_batiment"),(float)bat.getDouble("largeur_batiment"),(float)bat.getDouble("profondeur_batiment"),
											(float)bat.getDouble("degrespentetoit"), (float)bat.getDouble("tauxvulnera"), bat.getString("libelle_batiment"), bat.getString("typemateriaux_batiment"),
													bat.getString("typepentetoit"), bat.getString("typemattoit"), getBitmap("data/PDF/"+bat.getInt("0")+".png" +
															"", myActivity), new Position(bat.getDouble("lat_batiment"), bat.getDouble("lon_batiment")),
													new Vector<Niveau>()));
					System.out.println("batiment ajouter");
	
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		
		JSONArray niveaux=null;
		try {
			// récupérer la liste de tous les etages
			niveaux = json.getJSONArray("niveau"); 
			// parcourir toute la liste des bat
			for (int i = 0; i < niveaux.length(); i++) {
				// récupérer un bat de type JSONObject
				JSONObject niv = niveaux.getJSONObject(i);
				Batiment tmp = DataManager.getBatimentById(niv.getInt("id_batiment"));
				tmp.niveaux.add(tmp.new Niveau(niv.getInt("numniveau"),niv.getInt("id_niveau"),niv.getInt("nombre_pieces"), getBitmap("data/plan/"+niv.getString("plan"), myActivity), new Vector<Batiment.Niveau.CodeEtare>()));
				System.out.println("niveau ajouter");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		
		JSONArray dangers=null;
		Map<Integer, Batiment.Niveau.CodeEtare> map = new HashMap<Integer, Batiment.Niveau.CodeEtare>();
		try {
			// récupérer la liste de tous les produits
			dangers = json.getJSONArray("produitsdangereux"); 
			// parcourir toute la liste des produits
			for (int i = 0; i < dangers.length(); i++) {
				// récupérer un produit de type JSONObject
				JSONObject dan = dangers.getJSONObject(i);
				Batiment batbat = new Batiment();
				Batiment.Niveau nivniv = batbat.new Niveau();
				map.put((Integer)dan.getInt("id_produit"), nivniv.new CodeEtare(dan.getString("attributproduit"),getBitmap("ETARE_LOGO/"+dan.getString("attributproduit")+".jpg",myActivity)));
				System.out.println("danger ajouter");

			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		JSONArray association=null;
		try {
			// récupérer la liste de tous les produits
			association = json.getJSONArray("contient"); 
			// parcourir toute la liste des produits
			for (int i = 0; i < association.length(); i++) {
				// récupérer un produit de type JSONObject
				JSONObject asso = association.getJSONObject(i);
				DataManager.getNiveauById(asso.getInt("id_niveau")).codes.add(map.get(asso.getInt("id_produit")));
				System.out.println("danger ajouter");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		
		TextureEtage.TypeEtage.loadBitmaps(context);
		DataManager.tri_Niveaux();
		return true; //Tout va bien wesh
	}
	
	public boolean emettreDonnees(MainActivity myActivity)
	{
		ArrayList<DataBatiment> listeData= new ArrayList<DataBatiment>();
		
		File myRapport = myActivity.getDir("Rapports",Activity.MODE_PRIVATE);
		String [] listerapport = myRapport.list();
		File myAFPS = myActivity.getDir("Fiche_AFPS",Activity.MODE_PRIVATE);
		String [] listeafps= myAFPS.list();
		File myfile = myActivity.getDir("Photo",Activity.MODE_PRIVATE);
		String[] listefichier = myfile.list();
		
		System.out.println("afps :"+listeafps.length +" rapport : "+listerapport.length +" photo :" + listefichier.length  );
		if (listerapport.length < 1 && listeafps.length <  1 && listefichier.length <1)
		{
			myActivity.handler.sendEmptyMessage(6);
			return false;
		}
		
		for(int i = 0 ; i<listerapport.length;i++)
		{
			String tmp = "";
			int j=0;
			while (listerapport[i].charAt(j)!='.')
			{
				tmp=tmp+listerapport[i].charAt(j);
				j++;
			}
			listeData.add(new DataBatiment(Integer.parseInt(tmp), DataManager.lireFichier("Rapports", tmp+".rap", myActivity), ""));
		}
		
		
		
		for(int i = 0 ; i<listeafps.length;i++)
		{
			String tmp = "";
			int j=0;
			while (listeafps[i].charAt(j)!='.')
			{
				tmp=tmp+listeafps[i].charAt(j);
				j++;
			}
			int exist = -1;
			for( j = 0; j<listeData.size(); j++)
			{
				if(listeData.get(j).id_bat == Integer.parseInt(tmp)){exist=j;}
			}
			if(exist>=0)
				listeData.get(exist).afps=DataManager.lireFichier("Fiche_AFPS", tmp+".afps",myActivity);
			else
			{
				listeData.add(new DataBatiment(Integer.parseInt(tmp),"", DataManager.lireFichier("Fiche_AFPS", tmp+".afps", myActivity)));
			}
		}
		
		JSONArray json = new JSONArray();
		for(int i=0;i<listeData.size();i++)
		{
		JSONObject manJson = new JSONObject();
			try {
				manJson.put("id_bat",listeData.get(i).id_bat);
				manJson.put("afps", listeData.get(i).afps);
				manJson.put("rapport", listeData.get(i).rapport);
				json.put(manJson);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				myActivity.handler.sendEmptyMessage(6);
				return false;
			}
		}
		
		//System.out.println(json.toString());
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://192.168.7.1/Handling.php");
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	       // String tmp = DataManager.lireFichier("Fiche_AFPS", "20.afps", myActivity);
	        String tmp = json.toString();
	        System.out.println(tmp);
	        nameValuePairs.add(new BasicNameValuePair("retxp", tmp));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        myActivity.handler.sendEmptyMessage(5);
	    } catch (ClientProtocolException e) {
	    	myActivity.handler.sendEmptyMessage(1);
	        return false;
	    } catch (IOException e) {
	    	myActivity.handler.sendEmptyMessage(1);
	    	 return false;
	    }
	    
	    
	for (int i = 0;i<listefichier.length;i++)	
	{
		File imagefile = new File(myfile.getAbsolutePath() + File.separator+ listefichier[i]); 
		/*byte[] data = new byte[(int) imagefile.length()];
		FileInputStream fis;
		try {
			fis = new FileInputStream(imagefile);
			fis.read(data);
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}*/
		
		 httppost = new HttpPost("http://192.168.7.1/Handling.php");
		 Bitmap image = BitmapLoader.loadBitmap(imagefile.getAbsolutePath(), 1024, 768);
		 ByteArrayOutputStream stream = new ByteArrayOutputStream();
		 image.compress(Bitmap.CompressFormat.JPEG, 90, stream);
		 byte[] byteArray = stream.toByteArray();
		 String image_str = Base64.encodeToString(byteArray, 0);
         ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();

         nameValuePairs.add(new BasicNameValuePair("image",image_str));
         nameValuePairs.add(new BasicNameValuePair("name",listefichier[i]));

         try{
        	 httpclient = new DefaultHttpClient();
             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
             HttpResponse response = httpclient.execute(httppost);
            
         }catch(Exception e){
               System.out.println("Error in http connection "+e.toString());
               myActivity.handler.sendEmptyMessage(1);
               return false;
         }
	}
		
		for (int i = 0; i<listerapport.length;i++)//del rapport
		{
			DataManager.DeleteFile(myRapport.getAbsolutePath() + File.separator + listerapport[i]);
			
		}
		for (int i = 0; i<listeafps.length;i++)//del afps
		{
			DataManager.DeleteFile(myAFPS.getAbsolutePath() + File.separator + listeafps[i]);
		}
		for (int i = 0; i<listefichier.length;i++)//del photo
		{
			DataManager.DeleteFile(myfile.getAbsolutePath() + File.separator + listefichier[i]);
		}
		return true;
	}
	
	/*
	 * FAIRE UNE FONCTION ENVOIE ET RECUP!
	 */
	private void envoie(String cmd,MainActivity myActivity) throws Exception{
		commande=cmd;
		reponse=null;
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet("http://192.168.7.1/"+commande);
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
				HttpConnectionParams.setSoTimeout(httpParameters, 10000);
				httpClient.setParams(httpParameters);
				HttpResponse rep = null;
				try {
					rep = httpClient.execute(httpget);
					System.out.println("Envoie requette get");
				} catch (ClientProtocolException e) {
				
					e.printStackTrace();
					throw new Exception("Connexion impossible.");
				} catch (IOException e) {

					e.printStackTrace();
					throw new Exception("Connexion impossible.");
				} catch (Exception e) {
					throw new Exception("Connexion impossible.");
				}
				HttpEntity entity = null;
				
				if (rep != null)
				entity = rep.getEntity();
				else
				{
					myActivity.handler.sendEmptyMessage(1);
					throw new Exception("Connexion impossible.");
				}
				if (entity != null)
				{
					try {
						reponse = EntityUtils.toByteArray(entity);
						System.out.println("Reponse recu");
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					myActivity.handler.sendEmptyMessage(1);
				}
		
		
	}
		
	private Bitmap getBitmap(String lien,MainActivity activity)
	{
		try {
			envoie(lien, activity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (reponse!= null)
		return BitmapFactory.decodeByteArray(reponse, 0, reponse.length);
		return null;
	}
	
	
	public static Reseau getInstance(){
		return singleton;
	}
	
	public class DataBatiment{
		public String rapport,afps;
		public int id_bat;
		public DataBatiment(int id,String rap,String _afps)
		{
			id_bat= id;
			rapport = rap;
			afps = _afps;
		}
		}
		
	}


