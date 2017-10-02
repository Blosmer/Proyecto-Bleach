package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.ProyectoFinal;

public class StartMenu extends AbstractScreen{

	private Skin skin;
	
	public StartMenu(ProyectoFinal game) {
		super(game);

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		skin = new Skin(Gdx.files.internal("files/menu.txt"));
		
		Table table = new Table(getSkin());
		table.setWidth(width);
		table.setHeight(height);
		
	}

	public Skin getSkin(){
		return skin;
	}
}
