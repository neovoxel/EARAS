package com.sarae.view.onglets.schema3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.sarae.model.Batiment;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class Etare3D {
	private int[] id = null;
	private Bitmap bmp = null;
	
	private float[] tabPoints;
	private FloatBuffer vertexBuffer;
	private short[] surfaceIndexes;
	private ShortBuffer indexBufferSurfaces;
		
	public Etare3D(Batiment.Niveau.CodeEtare code) {
		//bmp = code.logo;
	    
	    //generate();
	}
	
	public void generate(float x, float y, float z, float l) {
		tabPoints = new float[4*3];
		
		int i = 0;
		tabPoints[i++] = x;
		tabPoints[i++] = y+0.01f;
		tabPoints[i++] = z;
		
		tabPoints[i++] = x-l; 
		tabPoints[i++] = y+0.01f;
		tabPoints[i++] = z;
		
		tabPoints[i++] = x-l;
		tabPoints[i++] = y+l-0.01f;
		tabPoints[i++] = z;
		
		tabPoints[i++] = x;
		tabPoints[i++] = y+l-0.01f;
		tabPoints[i++] = z;
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(tabPoints.length * 4);	// float has 4 bytes
	    vbb.order(ByteOrder.nativeOrder());
	    vertexBuffer = vbb.asFloatBuffer();
	    vertexBuffer.put(tabPoints);
	    vertexBuffer.position(0);
		
		surfaceIndexes = new short[] {
			0, 1, 2,
			0, 2, 3
		};
		
		ByteBuffer ibb = ByteBuffer.allocateDirect(surfaceIndexes.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBufferSurfaces = ibb.asShortBuffer();
		indexBufferSurfaces.put(surfaceIndexes);
		indexBufferSurfaces.position(0);
	}
	
	public void loadTexture(GL10 gl) {
		id = new int[1];
	    gl.glGenTextures(1, id, 0);
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, id[0]);
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
	    gl.glFlush();

	    //if (bmp != null) bmp.recycle();
	}
	
	public void draw(GL10 gl) {
		//gl.glPushMatrix();
		
		//gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColor4f(1.f, 0.f, 0.f, 1.f);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, surfaceIndexes.length, GL10.GL_UNSIGNED_SHORT, indexBufferSurfaces);
		
	    //gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    
	    //gl.glPopMatrix();
	}
}
