package com.sarae.model;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.*;

import com.sarae.MainActivity;
import com.sarae.model.Batiment.Niveau;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.os.CountDownTimer;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

public class Reseau{
	
	 public enum Status{
	    	CONNECTE, ENVOIE, RECEPTION, STANDBY, DOWN, ERREUR;
	    	public String toString(){
	    		switch(this){
	    			case CONNECTE:
	    				return "Status: Connection";
	    			case ENVOIE:
	    				return "Status: Envoie";
	    			case RECEPTION:
	    				return "Status: Reception";
	    			case STANDBY:
	    				return "Status: StandBy";
	    			case DOWN:
	    				return "Status: Down";
	    			case ERREUR:
	    				return "Status: Erreur";
	    			default:
	    				return "Status: Down";
	    		}
	    	}
	    }
	private boolean connected=false;
	// CrÃƒÂ©ation du socket et connexion
    private  Status status=Status.DOWN;
    private byte[] reponse;
    public String commande;
    private boolean timeElapsed=false;
    private double x,y;
    
    private static Reseau singleton = new Reseau();
    
    public static class JsonParser {

		static InputStream is = null;

		static JSONObject jObj = null;

		static String json = "";

		// Constructeur de notre classe
		public JsonParser() {

		}
    }

	
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
			

		Vector<Bitmap> tiles = new Vector<Bitmap>();
		
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
		DataManager.origin_Latitude=Double.parseDouble(links[0]);//Latitude
		DataManager.origin_Longitude=Double.parseDouble(links[1]);//longitude
		DataManager.map_largeur=Integer.parseInt(links[2]);//largeur map en nb tiles
		System.out.println("origin_x : "+ DataManager.origin_Latitude);
		System.out.println("origin_y : "+ DataManager.origin_Longitude);
		System.out.println("largeur : "+ DataManager.map_largeur);
		System.out.println("nombre d'image a dl : "+(links.length-3));
		for (int i=3; i<links.length;i++)//AJOUT DES IMAGES DE LA CARTE
		{
			System.out.println("FICHIER:"+links[i]);
			try {
				envoie("map/Z18/"+links[i],myActivity);
			} catch (Exception e) {
				return false;
			} //On rÃ©cupÃ¨re une image
			tiles.add(BitmapFactory.decodeByteArray(reponse, 0, reponse.length));
			DataManager.addZone(tiles.get(i-3));
		}
		
		
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
													bat.getString("typepentetoit"), bat.getString("typemattoit"), null, new Position(bat.getDouble("lat_batiment"), bat.getDouble("lon_batiment")),
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
				tmp.niveaux.add(tmp.new Niveau(niv.getInt("numniveau"),niv.getInt("id_niveau"),niv.getInt("nombre_pieces"), getBitmap(niv.getString("plan"), myActivity), new Vector<Batiment.Niveau.CodeEtare>()));
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
		
		return true; //Tout va bien wesh
	}
	
	/*public void testPierre(Context context) {
		Batiment batbat = new Batiment();
		Batiment.Niveau nivniv = batbat.new Niveau();
		
		Map<String, Batiment.Niveau.CodeEtare> map = new HashMap<String, Batiment.Niveau.CodeEtare>();
		map.put("nocif", nivniv.new CodeEtare("nocif", DataManager.getBitmapFromAsset(context, "etare/nocif.bmp")));
		map.put("toxique", nivniv.new CodeEtare("toxique", DataManager.getBitmapFromAsset(context, "etare/toxique.jpg")));
		map.put("comburant", nivniv.new CodeEtare("comburant", DataManager.getBitmapFromAsset(context, "etare/comburant.jpg")));
		
		Vector<Batiment.Niveau.CodeEtare> vec = new Vector<Batiment.Niveau.CodeEtare>();
		vec.add(map.get("toxique"));
		vec.add(map.get("comburant"));
		
		Vector<Batiment.Niveau.CodeEtare> vec2 = new Vector<Batiment.Niveau.CodeEtare>();
		vec2.add(map.get("nocif"));
		
		Batiment tmp = new Batiment(0,20,10,6,
							1,1,"HopitalNice","1",
							"Tr","1", null, new Position(4.637843,43.673163), new Vector<Batiment.Niveau>());
	    //tmp.niveaux.add(tmp.new Niveau(0, 5, DataManager.getBitmapFromAsset(context, "plans/plan2D.png"), vec));
		//tmp.niveaux.add(tmp.new Niveau(1, 5, DataManager.getBitmapFromAsset(context, "plans/plan2D.png"), vec2));
		//tmp.niveaux.add(tmp.new Niveau(2, 5, DataManager.getBitmapFromAsset(context, "plans/plan2D.png"), vec));
	    DataManager.addBatiment(tmp);
	    
	    tmp = new Batiment(1,20,20,10,
							1,1,"IUT","1",
							"Pt","1", null, new Position(4.640016,43.672491), new Vector<Batiment.Niveau>());
	    tmp.niveaux.add(tmp.new Niveau(0, 6, DataManager.getBitmapFromAsset(context, "plans/plan2D_2.png"), vec));
		tmp.niveaux.add(tmp.new Niveau(1, 3, DataManager.getBitmapFromAsset(context, "plans/plan2D_3.jpg"), vec2));
	    DataManager.addBatiment(tmp);
	    
	    tmp = new Batiment(2,50,20,20,
							1,1,"Batiment 443","1",
							"lol","1", null, new Position(4.638685,43.672018), new Vector<Batiment.Niveau>());
	    tmp.niveaux.add(tmp.new Niveau(-1, 4, DataManager.getBitmapFromAsset(context, "plans/plan2D_3.jpg"), vec));
		tmp.niveaux.add(tmp.new Niveau(0, 5, DataManager.getBitmapFromAsset(context, "plans/plan2D.png"), vec2));
		tmp.niveaux.add(tmp.new Niveau(1, 5, null, vec));
		tmp.niveaux.add(tmp.new Niveau(2, 5, DataManager.getBitmapFromAsset(context, "plans/plan2D_2.png"), null));
		tmp.niveaux.add(tmp.new Niveau(3, 5, DataManager.getBitmapFromAsset(context, "plans/plan2D.png"), vec));
	    DataManager.addBatiment(tmp);
		
	}*/
	
	public Status getStatus()
	{
		return status;
	}
	/*
	 * FAIRE UNE FONCTION ENVOIE ET RECUP!
	 */
	private void envoie(String cmd,MainActivity myActivity) throws Exception{
		commande=cmd;
		reponse=null;
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet("http://192.168.7.1/"+commande);
				HttpResponse rep = null;
				try {
					rep = httpClient.execute(httpget);
					System.out.println("Envoie requette get");
				} catch (ClientProtocolException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
}
