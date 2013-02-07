package com.sarae.view.onglets.schema3d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;



import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class VortexRenderer implements GLSurfaceView.Renderer {
	private float _angle = 0.f;
	private Batiment3D myBat;
	
	public VortexRenderer(Batiment3D bat) {
		myBat = bat;
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		gl.glShadeModel(GL10.GL_SMOOTH);
		// define the color we want to be displayed as the "clipping wall"
	    gl.glClearColor(1.f, 1.f, 1.f, 1.f);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		myBat.loadTextures(gl);
		gl.glDisable(GL10.GL_TEXTURE_2D);
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
	    // clear the color buffer to show the ClearColor we called above...
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	    gl.glClearDepthf(1.0f);
	    
	    
	    gl.glTranslatef(0f, -0.25f, 0f);
	    gl.glRotatef(_angle, 0f, 1f, 0f);
	    
	    myBat.drawEtares(gl);
	    myBat.draw(gl);
	    
	    // Set GL_MODELVIEW transformation mode
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();                      // reset the matrix to its default state
        
        // When using GL_MODELVIEW, you must set the camera view
        GLU.gluLookAt(gl, 0, 1, -4,			// eye
        				0f, 0f, 0f,			// center
        				0f, 1.0f, 0.0f);	// up
	}
	
	public void setAngle(float angle) {
	    _angle = angle;
	}
}
