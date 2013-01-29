package com.sarae.view.onglets.schema3d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;



import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class VortexRenderer implements GLSurfaceView.Renderer {
	
	private float _red = 1.f;
	private float _green = 1.f;
	private float _blue = 1.f;
	private float _angle = 0.f;
	private Batiment3D myBat;
	
	public VortexRenderer(Batiment3D bat) {
		myBat = bat;
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
	}
	
	public void onSurfaceChanged(GL10 gl, int w, int h) {
		gl.glViewport(0, 0, w, h);
		
	    // make adjustments for screen ratio
	    float ratio = (float) w / h;
	    gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
	    gl.glLoadIdentity();                        // reset the matrix to its default state
	    gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);  // apply the projection matrix
	}
	
	public void onDrawFrame(GL10 gl) {
	    // define the color we want to be displayed as the "clipping wall"
	    gl.glClearColor(_red, _green, _blue, 1.0f);
	    // clear the color buffer to show the ClearColor we called above...
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	    gl.glClearDepthf(1.0f);
	    
	    //gl.glEnable(GL10.GL_BLEND);
	    //gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 
	    gl.glEnable(GL10.GL_DEPTH_TEST);
	   //gl.glFrontFace(GL10.GL_CCW);
       // gl.glEnable(GL10.GL_CULL_FACE);
        //gl.glCullFace(GL10.GL_BACK);
        //gl.glEnable(GL10.GL_LINE_SMOOTH);
        
        //gl.glLineWidth(1.2f);
	    
	    gl.glRotatef(_angle, 0f, 1f, 0f);
	    gl.glTranslatef(0f, -0.25f, 0f);
	    
	    myBat.draw(gl);
	    
	    // Set GL_MODELVIEW transformation mode
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();                      // reset the matrix to its default state
        
        // When using GL_MODELVIEW, you must set the camera view
        GLU.gluLookAt(gl, 0, 1, -4,			// eye
        				0f, 0f, 0f,			// center
        				0f, 1.0f, 0.0f);	// up
        
        //gl.glDisable(GL10.GL_CULL_FACE);
        //gl.glDisable(GL10.GL_BLEND); 
	}
	
	public void setAngle(float angle) {
	    _angle = angle;
	}
}
