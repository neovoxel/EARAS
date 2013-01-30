package com.sarae.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DataManager {
	private static Vector<Bitmap> imgZone = new Vector<Bitmap>();
	private static Vector<Bitmap> photos = new Vector<Bitmap>();
	private static Vector<Batiment> batiments = new Vector<Batiment>();
	//private static Vector<MapTile> mapTiles = new Vector<MapTile>();
	
	private DataManager() {  }
	
	// ------ methodes ADD -------
	public static void addBatiment(Batiment bat)
	{ batiments.add(bat);}
	
	public static void addZone(Bitmap img)
	{ imgZone.add(img);}
	
	public static void addPhoto(Bitmap img)
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
	
	public static Bitmap getZone(int index)
	{ return imgZone.get(index);}
	
	public static Bitmap getPhoto(int index)
	{ return photos.get(index);}
	
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
}
