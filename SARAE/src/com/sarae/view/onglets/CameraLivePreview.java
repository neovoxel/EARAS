package com.sarae.view.onglets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 
 * Android Camera API archetype
 * 
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 * 
 * @author antoine vianey
 *
 */
public final class CameraLivePreview extends SurfaceView 
        implements SurfaceHolder.Callback {
    
    private SurfaceHolder holder;
    private Camera camera;
    private Context myContext;
    private Activity myActivity;
	private int id_bat;
	private boolean canTake=true;
	private CountDownTimer timer;
	ViewPhoto myVp;
    
	 public CameraLivePreview(Context context, Activity activity, int idbat, ViewPhoto vp) {
	        super(context);
	        myContext=context;
	        myActivity = activity;
			id_bat=idbat;
			myVp=vp;
	        init();
	        setOnTouchListener(new OnTouchListener() {
				
				public boolean onTouch(View v, MotionEvent event) {
					if (canTake)
		        	{
		        	timer= new CountDownTimer(3000,1000) {
		    			
		    			@Override
		    			public void onTick(long millisUntilFinished) {
		    				// TODO Auto-generated method stub
		    				
		    			}
		    			
		    			@Override
		    			public void onFinish() {
		    				canTake=true;
		                	
		    				
		    			}
		    			
		    		}.start();
		    		canTake=false;
		    		camera.takePicture(shutterCallback, rawCallback, jpegCallback);
		    		
		        	}
					
					return false;
				}
			});
	       
	    }



    /**
     * Retrieve raw picture data after shooting
     */
    private Camera.PictureCallback rawCallback = 
            new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera c) {
            // work with raw data
            // ...
        }
    };
    
    /**
     * Retrieve jpeg compress data after shooting
     */
    private Camera.PictureCallback jpegCallback = 
            new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera c) {
        	
            // start the camera preview
            camera.startPreview();
            File pictureFileDir = myActivity.getDir("Photo",Activity.MODE_PRIVATE);

            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

              Log.d("MakePhotoActivity", "Impossible de créer le dossier de destination.");
              Toast.makeText(myContext, "Impossible de créer le dossier de destination.",Toast.LENGTH_LONG).show();
              return;

            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
            String date = dateFormat.format(new Date());
            String photoFile = id_bat+"_"+ date + ".jpg";

            String filename = pictureFileDir.getPath() + File.separator + photoFile;

            File pictureFile = new File(filename);

            try {
              FileOutputStream fos = new FileOutputStream(pictureFile);
              fos.write(data);
              fos.close();
              Toast.makeText(myContext, "Nouvelle image sauvegardée : " + photoFile,
                  Toast.LENGTH_LONG).show();
            } catch (Exception error) {
              Log.d("MakePhotoActivity", "File" + filename + "not saved : "
                  + error.getMessage());
              Toast.makeText(myContext, "L'image n'a pas pu être sauvegardée.",
                  Toast.LENGTH_LONG).show();
            }
            	myVp.update();

          }
        	

        
    };
    
    /**
     * Retrieve frame data for each frame
     */
    private Camera.PreviewCallback frameCallback = 
            new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            // work with the frame data
            // ...
        }
    };
    
    /**
     * Retrieve information about shutter
     */
    private Camera.ShutterCallback shutterCallback = 
            new Camera.ShutterCallback() {
        public void onShutter() {
            // handle shutter done
            // ...
        }
    };
    
    /**
     * Ensure it is supported by adding 
     * android.hardware.camera.autofocus feature
     * to the application manifest
     */
    private Camera.AutoFocusCallback autoFocusCallback = 
            new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            // handle focus done
            // you can choose to take a picture
            // after auto focus is completed
        	
    		camera.takePicture(shutterCallback, rawCallback, jpegCallback);
            
        }
    };


    
    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    
    public void surfaceCreated(SurfaceHolder holder) {
        // surface created
        // we can tell the camera to render
        // into the surface
        // but it's not ready to preview yet
        camera = Camera.open();
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException exception) {
            camera.release();
            camera = null;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // surface destroyed
        // we must tell the camera to stop it preview
        this.getHolder().removeCallback(this);
    	camera.stopPreview();
        camera.setPreviewCallback(null);
        camera.release();
        camera = null;
    }
    
    

    public void surfaceChanged(SurfaceHolder holder, 
            int format, int w, int h) {
        // we get the surface dimensions
        // we can configure the preview
    
        Camera.Parameters parameters = camera.getParameters();

        List<Size> sizes = parameters.getSupportedPreviewSizes();
        Size optimalSize = getOptimalPreviewSize(sizes, w, h);
        parameters.setPreviewSize(optimalSize.width, optimalSize.height);

        camera.setParameters(parameters);
        
        // let render
        camera.startPreview();
        camera.setPreviewCallback(frameCallback);
    	
    }

    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        
        // requirement
        final double ASPECT_TOLERANCE = 0.05;
        
        double targetRatio = (double) w / h;
        if (sizes == null) {
            return null;
        }

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // find a size that match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // it's not possible
        // ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        
        return optimalSize;
    }

    public void takePhoto() {
        // take a photo :
        // 1 - auto focus
        // 2 - take the picture in the auto focus callback
    	camera.autoFocus(autoFocusCallback);
    }
   

}
