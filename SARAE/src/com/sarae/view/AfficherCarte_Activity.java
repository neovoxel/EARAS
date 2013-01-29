package com.sarae.view;

import java.util.ArrayList;
import java.util.Vector;

import com.sarae.R;
import com.sarae.model.Batiment;
import com.sarae.model.DataManager;

import android.os.Bundle;
import android.app.Activity;
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

	private float XMin,YMin,XPas,YPas;
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
        
        iv.setImageBitmap(combineImages(bitmaplist));
        
        
        ///////////////////////////////////////////////////////////////
        
        batiments = DataManager.getBatiments();
        
        ////////////////////////////////////////////////////////////////
        
        XPas=0.001379f;
        YPas=0.000991f;
        
        XMin=4.637528f;//ORIGINE EN X
        YMin= 43.673764f;//ORIGINE EN Y
        
       rl = (RelativeLayout) findViewById(R.id.Relat);
       rl.addView(new BouttonBleu(rl.getContext(), xtoi(4.639458f), ytoi(43.672647f)));
       
       for(int i=0;i<batiments.size();i++)
       {
    	   rl.addView(new Bouttonbat(rl.getContext(),this, xtoi(batiments.get(i).position.x), ytoi(batiments.get(i).position.y), batiments.get(i)));
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

    public int xtoi(double d)
    {
    	float w=3*XPas;
		return (int)((d-XMin)*size.x/w);
    	
    }
    
    public int ytoi(double d)
    {
    	float w=2*YPas;
		return (int)((YMin-d)*size.y/w);
    	
    }
    
    /*public static Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(strName);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
        	System.out.println("bitmap null");
            return null;
            
        }
        
      

        return bitmap;
    }*/
    
    public Bitmap combineImages(ArrayList<Bitmap> listeimage) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom 
        Bitmap cs = null; 
     
        int width, height = 0; 
         
        
          width = 3*256; 
          height = 2*256; 
  
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 
     
        Canvas comboImage = new Canvas(cs); 
        
        for (int i=0;i<2;i++)
        {
        	for (int j=0;j<3;j++)
        	{
        		comboImage.drawBitmap(listeimage.get(j+i*3), j*256f, i*256f, null); 
      
        	}
        }

        return cs; 
      }
    
    }

