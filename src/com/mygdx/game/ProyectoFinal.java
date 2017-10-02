//Pablo Martinez Carreres

package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.SplashScreen;

public class ProyectoFinal extends Game {
	public static SpriteBatch batch;
	public static final int V_WIDTH = 600;
	public static final int V_HEIGHT = 400;
	public static final float PPM = 100;
	public static AssetManager assetManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		
		// screen = new TestScreen(this);
		screen = new SplashScreen(this);
		// screen = new StartMenu(this);
		setScreen(screen);
	}

	public static SpriteBatch getBatch() {
		return batch;
	}

	public static void setBatch(SpriteBatch batch) {
		ProyectoFinal.batch = batch;
	}

}
