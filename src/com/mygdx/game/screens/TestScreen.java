package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.view.Hud;
import com.mygdx.game.ProyectoFinal;
import com.mygdx.game.sprites.FatHollow;
import com.mygdx.game.sprites.Player;
import com.mygdx.game.tools.PlayerControls;
import com.mygdx.game.tools.MapBodyBuilder;
import com.mygdx.game.tools.WorldContactListener;

public class TestScreen extends AbstractScreen {

	private OrthographicCamera gameCam;
	private Viewport gamePort;

	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private Hud hud;

	private World world;
	private Box2DDebugRenderer b2dr;
	private PlayerControls controls;

	private int[] capasInferiores;
	private int[] capasSuperiores;

	private Player player;
	private Array<FatHollow> fatHollowList;

	public TestScreen(ProyectoFinal game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();
		gameCam = new OrthographicCamera();
		world = new World(new Vector2(0, -10), true);

		gamePort = new FitViewport(ProyectoFinal.V_WIDTH / ProyectoFinal.PPM,
				ProyectoFinal.V_HEIGHT / ProyectoFinal.PPM, gameCam);

		mapLoader = new TmxMapLoader();
		map = mapLoader.load("maps/testMap2.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / ProyectoFinal.PPM);

		gameCam.position.set(gamePort.getWorldWidth() / 2, (float) (gamePort.getWorldHeight() / 2 - 1.2), 0);
		gameCam.zoom = (float) 0.4;

		capasInferiores = new int[1];
		capasInferiores[0] = 0;
		// capasInferiores[1] = 1;

		// capasSuperiores = new int[1];
		// capasSuperiores[0] = 2;
		// capasSuperiores[1] = 3;

		b2dr = new Box2DDebugRenderer();

		// Crear Obstaculos de la capa Obstaculos de tipo Objeto
		MapBodyBuilder.crearBodies(map, world);
		player = new Player(world, 50, 58);
		hud = new Hud(game.batch);
		controls = new PlayerControls();

		fatHollowList = new Array<FatHollow>();
		fatHollowList.add(new FatHollow(world, 130, 58));
		fatHollowList.add(new FatHollow(world, 250, 78));
		fatHollowList.add(new FatHollow(world, 450, 78));

		world.setContactListener(new WorldContactListener());

	}

	public void update(float dt) {
		controls.update(dt, player);
		world.step(1 / 60f, 6, 2);
		player.update(dt);
		if (player.getHitPoints() <= 0) {
			game.setScreen(new GameOverScreen((ProyectoFinal) game));
		}

		for (FatHollow fatHollow : fatHollowList) {
			fatHollow.update(dt, player);
			if (fatHollow.isDestroyed())
				fatHollowList.removeValue(fatHollow, true);
		}

		gameCam.position.x = player.b2body.getPosition().x;

		gameCam.update();
		renderer.setView(gameCam);
		hud.update(dt, player);
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(30 / 255f, 50 / 255f, 235 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.render(capasInferiores);

		// Con esto se muestran las formas fisicas
		// No te olvides de desactivarlo para las demostraciones
		//b2dr.render(world, gameCam.combined);

		game.batch.setProjectionMatrix(gameCam.combined);
		game.batch.begin();

		player.draw(game.getBatch());

		for (FatHollow fatHollow : fatHollowList) {
			fatHollow.draw(game.getBatch());
		}

		game.batch.end();

		// renderer.render(capasSuperiores);
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
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

	public World getWorld() {
		return world;
	}

	public TiledMap getMap() {
		return map;
	}

	@Override
	public void dispose() {
		map.dispose();
	}
}
