package com.sarae.view.onglets;

import java.io.File;
import java.util.ArrayList;


import com.sarae.model.DataManager;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ViewPhoto{
	private SurfaceView  camera;

	private ListView gallery;
	private int id_bat=0;
	private Activity myActivity;
	private Context context;
	private ArrayList<Bitmap> bitmap;
	private ArrayList<String> imagePath;
	public ViewPhoto(Context _context,AttributeSet attrs,Activity act, int id) {
		context = _context;
		id_bat= id;
		myActivity=act;
		
		LinearLayout.LayoutParams layoutPhotoView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.20f);
		LinearLayout.LayoutParams layoutParambutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f - 0.20f);
		
		camera = new CameraLivePreview(context,myActivity,id_bat,this);
		camera.setLayoutParams(layoutPhotoView);
		
		
		getDrawableList();
		gallery=new ListView(context);
		gallery.setLayoutParams(layoutParambutton);
		gallery.setAdapter(new ImageAdapter(context));
		gallery.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("deprecation")
			public void onItemClick(AdapterView<?> arg0, View arg1,  int arg2,
					long arg3) {
				final File myfile = myActivity.getDir("Photo",Activity.MODE_PRIVATE);
				final String[] fichier = myfile.list();
				final AlertDialog al = new AlertDialog.Builder(context).create();
				final int nb = arg2;
				al.setTitle("Supprimer?");
				al.setMessage("Voulez-vous supprimer le fichier : "+fichier[arg2]);
				al.setButton("Supprimer", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						
						DataManager.DeleteFile(imagePath.get(nb));
						update();
						Toast.makeText(context, "Photo supprimée", Toast.LENGTH_SHORT).show();
						
					}
				});
				
				al.setButton2("Annuler", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
					
						al.cancel();
					}
				});
				al.show();
			}
		});
		
		
		
		
		
	}	
	

	
	public class ImageAdapter extends BaseAdapter {
		/** The parent context */
		private Context myContext;
		// Put some images to project-folder: /res/drawable/
		// format: jpg, gif, png, bmp, ...

		/** Simple Constructor saving the 'parent' context. */
		public ImageAdapter(Context c) {
			this.myContext = c;
		}

		// inherited abstract methods - must be implemented
		// Returns count of images, and individual IDs
		public int getCount() {
			return bitmap.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}
		// Returns a new ImageView to be displayed,
		public View getView(int position, View convertView, 
				ViewGroup parent) {					
			ImageView iv = new ImageView(this.myContext);
			iv.setImageBitmap(bitmap.get(position));
			iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

			
			return iv;
		}
	}// ImageAdapter
	
	public void addtoLayout(LinearLayout layout)
	{
		layout.removeAllViews();
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(camera);
		layout.addView(gallery);
	}
	
	private void getDrawableList(){
		
		bitmap = new ArrayList<Bitmap>();
		imagePath = new ArrayList<String>();
		File myfile = myActivity.getDir("Photo",Activity.MODE_PRIVATE);
		String[] fichier = myfile.list();
		for (int i=0; i<fichier.length;i++)
		{
			String path=myfile.getAbsolutePath() + File.separator+ fichier[i];
			String tmp = "";
			int j=0;
			while (fichier[i].charAt(j)!='_')
			{
				tmp=tmp+fichier[i].charAt(j);
				j++;
			}
			if(Integer.parseInt(tmp) == id_bat)
			{
			bitmap.add(BitmapLoader.loadBitmap(path, 200, 120));
			imagePath.add(path);
			
			}
		}
			
	}
	
	public void update()
	{	
		getDrawableList();
		gallery.setAdapter(new ImageAdapter(context));
	}
	
	
	
	
	
	
}
