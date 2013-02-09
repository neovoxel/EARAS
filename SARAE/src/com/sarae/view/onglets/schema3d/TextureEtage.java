package com.sarae.view.onglets.schema3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.sarae.model.DataManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class TextureEtage {
	private int[] id = null;
	private Bitmap bmp = null;
	
	private float[] tabPoints;
	private FloatBuffer vertexBuffer;
	private short[] surfaceIndexes;
	private ShortBuffer indexBufferSurface;
	private float[] texCoords;
	private FloatBuffer texBuffer;
		
	public TextureEtage(TypeEtage e) {
		bmp = e.getBitmap();
		
		texCoords = new float[] {
			0.f, 1.f,
			1.f, 1.f,	// I have no idea what I'm doing
			1.f, 0.f,
			0.f, 0.f
		};
		
		ByteBuffer ibb = ByteBuffer.allocateDirect(texCoords.length * 4);
		ibb.order(ByteOrder.nativeOrder());
		texBuffer = ibb.asFloatBuffer();
		texBuffer.put(texCoords);
		texBuffer.position(0);
	}
	
	public void generate(float x, float y, float z, float l) {
		tabPoints = new float[4*3];
		
		int i = 0;
		tabPoints[i++] = x;			//	oo
		tabPoints[i++] = y+0.01f;	//	xo
		tabPoints[i++] = z;
		
		tabPoints[i++] = x-l;		//	oo
		tabPoints[i++] = y+0.01f;	//	ox
		tabPoints[i++] = z;
		
		tabPoints[i++] = x-l;		//	ox
		tabPoints[i++] = y+l-0.01f;	//	oo
		tabPoints[i++] = z;
		
		tabPoints[i++] = x;			//	xo
		tabPoints[i++] = y+l-0.01f;	//	oo
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
		indexBufferSurface = ibb.asShortBuffer();
		indexBufferSurface.put(surfaceIndexes);
		indexBufferSurface.position(0);
	}
	
	public void loadTexture(GL10 gl) throws Exception {
		if (bmp != null) {
			id = new int[1];
		    gl.glGenTextures(1, id, 0);
		    gl.glBindTexture(GL10.GL_TEXTURE_2D, id[0]);
		    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		    gl.glFlush();
		}
		else
			throw new Exception("can't load texture : Bitmap is null");
	}
	
	public void draw(GL10 gl) {
		gl.glColor4f(1.f, 1.f, 1.f, 0.5f);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, id[0]);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, surfaceIndexes.length, GL10.GL_UNSIGNED_SHORT, indexBufferSurface);
	}
	
	public enum TypeEtage {
		RDC, SSOL;
		private static final String[] FILES = new String[]
		{ "RDC.jpg", "SSOL.jpg" };
		private static Bitmap[] BMPS = new Bitmap[2];
		public static void loadBitmaps(Context c) {
			for (int i = 0 ; i < values().length ; ++i)
				BMPS[i] = DataManager.getBitmapFromAsset(c, FILES[i]);
		};
		public Bitmap getBitmap()
		{ return BMPS[this.ordinal()]; }
	};
}
