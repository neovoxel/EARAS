package com.sarae.view.onglets.schema3d;

import com.sarae.model.Batiment;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class VortexView extends GLSurfaceView {
	private VortexRenderer _renderer;
	
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
				_renderer.setAngle(event.getX() / 2);
			}
		});
		return true;
	}
	
	@Override public void onPause() {
		super.onPause();
	}
	
	@Override public void onResume() {
		super.onResume();
	}
}
