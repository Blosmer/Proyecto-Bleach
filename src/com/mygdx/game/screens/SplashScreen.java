package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.mygdx.game.ProyectoFinal;

public class SplashScreen extends AbstractScreen {

	private Texture splashTexture;
	private Music music;

	public SplashScreen(ProyectoFinal game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();
		splashTexture = new Texture("sprites/bleach_splash.jpg");
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		cargarMusica();
	}
	
	private void cargarMusica() {
		music = Gdx.audio.newMusic(Gdx.files.internal("sounds/Bleach Opening - 1.mp3"));
		music.setVolume(0.5f);
		music.play();
		music.setLooping(true);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		game.batch.draw(splashTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen(new TestScreen(game));
		}
	}

	@Override
	public void resize(int width, int height) {
		game.batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
		splashTexture.dispose();
	}

}
