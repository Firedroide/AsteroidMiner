package ch.kanti_wohlen.asteroidminer.screen;

import java.util.Iterator;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;
import ch.kanti_wohlen.asteroidminer.Input;
import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.entities.Asteroid;
import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen extends AbstractScreen {
	
	private static final float timeStep = 1 / 60f; // TODO: Allow different maximum frame rates?
	private static final int velocityIterations = 8;
	private static final int positionIterations = 3;
	
	private final Input input;
	private final World world;
	private final SpriteBatch batch;
	private final SpaceShip spaceShip;
	
	private float backgroundU2;
	private float backgroundV2;
	
	public GameScreen(AsteroidMiner game) {
		input = game.getInput();
		world = new World(new Vector2(0, 0), true);
		batch = game.getSpriteBatch();
		
		spaceShip = new SpaceShip(world);
		new Asteroid(world, new Vector2(1000f, 1000f), 100);
	}
	
	@Override
	public void render(float delta) {
		renderGame();
		
		// Process input
		input.onGameRunning(spaceShip);
		// Do physics
		world.step(timeStep, velocityIterations, positionIterations);
	}
	
	public void renderGame() {
		// Draw background
		batch.draw(Textures.BACKGROUND.getTexture(), 0f, 0f, width, height, 0f, 0f, backgroundU2, backgroundV2);
		
		Iterator<Body> bodies = world.getBodies();
		while (bodies.hasNext()) {
			Body body = bodies.next();
			Object o = body.getUserData();
			if (!(o instanceof Entity)) return;
			
			Entity e = (Entity) body.getUserData();
			if (e.isRemoved()) {
				bodies.remove();
			} else {
				e.render(batch);
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		backgroundU2 = width / Textures.BACKGROUND.getWidth();
		backgroundV2 = height / Textures.BACKGROUND.getHeight();
	}
	
	@Override
	public void show() {
		super.show();
		backgroundU2 = width / Textures.BACKGROUND.getWidth();
		backgroundV2 = height / Textures.BACKGROUND.getHeight();
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
