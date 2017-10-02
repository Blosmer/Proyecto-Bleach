package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ProyectoFinal;
import com.mygdx.game.tools.EllipseShape;

public class BladeShot extends Sprite {

	private World world;
	private Animation shotAnimation;
	float stateTime;
	boolean destroyed;
	boolean setToDestroy;
	boolean shootRight;
	private Body b2body;
	private Texture playerTexture;
	private TextureRegion region;
	private EllipseShape ellipse;
	private float x, y;

	public BladeShot(World world, float x, float y, boolean shootRight) {
		this.shootRight = shootRight;
		this.world = world;
		this.x = x;
		this.y = y;

		ellipse = new EllipseShape();
		playerTexture = new Texture("sprites/Ichigo_Animations.png");

		loadAnimations();
		defineBladeShot();

		setBounds(0, 0, 110 / ProyectoFinal.PPM, 110 / ProyectoFinal.PPM);
		setSize(50 / ProyectoFinal.PPM, 50 / ProyectoFinal.PPM);
		setScale(1);
	}

	public void update(float dt) {
		// Con esto mantiene su velocidad sin caer por la gravedad
		b2body.setLinearVelocity(b2body.getLinearVelocity().x, 0);

		stateTime += dt;
		region = ((TextureRegion) shotAnimation.getKeyFrame(stateTime, true));

		if ((b2body.getLinearVelocity().x < 0 || !shootRight) && !region.isFlipX()) {
			region.flip(true, false);
		} else if ((b2body.getLinearVelocity().x > 0 || shootRight) && region.isFlipX()) {
			region.flip(true, false);
		}
		setRegion(region);

		if (shootRight) {
			setPosition(b2body.getPosition().x - getWidth() / 2 - 15 / ProyectoFinal.PPM,
					b2body.getPosition().y - getHeight() / 2);
		} else {
			setPosition(b2body.getPosition().x - getWidth() / 2 + 15 / ProyectoFinal.PPM,
					b2body.getPosition().y - getHeight() / 2);
		}

		if ((shootRight && b2body.getLinearVelocity().x < 2f) || (!shootRight && b2body.getLinearVelocity().x > -2f)) {
			setToDestroy();
		}
		if ((stateTime > 3 || setToDestroy) && !destroyed) {
			world.destroyBody(b2body);
			destroyed = true;
		}
	}

	public void loadAnimations() {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(playerTexture, i * 110, 550, 110, 110));
		}
		shotAnimation = new Animation(0.1f, frames);
		frames.clear();
	}

	public void defineBladeShot() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(shootRight ? x + 12 / ProyectoFinal.PPM : x - 12 / ProyectoFinal.PPM, y);
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.fixedRotation = true;
		if (!world.isLocked()) {
			b2body = world.createBody(bdef);
		}

		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(8 / ProyectoFinal.PPM);

		// ChainShape chainShape = ellipse.createEllipse(6 / ProyectoFinal.PPM,
		// 12 / ProyectoFinal.PPM);
		// fdef.shape = chainShape;

		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);

		b2body.setGravityScale(0);
		b2body.setLinearVelocity(new Vector2(shootRight ? 2f : -2f, 0));
	}

	public void setToDestroy() {
		setToDestroy = true;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

}
