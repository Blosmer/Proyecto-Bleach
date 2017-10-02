package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ProyectoFinal;
import com.mygdx.game.tools.MapBodyBuilder;

public class Level1 extends AbstractScreen{

	private OrthographicCamera gameCam;
	private Viewport gamePort;
	
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	
	private World world;
	private Box2DDebugRenderer b2dr;
	
	public Level1(ProyectoFinal game) {
		super(game);
		
		gameCam = new OrthographicCamera();
		world = new World(new Vector2(0, 0), true);
		
		gamePort = new FitViewport(ProyectoFinal.V_WIDTH, ProyectoFinal.V_HEIGHT, gameCam);

		mapLoader = new TmxMapLoader();
		map = mapLoader.load("maps/BoboboOnIce.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);
		
		// Crear Obstaculos de la capa Obstaculos de tipo Objeto
		MapBodyBuilder.crearBodies(map, world);
		
	}
	
	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//renderer.render(capasInferiores);
		
		b2dr.render(world, gameCam.combined);

		game.batch.setProjectionMatrix(gameCam.combined);
		game.batch.begin();

		game.batch.end();
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

	@Override
	public void dispose() {
		map.dispose();
	}
}
