package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.screens.AbstractScreen;

public abstract class Enemy extends Sprite{
	protected World world;
	protected AbstractScreen screen;
	public Body b2body;

	public Enemy(World world, float posX, float posY) {
		this.world = world;
		setPosition(posX, posY);
	}
	
	protected abstract void defineEnemy();

}
