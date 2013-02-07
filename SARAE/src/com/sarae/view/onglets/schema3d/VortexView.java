package com.sarae.view.onglets.schema3d;

import com.sarae.model.Batiment;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class VortexView extends GLSurfaceView {
	private VortexRenderer _renderer;
	private float lastX = 0.f;
	
	public VortexView(Context context, AttributeSet attrs, Batiment bat) {
		super(context, attrs);
		
		if (bat.getNbNiveaux() > 0) {
			_renderer = new VortexRenderer(new Batiment3D(bat));
			setRenderer(_renderer);
		}
	}
	
	public boolean onTouchEvent(final MotionEvent event) {
		queueEvent(new Runnable() {
			public void run() {
				//_renderer.setColor(event.getX() / getWidth(), event.getY() / getHeight(), 1.0f);
				float currentX = event.getX();
				
				_renderer.setAngle(event.getX() / 2);	// Bla bla bla
				
				lastX = currentX;
			}
		});
		return true;
	}
}
