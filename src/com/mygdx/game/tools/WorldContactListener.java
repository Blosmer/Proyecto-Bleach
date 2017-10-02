package com.mygdx.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.sprites.Player;
import com.mygdx.game.sprites.BladeShot;
import com.mygdx.game.sprites.FatHollow;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {

		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		if ((fixA.getUserData() instanceof Player && fixB.getUserData() instanceof FatHollow)
				|| (fixA.getUserData() instanceof FatHollow && fixB.getUserData() instanceof Player)) {

			if (fixA.getUserData() instanceof Player) {
				((FatHollow) fixB.getUserData()).onTouch((Player) fixA.getUserData());
				System.out.println("Vida de Ichigo " + ((Player) fixA.getUserData()).getHitPoints() + "/10");

			} else {
				((FatHollow) fixA.getUserData()).onTouch((Player) fixB.getUserData());
			}

		} else {
			// Gdx.app.log("Begin Contact", "");
		}

		if ((fixA.getUserData() instanceof BladeShot && fixB.getUserData() instanceof FatHollow)
				|| (fixA.getUserData() instanceof FatHollow && fixB.getUserData() instanceof BladeShot)) {

			//Gdx.app.log("Getsuga tensho", "");

			if (fixA.getUserData() instanceof BladeShot) {
				((FatHollow) fixB.getUserData()).onGetHurt(2);
				System.out.println(((Player) fixA.getUserData()).getHitPoints());
			} else {
				((FatHollow) fixA.getUserData()).onGetHurt(2);
			}

		} else {
			// Gdx.app.log("Begin Contact", "");
		}

	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
