package com.sarae.model;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.sarae.MainActivity;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.widget.ImageView;


public class Reseau{
	
	private static boolean connected=false;
	// Création du socket et connexion
    private static Socket sRecup;
    private static InetAddress host;
    private static int port;
	
	private Reseau(){}
	
	public static void setHost(String _host)
	{ try {
		host=InetAddress.getByName(_host);
	} catch (UnknownHostException e) {
	}}
	
	public static InetAddress getHost()
	{return host;}
	
	public static void setPort(int _port)
	{ port=_port;}
	
	public static int getPort()
	{ return port;}
	
	public static void setConnexion(String _host, int _port)
	{
		try { host=InetAddress.getByName(_host); }
		catch (UnknownHostException e) {  }
		port=_port;
	}	
	
	public static boolean connexion(){
		 try {
			sRecup = new Socket(host, port);	// J'écris un commentaire lol - Pierre
		 } catch (UnknownHostException e) {
			 System.out.println(" 1 Marche po");
			 Log.e("1", "erreur host",e);
			return connected=false;
		 } catch (IOException e) {
			 System.out.println(" 2 Marche po");
			 Log.e("2", "erreur IOException",e);
			return connected=false;
		 } catch (NetworkOnMainThreadException e) {
			 System.out.println(" 3 Marche po");
			 Log.e("3", "erreur Thread",e);
			 return connected=false;
		 }
		 return connected=true;
	}
	
	public static void deconnexion()
	{
		if(sRecup != null)
			try {
				sRecup.close();
			} catch (IOException e)
			{  }
	}
	
	public static boolean isConnected()
	{return connected;}
	
	public static boolean ping()
	{	
		new Thread(new Runnable() {
			 
			public void run() {
				setConnexion("192.168.7.1", 80);
				
				if(connexion())
				{
					byte[] buffer = {'c', 'o', 'u', 'c', 'o', 'u'};
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
			
	}
	
	public static boolean chargerDonnees(Context context) {
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
		
		return true; //Tout va bien wesh
	}
	
	private static String getSubString(String chaine)
	{
		int i=0;
		while(chaine.charAt(i)!=';' || i<chaine.length())
			i++;
		String s = chaine.substring(0, i-1);
		chaine=chaine.substring(i+1); 
		return s; 
	}
	
}
