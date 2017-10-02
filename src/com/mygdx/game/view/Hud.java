package com.mygdx.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ProyectoFinal;
import com.mygdx.game.sprites.Player;

public class Hud implements Disposable {

	private SpriteBatch sb;
	public Stage stage;
	private Viewport viewport;
	private Texture playerTexture;
	private TextureRegion playerTextureR;
	private Texture lifeBar;

	private Image playerIcon;
	private float playerLife;

	public Hud(SpriteBatch sb) {
		this.sb = sb;
		Rectangle rectangle = new Rectangle();
		rectangle.setWidth(10 / ProyectoFinal.PPM);
		rectangle.setHeight(2 / ProyectoFinal.PPM);
		rectangle.setX(0);
		rectangle.setY(300);

		lifeBar = new Texture("sprites/red.png");
		playerTexture = new Texture("sprites/Ichigo_Animations.png");
		playerTextureR = new TextureRegion(playerTexture, 990, 0, 110, 110);
		playerIcon = new Image(playerTextureR);
		playerIcon.scaleBy(0.4f);
		playerIcon.setPosition(0, 300);

		viewport = new FitViewport(ProyectoFinal.V_WIDTH, ProyectoFinal.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, sb);

		stage.addActor(playerIcon);
		// stage.addActor();

	}

	public void update(float dt, Player player) {
		playerLife = player.getHitPoints();
		
//		sb.begin();
//		sb.draw(lifeBar, 100, 100, 100 * playerLife, 100);
//		sb.end();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
