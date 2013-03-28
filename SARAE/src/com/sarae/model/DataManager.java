package com.sarae.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import com.sarae.model.Batiment.Niveau;

import android.R.integer;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class DataManager {
	public static Vector<Bitmap> imgZone = new Vector<Bitmap>();
	private static Vector<String> photos = new Vector<String>();
	private static Vector<Batiment> batiments = new Vector<Batiment>();
	private static double posLatitude=0;
	private static double posLongitude=0;
	public static double origin_Latitude=0;
	public static double origin_Longitude=0;
	public static int map_largeur=0;
	
	private DataManager() {  }
	
	// ------ methodes ADD -------
	public static void addBatiment(Batiment bat)
	{ batiments.add(bat);}
	
	public static void addZone(Bitmap img)
	{ imgZone.add(img);}
	
	public static void addPhoto(String img)
	{ photos.add(img);}
	
	//public void addMapTile(MapTile mapTile)
	//{ mapTiles.add(mapTile);}
	
	// ------ methodes GET -------
	public static Batiment getBatiment(int index)
	{ return batiments.get(index);}
	
	public static Vector<Batiment> getBatiments() {
		return (Vector<Batiment>) batiments.clone();
	}
	
	public static Batiment getBatimentById(int id) {
		for (int i = 0 ; i < batiments.size() ; ++i) {
			if (batiments.get(i).id_batiment == id)
				return batiments.get(i);
		}
		return null;
	}
	
	public static Niveau getNiveauById(int id) {
		for (int i = 0 ; i < batiments.size() ; ++i) {
			for (int j=0; j< batiments.get(i).niveaux.size();j++){
			if (batiments.get(i).niveaux.get(j).id_Niveau == id)
				return batiments.get(i).niveaux.get(j);
			}
		}
		return null;
	}
	
	public static void tri_Niveaux() {
		for (int i = 0 ; i < batiments.size() ; ++i) {
			Batiment current_bat = batiments.get(i);
			
			int[] tabNiv = new int[current_bat.niveaux.size()];
			for (int j = 0 ; j < current_bat.niveaux.size() ; ++j)
				tabNiv[j] = current_bat.niveaux.get(j).numEtage;
			
			for (int j = 0 ; j < current_bat.niveaux.size() ; ++j)
				System.out.println("LOOOOOOOOOOOL1  " + tabNiv[j]);
			
			triBulle(tabNiv);
			
			for (int j = 0 ; j < current_bat.niveaux.size() ; ++j)
				System.out.println("LOOOOOOOOOOOL2  " + tabNiv[j]);
			
			Vector<Batiment.Niveau> niveaux = new Vector<Batiment.Niveau>();
			
			for (int j = 0 ; j < current_bat.niveaux.size() ; ++j) {
				for (int k = 0 ; k < current_bat.niveaux.size() ; ++k) {
					if (current_bat.niveaux.get(k).numEtage == tabNiv[j])
						niveaux.add(current_bat.niveaux.get(k));
				}
			}
			
			current_bat.niveaux = niveaux;
		}
		
		/*for (int i = 0 ; i < batiments.size() ; ++i) {
			Batiment current_bat = batiments.get(i);
			Vector<Integer> indices = new Vector<Integer>();
			
			for (int k = 0 ; k < current_bat.getNbNiveaux() ; ++k) {
				int indice_min = current_bat.getNbNiveaux()-1;
				for (int j = 0 ; j < current_bat.getNbNiveaux() ; ++j) {
					if ((current_bat.niveaux.get(j).numEtage < current_bat.niveaux.get(indice_min).numEtage)) {
						boolean exists = false;
						for (int l = 0 ; l < indices.size() ; ++l) {
							if (indices.get(l) == j) {
								exists = true;
								break;
							}
						}
						if (!exists)
							indice_min = j;
					}
				}
				indices.add(indice_min);
			}
			
			Vector<Batiment.Niveau> niveaux = new Vector<Batiment.Niveau>();
			
			for (int j = 0 ; j < current_bat.getNbNiveaux() ; ++j)
				niveaux.add(current_bat.niveaux.get(indices.get(j)));
			
			current_bat.niveaux = niveaux;
		}*/
	}
	
	public static void triBulle(int tableau[]) {
	    int longueur=tableau.length;
	    boolean inversion;
	    
	    do {
	        inversion=false;
	
	        for(int i=0;i<longueur-1;i++) {
	            if(tableau[i]>tableau[i+1]) {
	                echanger(tableau,i,i+1);
	                inversion=true;
	            }
	        }
	    } while(inversion);
    }
	
	private static void echanger(int tableau[], int a, int b) {
		 int tmp = tableau[a];
         tableau[a] = tableau[b];
         tableau[b] = tmp;
	}
	
	public static Bitmap getZone(int index)
	{ return imgZone.get(index); }
	
	public static String getPhoto(int index)
	{ return photos.get(index); }
	
	public static int getPhotoSize()
	{ return photos.size(); }
	
	//public MapTile getMapTile(int index)
	//{ return mapTiles.get(index);}
	
	public static Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(strName);
            bitmap = BitmapFactory.decodeStream(istr);
        }
        catch (IOException e) {
        	System.out.println("bitmap null");
            return null;
        }
        
        return bitmap;
    }
	
	// ------ methodes DEL -------
	public static void delBatiment(int index)
	{ batiments.removeElementAt(index);}
	
	public static void delZone(int index)
	{ imgZone.removeElementAt(index);}
	
	public static void delPhoto(int index)
	{ photos.removeElementAt(index);}
	
	public static void delPhoto(String img) {
		for (int i = 0 ; i < photos.size() ; ++i) {
			if (photos.get(i) == img) {
				delPhoto(i);
				return;
			}
		}
			
	}
	
	//public void delMapTiles(int index)
	//{ mapTiles.removeElementAt(index);}
	
	// ------ methodes CLEAR -------
	public static void clearBatiment()
	{ batiments.clear();}
	
	public static void clearZone()
	{ imgZone.clear();}
	
	public static void clearPhoto()
	{ photos.clear();}
	
	//public void clearMapTile()
	//{ mapTiles.clear();}
	
	public static void clearAll()
	{
		batiments.clear();
		imgZone.clear();
		photos.clear();
		//mapTiles.clear();
	}
	
	public static final  void DeleteFile(String path)
	{
		File tmp = new File(path);
		tmp.delete();
	}
	
	public static void setLatitude(double lat)
	{
		posLatitude = lat;
	}
	
	public static double getLatitude()
	{
		return posLatitude;
	}
	
	public static void setLongitude(double lon)
	{
		posLongitude=lon;
	}
	
	public static double getLongitude()
	{
		return posLongitude;
	}
	
	public static String lireFichier(String dossier,String nomFichier,Context context) 
    {
        File dir = context.getDir(dossier,Context.MODE_PRIVATE);
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
		        	 Toast.makeText(context,"Impossible d'ouvrir :"+nomFichier, Toast.LENGTH_LONG).show();
		        	 e.printStackTrace(); 
		         }
	         }
        }
        return monText;

      }
}
