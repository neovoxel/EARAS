package com.sarae.view.onglets.schema3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import com.sarae.model.Batiment;

public class Batiment3D {
	private Batiment batiment;
	private float[] tabPoints;
	private FloatBuffer vertexBuffer;
	
	private short[][] lineIndexes;
	private short[] lineIndexToit;
	private ShortBuffer[] indexBuffer;
	private ShortBuffer indexBufferToit;
	
	private Vector<Etare3D> codeEtares;
	
	public Batiment3D(Batiment bat) {
		batiment = bat;
		
		codeEtares = new Vector<Etare3D>();
		
		lineIndexes = new short[batiment.getNbNiveaux()][];
		indexBuffer = new ShortBuffer[batiment.getNbNiveaux()];
		
		generateTabPoints();
		generateLines();
	}
	
	public void loadTextures(GL10 gl) {
		for (int i = 0 ; i < codeEtares.size() ; ++i) {
			try {
				codeEtares.get(i).loadTexture(gl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void draw(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColor4f(0.f, 0.f, 0.f, 1.f);
		for (int i = 0 ; i < batiment.getNbNiveaux() ; ++i)
		    gl.glDrawElements(GL10.GL_LINES, lineIndexes[i].length, GL10.GL_UNSIGNED_SHORT, indexBuffer[i]);
	    
		if (!batiment.typePenteToit.isEmpty())
		    gl.glDrawElements(GL10.GL_LINES, lineIndexToit.length, GL10.GL_UNSIGNED_SHORT, indexBufferToit);
		
	    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	public void drawEtares(GL10 gl) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		for (int i = 0 ; i < codeEtares.size() ; ++i)
			codeEtares.get(i).draw(gl);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	private void generateLines() {
		{
			lineIndexes[0] = new short[] {
				0, 1,
				1, 2,
				2, 3,
				3, 0,
				4, 5,
				5, 6,
				6, 7,
				7, 4
			};
			
			ByteBuffer ibb_ = ByteBuffer.allocateDirect(lineIndexes[0].length * 2);
			ibb_.order(ByteOrder.nativeOrder());
			indexBuffer[0] = ibb_.asShortBuffer();
			indexBuffer[0].put(lineIndexes[0]);
			indexBuffer[0].position(0);
		}
		if (batiment.typePenteToit == "Pt") {	// Pt // Tr
			lineIndexToit = new short[] {
				0, (short) (4 + 4 *(batiment.getNbNiveaux()-1)),
				1, (short) (5 + 4 *(batiment.getNbNiveaux()-1)),
				2, (short) (6 + 4 *(batiment.getNbNiveaux()-1)),
				3, (short) (7 + 4 *(batiment.getNbNiveaux()-1)),
				(short) (4 + 4 *(batiment.getNbNiveaux()-1)), (short) (8 + 4 *(batiment.getNbNiveaux()-1)),
				(short) (7 + 4 *(batiment.getNbNiveaux()-1)), (short) (8 + 4 *(batiment.getNbNiveaux()-1)),
				(short) (5 + 4 *(batiment.getNbNiveaux()-1)), (short) (9 + 4 *(batiment.getNbNiveaux()-1)),
				(short) (6 + 4 *(batiment.getNbNiveaux()-1)), (short) (9 + 4 *(batiment.getNbNiveaux()-1)),
				(short) (8 + 4 *(batiment.getNbNiveaux()-1)), (short) (9 + 4 *(batiment.getNbNiveaux()-1))
			};
			
			ByteBuffer ibb_ = ByteBuffer.allocateDirect(lineIndexToit.length * 2);
			ibb_.order(ByteOrder.nativeOrder());
			indexBufferToit = ibb_.asShortBuffer();
			indexBufferToit.put(lineIndexToit);
			indexBufferToit.position(0);
		}
		
		for (int i = 1 ; i < batiment.getNbNiveaux() ; ++i) {
			lineIndexes[i] = new short[] {
				(short) (4+(4*i)), (short) (5+(4*i)),
				(short) (5+(4*i)), (short) (6+(4*i)),
				(short) (6+(4*i)), (short) (7+(4*i)),
				(short) (7+(4*i)), (short) (4+(4*i))
			};
			
			ByteBuffer ibb = ByteBuffer.allocateDirect(lineIndexes[i].length * 2);	// short has 2 bytes
			ibb.order(ByteOrder.nativeOrder());
			indexBuffer[i] = ibb.asShortBuffer();
			indexBuffer[i].put(lineIndexes[i]);
			indexBuffer[i].position(0);
		}
	}
	
	private void generateTabPoints() {
		int toit = 0;
		if (batiment.typePenteToit == "Pt")
			toit = 2;
		tabPoints = new float[(batiment.getNbNiveaux()+1) * 4 * 3 + toit * 3];
		final float hauteurEtage = (float) 1/batiment.getNbNiveaux();
		
		float tmp = (batiment.profondeur / batiment.largeur);
		if (tmp > 0.8f)
			tmp = 0.8f;
		final float x = tmp;
		final float z = 0.5f;
		
		for (int i = 0 ; i < batiment.getNbNiveaux()+1 ; ++i) {
			int j = 0, k = i*4*3;
			float y = (float) i * hauteurEtage;
			
			tabPoints[k + j++] = -x;
			tabPoints[k + j++] = y;	// 0
			tabPoints[k + j++] = -z;
			
			tabPoints[k + j++] = x;
			tabPoints[k + j++] = y;	// 1
			tabPoints[k + j++] = -z;
			
			tabPoints[k + j++] = x;
			tabPoints[k + j++] = y;	// 2
			tabPoints[k + j++] = z;
			
			tabPoints[k + j++] = -x;
			tabPoints[k + j++] = y;	// 3
			tabPoints[k + j++] = z;
			
			if (i < batiment.getNbNiveaux()) {
				for (int code = 0 ; code < batiment.niveaux.elementAt(i).codes.size() ; ++code) {
					codeEtares.add(new Etare3D(batiment.niveaux.elementAt(i).codes.elementAt(code)));
					
					float l = hauteurEtage;
					if (l > 0.2f)
						l = 0.2f;
					codeEtares.lastElement().generate(-((x+0.02f)+(l+0.02f)*code), y, -z, l);
					
					
					/*
					codeEtares[i] = new Etare3D(batiment.niveaux.elementAt(i).codes.elementAt(code));
					
					float l = hauteurEtage;
					if (l > 0.2f)
						l = 0.2f;
					codeEtares[i].generate(-((x+0.02f)+(l+0.02f)*code), y, -z, l);
					*/
				}
			}
		}
		
		if (batiment.typePenteToit == "Pt") {
			final int i = (batiment.getNbNiveaux()+1) * 4 * 3;
			int j = 0;
			tabPoints[i + j++] = -x;
			tabPoints[i + j++] = 1.25f;
			tabPoints[i + j++] = z;
			
			tabPoints[i + j++] = x;
			tabPoints[i + j++] = 1.25f;
			tabPoints[i + j++] = z;
		}
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(tabPoints.length * 4);	// float has 4 bytes
	    vbb.order(ByteOrder.nativeOrder());
	    vertexBuffer = vbb.asFloatBuffer();
	    vertexBuffer.put(tabPoints);
	    vertexBuffer.position(0);
	}
}
