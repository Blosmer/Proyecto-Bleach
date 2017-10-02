package com.mygdx.game.screens;

import com.badlogic.gdx.Screen;
import com.mygdx.game.ProyectoFinal;

public abstract class AbstractScreen implements Screen {
	protected ProyectoFinal game;

	public AbstractScreen(ProyectoFinal game) {
		this.game = game;
	}

	@Override
	public void show() {
	}

	public void update(float dt) {
	}

	@Override
	public void render(float delta) {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		game.batch.dispose();
	}
}
