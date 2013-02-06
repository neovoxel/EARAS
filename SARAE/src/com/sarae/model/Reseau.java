package com.sarae.model;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.NetworkOnMainThreadException;
import android.util.Log;


public class Reseau extends Thread{
	
	 public enum Status{
	    	CONNECTE, ENVOIE, RECEPTION, STANDBY, DOWN, ERREUR;
	    	public String toString(){
	    		switch(this){
	    			case CONNECTE:
	    				return "Status: Connect�";
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
	// CrÃ©ation du socket et connexion
    private Socket sRecup;
    private InetAddress host;
    private int port;
    private  Status status=Status.DOWN;
    private String reponse=null;
    public String commande;
    
    private static Reseau singleton = new Reseau();
	
	private Reseau() {}
	
	/*
	public InetAddress getHost()
	{return host;}
	
	
	public int getPort()
	{ return port;}
	
	public void setConnexion(String _host, int _port)
	{
		try { host=InetAddress.getByName(_host); }
		catch (UnknownHostException e) { status=Status.ERREUR; }
		port=_port;
	}	
	
	public boolean connexion(){
		 try {
			 setConnexion("192.168.7.1", 80);
			 sRecup = new Socket(host, port);	// J'Ã©cris un commentaire lol - Pierre
		 } catch (UnknownHostException e) {
			 System.out.println(" 1 Marche po");
			 status=Status.ERREUR;
			 Log.e("1", "erreur host",e);
			return connected=false;
		 } catch (IOException e) {
			 System.out.println(" 2 Marche po");
			 status=Status.ERREUR;
			 Log.e("2", "erreur IOException",e);
			return connected=false;
		 } catch (NetworkOnMainThreadException e) {
			 System.out.println(" 3 Marche po");
			 status=Status.ERREUR;
			 Log.e("3", "erreur Thread",e);
			 return connected=false;
		 }
		 status=Status.CONNECTE;
		 return connected=true;
	}
	
	public void deconnexion()
	{
		if(sRecup != null)
			try {
				sRecup.close();
				status = Status.DOWN;
			} catch (IOException e)
			{  }
	}
	
	public boolean isConnected()
	{return connected;}
	
	public Status getStatus(){
		return status;
	}
	/*
	public static boolean ping()
	{	
		new Thread(new Runnable() {
			 
			public void run() {
				setConnexion("192.168.7.1", 80);
				
				if(connexion())
				{
					String tmp="coucou";
					try {
						//sRecup.getOutputStream().write(buffer, 0, buffer.length);
						PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sRecup.getOutputStream())),true);
			            out.println(tmp); 
					}
					catch (IOException e) { }
				} 
				

			}}).start();
		return true;
	}//*/
	
	///*
	public boolean chargerDonnees(double x, double y,Context context) {
		//*
		
		/*
		 * ENVOIE DE COORDONNEE
		 */
		 
		
		if(status==Status.CONNECTE){
			Vector<Bitmap> tiles = new Vector<Bitmap>();
			envoie("coucou");
			System.out.println(reponse);
		}
		else{
			System.out.println("Il n'y a pas de connexion!");}
		
		//*/

		/*
		Batiment batbat = new Batiment();
		Batiment.Niveau nivniv = batbat.new Niveau();
		
		Map<String, Batiment.Niveau.CodeEtare> map = new HashMap<String, Batiment.Niveau.CodeEtare>();
		map.put("nocif", nivniv.new CodeEtare("nocif", DataManager.getBitmapFromAsset(context, "etare/nocif.bmp")));
		map.put("toxique", nivniv.new CodeEtare("toxique", DataManager.getBitmapFromAsset(context, "etare/toxique.jpg")));
		map.put("comburant", nivniv.new CodeEtare("comburant", DataManager.getBitmapFromAsset(context, "etare/comburant.jpg")));
		
		Vector<Batiment.Niveau.CodeEtare> vec = new Vector<Batiment.Niveau.CodeEtare>();
		vec.add(map.get("toxique"));
		vec.add(map.get("comburant"));
		
		Batiment tmp = new Batiment(0,20,10,6,
							1,1,"HopitalNice","1",
							"Pt","1", null, new Position(4.637843,43.673163), new Vector<Batiment.Niveau>());
	    tmp.niveaux.add(tmp.new Niveau(0, 5, DataManager.getBitmapFromAsset(context, "plans/plan2D.png"), vec));
		tmp.niveaux.add(tmp.new Niveau(1, 5, DataManager.getBitmapFromAsset(context, "plans/plan2D.png"), vec));
		tmp.niveaux.add(tmp.new Niveau(2, 5, DataManager.getBitmapFromAsset(context, "plans/plan2D.png"), vec));
		//tmp.niveaux.get(0).codes.add(tmp.niveaux.get(0).new CodeEtare("Gaz", null));
	    DataManager.addBatiment(tmp);
	    
	    tmp = new Batiment(1,20,20,10,
							1,1,"IUT","1",
							"Pt","1", null, new Position(4.640016,43.672491), new Vector<Batiment.Niveau>());
	    tmp.niveaux.add(tmp.new Niveau(0, 6, DataManager.getBitmapFromAsset(context, "plans/plan2D_2.png"), vec));
		tmp.niveaux.add(tmp.new Niveau(1, 3, DataManager.getBitmapFromAsset(context, "plans/plan2D_3.jpg"), vec));
		//tmp.niveaux.get(0).codes.add(tmp.niveaux.get(0).new CodeEtare("LOL", null));
	    DataManager.addBatiment(tmp);
	    
	    tmp = new Batiment(2,50,20,20,
							1,1,"Batiment 443","1",
							"Pt","1", null, new Position(4.638685,43.672018), new Vector<Batiment.Niveau>());
	    tmp.niveaux.add(tmp.new Niveau(-1, 4, DataManager.getBitmapFromAsset(context, "plans/plan2D_3.jpg"), vec));
		tmp.niveaux.add(tmp.new Niveau(0, 5, DataManager.getBitmapFromAsset(context, "plans/plan2D.png"), vec));
		tmp.niveaux.add(tmp.new Niveau(1, 5, null, vec));
		tmp.niveaux.add(tmp.new Niveau(2, 5, DataManager.getBitmapFromAsset(context, "plans/plan2D_2.png"), vec));
		tmp.niveaux.add(tmp.new Niveau(3, 5, DataManager.getBitmapFromAsset(context, "plans/plan2D.png"), vec));
		//tmp.niveaux.get(0).codes.add(tmp.niveaux.get(0).new CodeEtare("Acide", null));
	    DataManager.addBatiment(tmp); 
		//*/
		return true; //Tout va bien wesh
	}
	
	/*
	 * FAIRE UNE FONCTION ENVOIE ET RECUP!
	 */
	private void envoie(String cmd){
		new Thread(new Runnable() {
			public void run() {	
				HttpGet httppost = new HttpGet("http://192.168.7.1/test.txt");
				HttpClient httpclient = new DefaultHttpClient();
				try {
					httpclient.execute(httppost);
				} catch (ClientProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} //Voila, la requ�te est envoy�e
			}}).start();
		/*new Thread(new Runnable() {
			public void run() {		
				InputStream in=null;
				String tmp = "";
				try {
					in = sRecup.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				byte[] b = new byte[512];
			try {
				while(in.read(b)>0){
					for(int i=0;i<b.length;i++)
						tmp+=b[i];
					Reseau.getInstance().recoit(tmp);				
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}

		}).start();*/
	}
		
	public void recoit(String message){		
		/*
		 * Faire une fonction qui lance le thread d'écoute, et une fonction qui recoit le résultat.
		 */
		reponse=message;
	}
	
	private String getSubString(String chaine){
		int i=0;
		while(chaine.charAt(i)!=':' || i<chaine.length())
			i++;
		String s = chaine.substring(0, i-1);
		chaine=chaine.substring(i+1); 
		return s; 
	}
	
	public static Reseau getInstance(){
		return singleton;
	}

	public void run() {
		if(status==Status.DOWN){
			//connexion();
		}
		if(status==Status.ERREUR){
			System.out.println("Une erreur s'est produite sur le réseau.");
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Run ok");
	}
	
}
