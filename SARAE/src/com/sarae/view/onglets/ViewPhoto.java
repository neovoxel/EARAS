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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ViewPhoto{
	private SurfaceView  camera;
	private Gallery gallery;
	private int id_bat=0;
	private Activity myActivity;
	private Context context;
	private ArrayList<Drawable> drawables;
	public ViewPhoto(Context _context,AttributeSet attrs,Activity act, int id) {
		context = _context;
		id_bat= id;
		myActivity=act;
		
		LinearLayout.LayoutParams layoutPhotoView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.1f);
		LinearLayout.LayoutParams layoutParambutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f - 0.1f);
		
		camera = new CameraLivePreview(context,myActivity,id_bat);
		camera.setLayoutParams(layoutPhotoView);
		
		getDrawableList();
		gallery=new Gallery(context);
		gallery.setLayoutParams(layoutParambutton);
		gallery.setAdapter(new ImageAdapter(context));
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				
				
			}

			public void onNothingSelected(AdapterView<?> parent) {

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
			return drawables.size();
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

			// Get a View to display image data 					
			ImageView iv = new ImageView(this.myContext);
			iv.setImageDrawable(drawables.get(position));

			// Image should be scaled somehow
			//iv.setScaleType(ImageView.ScaleType.CENTER);
			//iv.setScaleType(ImageView.ScaleType.CENTER_CROP);			
			//iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			//iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			//iv.setScaleType(ImageView.ScaleType.FIT_END);
			
			// Set the Width & Height of the individual images
			iv.setLayoutParams(new Gallery.LayoutParams(95, 70));
			

			return iv;
		}
	}// ImageAdapter
	
	public void addtoLayout(LinearLayout layout)
	{
		layout.removeAllViews();
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(camera);
		//layout.addView(gallery);
	}
	
	private void getDrawableList(){
		
		drawables = new ArrayList<Drawable>();
		File myfile = myActivity.getDir("Photo",Activity.MODE_PRIVATE);
		String[] fichier = myfile.list();
		for (int i=0; i<fichier.length;i++)
		{
			drawables.add(Drawable.createFromPath(fichier[i]));
		}
		Toast.makeText(context, fichier[0], Toast.LENGTH_LONG).show();
			
	}
	
	
	
	
}
