package com.mygdx.game.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;

public class EllipseShape {

	public EllipseShape(){

	}
	
	public ChainShape createEllipse(float width, float height) {
		return createEllipse(width, height, 64);
	}

	// STEPS es el numero de puntos de la elipse, cuantos más puntos, la forma es más
	// redondeada, pero pesada de generar
	public ChainShape createEllipse(float width, float height, int STEPS) {
		Vector2[] verts = new Vector2[STEPS];

		for (int i = 0; i < STEPS; i++) {
			float t = (float) (i * 2 * Math.PI) / STEPS;
			verts[i] = new Vector2(width * (float) Math.cos(t), height * (float) Math.sin(t));
		}

		ChainShape ellipse = new ChainShape();
		ellipse.createChain(verts);
		return ellipse;
	}
	
}
