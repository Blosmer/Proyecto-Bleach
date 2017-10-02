package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ProyectoFinal;

public class Player extends Sprite {
	public enum State {
		FALLING, JUMPING, RUNNING, STANDING, HITTING, DASHING, BLADESHOOTING, GETTINGHURT
	};

	public State currentState;
	public State previousState;
	public Body b2body;
	public BodyDef bdef;
	public World world;

	private Texture playerTexture;
	private Animation playerStand;
	private Animation playerFall;
	private Animation playerRun;
	private Animation playerJump;
	private Animation playerHitClose;
	private Animation playerBladeShoot;
	private Animation playerDash;
	private Animation playerGetHurt;

	private boolean dashCharged;
	private boolean runningRight;
	private boolean canShoot;
	private boolean hurt;

	private float dashTime;
	private float stateTimer;
	private float hurtTime;
	private int hitPoints;

	private Array<BladeShot> bladeShots;

	public Player(World world, float posX, float posY) {
		super();
		this.world = world;
		currentState = State.STANDING;
		previousState = State.STANDING;
		playerTexture = new Texture("sprites/Ichigo_Animations.png");
		bladeShots = new Array<BladeShot>();
		runningRight = true;
		dashCharged = true;
		canShoot = true;
		hurt = false;
		hitPoints = 10;

		definePlayer(posX, posY);
		loadAnimations();

		setBounds(0, 0, 110 / ProyectoFinal.PPM, 110 / ProyectoFinal.PPM);
		setSize(50 / ProyectoFinal.PPM, 50 / ProyectoFinal.PPM);
		setScale(1);
	}

	public void update(float dt) {
		// El sprite que estoy usando es un poco especialito (por mi culpa), y esto es para
		// ajustarlo dependiendo de a que lado mire
		setPosition(
				runningRight ? b2body.getPosition().x - 20 / ProyectoFinal.PPM
						: b2body.getPosition().x - 30 / ProyectoFinal.PPM,
				b2body.getPosition().y - 14 / ProyectoFinal.PPM);

		setRegion(getFrame(dt));

		// Tiempo de invulnerabilidad tras sufrir daño
		if (hurt) {
			hurtTime += dt;
			if (hurtTime >= 1) {
				hurt = false;
				hurtTime = 0;
			}
		}

		if (dashCharged == false) {
			chargeDash(dt);
		}

		// Para que el disparo se produzca en un frame especifico de la
		// animacion
		if (canShoot == false && playerBladeShoot.getKeyFrameIndex(stateTimer) == 4) {
			shot();
			canShoot = true;
		}

		for (BladeShot shot : bladeShots) {
			shot.update(dt);
			if (shot.isDestroyed())
				bladeShots.removeValue(shot, true);
		}
	}

	public void loadAnimations() {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(playerTexture, i * 110, 330, 110, 110));
		}
		playerJump = new Animation(0.06f, frames);
		frames.clear();

		for (int i = 4; i < 6; i++) {
			frames.add(new TextureRegion(playerTexture, i * 110, 330, 110, 110));
		}
		playerFall = new Animation(0.2f, frames);
		frames.clear();

		for (int i = 0; i < 8; i++) {
			frames.add(new TextureRegion(playerTexture, i * 110, 110, 110, 110));
		}
		playerRun = new Animation(0.1f, frames);
		frames.clear();

		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(playerTexture, i * 110, 0, 110, 110));
		}
		playerStand = new Animation(0.3f, frames);
		frames.clear();

		for (int i = 0; i < 6; i++) {
			frames.add(new TextureRegion(playerTexture, i * 110, 220, 110, 110));
		}
		playerHitClose = new Animation(0.1f, frames);
		frames.clear();

		for (int i = 0; i < 8; i++) {
			frames.add(new TextureRegion(playerTexture, i * 110, 440, 110, 110));
		}
		playerBladeShoot = new Animation(0.09f, frames);
		frames.clear();

		for (int i = 5; i < 7; i++) {
			frames.add(new TextureRegion(playerTexture, i * 110, 0, 110, 110));
		}
		playerDash = new Animation(0.05f, frames);
		frames.clear();

		for (int i = 3; i < 5; i++) {
			frames.add(new TextureRegion(playerTexture, i * 110, 660, 110, 110));
		}
		playerGetHurt = new Animation(0.1f, frames);
		frames.clear();
	}

	public State getState() {
		if (hurt == true) {
			return State.GETTINGHURT;
		} else if (b2body.getLinearVelocity().y > 0) {
			return State.JUMPING;
		} else if (b2body.getLinearVelocity().y < 0) {
			return State.FALLING;
		} else if (b2body.getLinearVelocity().x != 0) {
			return State.RUNNING;
		} else {
			return State.STANDING;
		}
	}

	public TextureRegion getFrame(float dt) {
		TextureRegion region = null;
		
		if (currentState != State.HITTING && currentState != State.DASHING && currentState != State.BLADESHOOTING) {
			currentState = getState();
		}

		switch (currentState) {
		case JUMPING:
			region = (TextureRegion) playerJump.getKeyFrame(stateTimer);
			break;
		case FALLING:
			region = (TextureRegion) playerFall.getKeyFrame(stateTimer);
			break;
		case RUNNING:
			//el true indica que es un bucle
			region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
			break;
		case STANDING:
			region = (TextureRegion) playerStand.getKeyFrame(stateTimer, true);
			break;
		case GETTINGHURT:
			region = (TextureRegion) playerGetHurt.getKeyFrame(stateTimer, true);
			break;
		case HITTING:
			region = (TextureRegion) playerHitClose.getKeyFrame(stateTimer);
			if (playerHitClose.isAnimationFinished(stateTimer)) {
				currentState = State.STANDING;
				stateTimer = 0;
			}
			break;
		case BLADESHOOTING:
			region = (TextureRegion) playerBladeShoot.getKeyFrame(stateTimer);
			if (playerBladeShoot.isAnimationFinished(stateTimer)) {
				currentState = State.STANDING;
				stateTimer = 0;
			}
			break;
		case DASHING:
			region = (TextureRegion) playerDash.getKeyFrame(stateTimer);
			b2body.setLinearVelocity(b2body.getLinearVelocity().x, 0);
			if (playerDash.isAnimationFinished(stateTimer)) {
				currentState = State.STANDING;
				stateTimer = 0;
				b2body.setLinearVelocity(0, 0);
			}
			break;
		}

		if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
			region.flip(true, false);
			runningRight = false;
		} else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
			region.flip(true, false);
			runningRight = true;
		}

		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
	}

	public void definePlayer(float x, float y) {
		bdef = new BodyDef();
		bdef.position.set(x / ProyectoFinal.PPM, y / ProyectoFinal.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		//Se elimina la rotacion
		bdef.fixedRotation = true;
		b2body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();

		Rectangle rectangle = new Rectangle();
		rectangle.setHeight(9 / ProyectoFinal.PPM);
		rectangle.setWidth(5 / ProyectoFinal.PPM);
		PolygonShape head = new PolygonShape();
		rectangle.setX(x);
		rectangle.setY(y);

		bdef.position.set((rectangle.getX() - rectangle.getWidth() / 2) / ProyectoFinal.PPM,
				(rectangle.getY() - rectangle.getHeight() / 2) / ProyectoFinal.PPM);
		head.setAsBox(rectangle.getWidth(), rectangle.getHeight());

		fdef.shape = head;
		//Se elimina la capacidad de rebotar
		fdef.restitution = 0f;
		b2body.createFixture(fdef).setUserData(this);
	}

	// Un pequeño delay para limitar su uso
	public void chargeDash(float dt) {
		dashTime += dt;
		if (dashTime >= 0.9) {
			dashCharged = true;
			dashTime = 0;
		}
	}

	public void shot() {
		bladeShots
				.add(new BladeShot(world, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
	}

	public void draw(Batch batch) {
		super.draw(batch);
		for (BladeShot ball : bladeShots)
			ball.draw(batch);
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(int hitPoints) {
		if (hurt == false) {
			this.hitPoints = hitPoints;
		}
	}

	public void onGetHurt(int damage) {
		if (hurt == false) {
			hitPoints = hitPoints - damage;
		}
		hurt = true;
	}

	public boolean getHurtState() {
		return hurt;
	}

	public float getStateTimer() {
		return stateTimer;
	}

	public void setStateTimer(float stateTimer) {
		this.stateTimer = stateTimer;
	}

	public boolean isDashCharged() {
		return dashCharged;
	}

	public void setDashCharged(boolean dashCharged) {
		this.dashCharged = dashCharged;
	}

	public boolean isRunningRight() {
		return runningRight;
	}

	public void setRunningRight(boolean runningRight) {
		this.runningRight = runningRight;
	}

	public boolean isCanShoot() {
		return canShoot;
	}

	public void setCanShoot(boolean canShoot) {
		this.canShoot = canShoot;
	}

	public boolean isHurt() {
		return hurt;
	}

	public void setHurt(boolean hurt) {
		this.hurt = hurt;
	}
}
