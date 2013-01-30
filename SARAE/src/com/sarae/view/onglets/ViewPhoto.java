package com.sarae.view.onglets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
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
		File myfile = myActivity.getDir("Photo",Activity.MODE_PRIVATE);
		String[] fichier = myfile.list();
		for (int i=0; i<fichier.length;i++)
		{
			String path=myfile.getAbsolutePath() + File.separator+ fichier[i];
			bitmap.add(BitmapLoader.loadBitmap(path, 200, 120));
			System.out.println(myfile.getAbsolutePath() + File.separator+ fichier[i]);
		}
			
	}
	
	public void update()
	{

		
		File myfile = myActivity.getDir("Photo",Activity.MODE_PRIVATE);
		String[] fichier = myfile.list();
		String path=myfile.getAbsolutePath() + File.separator+ fichier[fichier.length-1];
		bitmap.add(BitmapLoader.loadBitmap(path, 200, 120));
		gallery.setAdapter(new ImageAdapter(context));
	}
	
	
	
	
}
