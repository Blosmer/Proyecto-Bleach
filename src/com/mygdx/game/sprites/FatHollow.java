package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ProyectoFinal;

public class FatHollow extends Enemy {
	public enum State {
		MOVING, STANDING, HITTING, DEFENDING
	};

	public State currentState;
	public State previousState;

	private BodyDef bdef;
	private float posX, posY;
	
	private Texture enemyTexture;
	private Animation enemyStand, enemyMove, enemyAttack, enemyShell;

	private float stateTimer;
	private boolean movingRight;
	private boolean canAttack;
	private boolean destroyed;
	private boolean setToDestroy;
	
	private int hitPoints;

	public FatHollow(World world, float posX, float posY) {
		super(world, posX, posY);
		this.posX = posX;
		this.posY = posY;
		enemyTexture = new Texture("sprites/TurtleHollowAnimation.png");
		hitPoints = 4;
		movingRight = false;
		canAttack = true;

		loadAnimations();
		defineEnemy();

		setBounds(0, 0, 90 / ProyectoFinal.PPM, 80 / ProyectoFinal.PPM);
		setSize(60 / ProyectoFinal.PPM, 50 / ProyectoFinal.PPM);
		setScale(1);
	}

	public void update(float dt, Player player) {
		comportamiento(player);

		if (movingRight) {
			setPosition(b2body.getPosition().x - 33 / ProyectoFinal.PPM,
					b2body.getPosition().y - 19 / ProyectoFinal.PPM);
		} else {
			setPosition(b2body.getPosition().x - 28 / ProyectoFinal.PPM,
					b2body.getPosition().y - 19 / ProyectoFinal.PPM);
		}
		setRegion(getFrame(dt));

		if (hitPoints <= 0) {
			setToDestroy();
		}

		if ((setToDestroy) && !destroyed) {
			world.destroyBody(b2body);
			destroyed = true;
		}
	}

	public void setToDestroy() {
		setToDestroy = true;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void comportamiento(Player player) {

		if (Math.abs(player.b2body.getPosition().x - b2body.getPosition().x) < 0.4) {
			currentState = State.HITTING;
			canAttack = false;
		}

		if (Math.abs(player.b2body.getPosition().x - b2body.getPosition().x) < 0.9 && currentState != State.HITTING) {
			// System.out.println(Math.abs(player.b2body.getPosition().x -
			// b2body.getPosition().x));
			if (player.b2body.getPosition().x < b2body.getPosition().x && b2body.getLinearVelocity().x >= -0.06f) {
				b2body.applyLinearImpulse(new Vector2(-0.06f, 0), b2body.getWorldCenter(), true);
			} else if (player.b2body.getPosition().x > b2body.getPosition().x
					&& b2body.getLinearVelocity().x <= 0.06f) {
				b2body.applyLinearImpulse(new Vector2(0.06f, 0), b2body.getWorldCenter(), true);
			}
		}

		// Para que el golpe se produzca en un frame especifico de la
		// animacion
		if (canAttack == false && enemyAttack.getKeyFrameIndex(stateTimer) == 4) {

			if (player.b2body.getPosition().x < b2body.getPosition().x && b2body.getLinearVelocity().x >= -0.06f) {
				b2body.applyLinearImpulse(new Vector2(-0.9f, 0), b2body.getWorldCenter(), true);
			} else if (player.b2body.getPosition().x > b2body.getPosition().x
					&& b2body.getLinearVelocity().x <= 0.06f) {
				b2body.applyLinearImpulse(new Vector2(0.9f, 0), b2body.getWorldCenter(), true);
			}

			canAttack = true;
		}
	}

	@Override
	protected void defineEnemy() {
		bdef = new BodyDef();
		bdef.position.set(posX / ProyectoFinal.PPM, posY / ProyectoFinal.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.fixedRotation = true;
		b2body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();

		Rectangle rectangle = new Rectangle();
		rectangle.setHeight(12 / ProyectoFinal.PPM);
		rectangle.setWidth(14 / ProyectoFinal.PPM);
		PolygonShape head = new PolygonShape();
		rectangle.setX(posX);
		rectangle.setY(posY);

		bdef.position.set((rectangle.getX() - rectangle.getWidth() / 2) / ProyectoFinal.PPM,
				(rectangle.getY() - rectangle.getHeight() / 2) / ProyectoFinal.PPM);
		head.setAsBox(rectangle.getWidth(), rectangle.getHeight());

		fdef.shape = head;
		fdef.restitution = 0f;
		// fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData(this);
	}

	public void loadAnimations() {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 0; i < 7; i++) {
			frames.add(new TextureRegion(enemyTexture, i * 90, 0, 90, 80));
		}
		enemyStand = new Animation(0.2f, frames);
		frames.clear();

		for (int i = 0; i < 6; i++) {
			frames.add(new TextureRegion(enemyTexture, i * 90, 0, 90, 80));
		}
		enemyMove = new Animation(0.2f, frames);
		frames.clear();

		for (int i = 1; i < 9; i++) {
			frames.add(new TextureRegion(enemyTexture, i * 90, 160, 90, 80));
		}
		enemyAttack = new Animation(0.1f, frames);
		frames.clear();

	}

	public State getState() {
		if (b2body.getLinearVelocity().x != 0) {
			return State.MOVING;
		} else {
			return State.STANDING;
		}
	}

	public TextureRegion getFrame(float dt) {
		TextureRegion region = null;
		if (currentState != State.HITTING) {
			currentState = getState();
		}

		switch (currentState) {
		case MOVING:
			region = (TextureRegion) enemyMove.getKeyFrame(stateTimer, true);
			break;
		case STANDING:
			region = (TextureRegion) enemyStand.getKeyFrame(stateTimer, true);
			break;
		case DEFENDING:
			// region = (TextureRegion) enemyShell.getKeyFrame(stateTimer,
			// true);
			break;
		case HITTING:
			region = (TextureRegion) enemyAttack.getKeyFrame(stateTimer);
			if (enemyAttack.isAnimationFinished(stateTimer)) {
				currentState = State.STANDING;
				stateTimer = 0;
			}
			break;
		}

		if ((b2body.getLinearVelocity().x < 0 || !movingRight) && !region.isFlipX()) {
			region.flip(true, false);
			movingRight = false;
		} else if ((b2body.getLinearVelocity().x > 0 || movingRight) && region.isFlipX()) {
			region.flip(true, false);
			movingRight = true;
		}

		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
	}

	public void onTouch(Player player) {
		if (player.getHurtState() == false) {
			if (player.b2body.getPosition().x <= b2body.getPosition().x) {
				player.b2body.setLinearVelocity(0, 0);
				player.b2body.applyLinearImpulse(new Vector2(-1f, 2f), b2body.getWorldCenter(), true);
			} else {
				player.b2body.setLinearVelocity(0, 0);
				player.b2body.applyLinearImpulse(new Vector2(1f, 2f), b2body.getWorldCenter(), true);
			}
			player.onGetHurt(1);
		}
	}

	public void onGetHurt(int damage) {
		hitPoints = hitPoints - damage;
		b2body.setLinearVelocity(0, 0);
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}

}
