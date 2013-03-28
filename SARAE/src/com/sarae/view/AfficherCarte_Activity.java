package com.sarae.view;

import java.util.ArrayList;
import java.util.Vector;

import com.sarae.MainActivity;
import com.sarae.R;
import com.sarae.model.Batiment;
import com.sarae.model.DataManager;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AfficherCarte_Activity extends Activity {
	ArrayList<Bitmap> bitmaplist;
	ImageView iv;
	Vector<Batiment> batiments;

	private double XMin,YMin,XPas,YPas;
	private double PosX,PosY;
	private RelativeLayout rl;
	private Point size;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_carte_);
        
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        
        iv = (ImageView) findViewById(R.id.imageView1);
        bitmaplist=new ArrayList<Bitmap>();
        
        //////////////////////////////////////////////////////////////
       
        for (int i=0;i<3;i++)
        {
        	for (int j=0;j<3;j++)
        	{
        		Bitmap tmp=DataManager.getBitmapFromAsset(this,"img/"+i+";"+j+".jpg");
        		bitmaplist.add(tmp);
        	}
        }
        
     
        //iv.setImageBitmap(DataManager.getZone(0));
        
        ///////////////////////////////////////////////////////////////
        
        batiments = DataManager.getBatiments();
       
        ////////////////////////////////////////////////////////////////
        
        PosX= DataManager.getLatitude();
        PosY= DataManager.getLongitude();
        
        XPas=0.00138044444444;
        YPas=0.0009975;
        
        XMin=DataManager.origin_Latitude;//ORIGINE EN X
        YMin= DataManager.origin_Longitude;//ORIGINE EN Y
        
       rl = (RelativeLayout) findViewById(R.id.Relat);
       
       iv.setImageBitmap(getZone(combineImages(DataManager.imgZone)));
       rl.addView(new BouttonBleu(rl.getContext(),size.x/2, size.y/2));
       
       for(int i=0;i<batiments.size();i++)
       {
    	   rl.addView(new Bouttonbat(rl.getContext(),this, xtoi(batiments.get(i).position.y,size.x/3), ytoi(batiments.get(i).position.x,size.y/2), batiments.get(i)));
       }
       
       if (batiments.size()<1)
       {
    	   AlertDialog.Builder adb = new AlertDialog.Builder(AfficherCarte_Activity.this);
		     adb.setTitle("Pas de données");
		     adb.setCancelable(false);
		     adb.setMessage("Il n'y as aucune données de batiment disponible pour cette zone");
		     adb.setIcon(android.R.drawable.ic_dialog_alert);
		     adb.setPositiveButton("OK", null);
		     adb.show();
    	   
       }
       
       
       /*rl.addView(new Bouttonbat(rl.getContext(), xtoi(4.637843f), ytoi(43.673163f)));
       rl.addView(new Bouttonbat(rl.getContext(), xtoi(4.638278f), ytoi(43.67253f)));
       rl.addView(new Bouttonbat(rl.getContext(), xtoi(4.638685f), ytoi(43.672018f)));
       rl.addView(new Bouttonbat(rl.getContext(), xtoi(4.640016f), ytoi(43.672491f)));*/
       
       
       
       
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_afficher_carte_, menu);
        return true;
        
    }

    public int xtoi(double d,int largeurTilepx)
    {
    	
		return (int)((d-XMin)/XPas*largeurTilepx);
    	
    }
    
    public int ytoi(double d,int hauteurTilepx)
    {
		return (int)((YMin-d)/YPas*hauteurTilepx);	
    }
    
    
    public Bitmap combineImages(Vector<Bitmap> listeimage) {
        Bitmap cs = null; 
     
        int width, height = 0; 
         
        
        int x= DataManager.map_largeur;
        int y= DataManager.imgZone.size()/x;
        width = x*256; 
        height = y*256; 
  
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 
     
        Canvas comboImage = new Canvas(cs); 
        
        for (int j=0;j<y;j++)
        {
        	for (int i=0;i<x;i++)
        	{
        		comboImage.drawBitmap(listeimage.get(i+j*x), i*256f, j*256f, null); 
      
        	}
        }

        return cs; 
      }
    
	    public Bitmap getZone(Bitmap map)
	    {
	    	
	    	int x =xtoi(PosX, 256)- (int)(1.5*256.0);
	    	int y = ytoi(PosY, 256)- 256;
	    	
	    	XMin=PosX-=1.5*XPas;
	    	YMin=PosY+=YPas;
	    	System.out.println("X:"+x);
	    	System.out.println("Y:"+y);
	    	Bitmap finale=null;
	    	if(x>0 && y>0)
	    	{
	    		try{
	    			finale = Bitmap.createBitmap(map, x, y, 3*256, 2*256);
	    		}
	    		catch(Exception e)
	    		{
	    			
	    			
	    			return null;
	    		}
	    		
	    	}
	    	
	    	if (finale==null)
	    	{
	    		AlertDialog.Builder adb = new AlertDialog.Builder(AfficherCarte_Activity.this);
			     adb.setTitle("Erreur de Positionement");
			     adb.setCancelable(false);
			     adb.setMessage("Impossible de vous positioner sur la carte. VAssurez-vous de ne pas être hors ou trop proche du bord de la zone disponible sur le serveur");
			     adb.setIcon(android.R.drawable.ic_dialog_alert);
			     adb.setPositiveButton("OK", new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						AfficherCarte_Activity.this.finish();
						
					}
				});
			     adb.show();
	    	}
	    	return finale;
	    }
    }
    
    

