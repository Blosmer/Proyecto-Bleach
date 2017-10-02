package com.mygdx.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.sprites.Player;
import com.mygdx.game.sprites.Player.State;

public class PlayerControls {

	public PlayerControls() {

	}

	public void update(float dt, Player player) {
		if (!player.isHurt()) {
			// Saltar
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.b2body.getLinearVelocity().y == 0) {
				player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
			}

			// Correr a la derecha con limitador de velocidad, y frenada si ataca
			if (Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 1
					&& player.currentState != State.HITTING && player.currentState != State.BLADESHOOTING) {
				player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
			}
			// Adivina
			if (Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -1
					&& player.currentState != State.HITTING && player.currentState != State.BLADESHOOTING) {
				player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
			}

			// Con esto elimino la inercia al soltar la tecla, pero solo cuando
			// anda, no en el aire
			if (!Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)
					&& player.b2body.getLinearVelocity().y == 0 && player.currentState != State.DASHING) {
				player.b2body.setLinearVelocity(0, 0);
			}

			// El botoncico de las tortas, que aun no hace nada excepto la animación
			if (Gdx.input.isKeyJustPressed(Input.Keys.K) && player.currentState != State.HITTING
					&& player.currentState != State.DASHING && player.currentState != State.BLADESHOOTING) {
				// A 0 para asegurar que empieza la animacion desde el principio
				player.setStateTimer(0);
				player.currentState = State.HITTING;
			}

			// Ataque a distancia, este si funciona
			if (Gdx.input.isKeyJustPressed(Input.Keys.J) && player.currentState != State.HITTING
					&& player.currentState != State.DASHING && player.currentState != State.BLADESHOOTING) {
				// A 0 para asegurar que empieza la animacion desde el principio
				player.setStateTimer(0);
				player.currentState = State.BLADESHOOTING;
				player.setCanShoot(false);

			}

			if (Gdx.input.isKeyJustPressed(Input.Keys.L) && player.currentState != State.DASHING
					&& player.isDashCharged() && player.currentState != State.BLADESHOOTING) {
				// A 0 para asegurar que empieza la animacion desde el principio
				player.setStateTimer(0);
				player.currentState = State.DASHING;
				player.setDashCharged(false);

				player.b2body.setLinearVelocity(0, 0);

				player.b2body.applyLinearImpulse(new Vector2(player.isRunningRight() ? 4f : -4f, 0),
						player.b2body.getWorldCenter(), true);
			}
		}
	}
}
