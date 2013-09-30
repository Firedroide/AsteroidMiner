package ch.kanti_wohlen.asteroidminer.screen;

import java.util.Iterator;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;
import ch.kanti_wohlen.asteroidminer.entities.Entity;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen implements Screen {
	
	private static final float timeStep = 1 / 60f; // TODO: Allow different maximum frame rates?
	private static final int velocityIterations = 8;
	private static final int positionIterations = 3;
	
	private final World world;
	private SpriteBatch batch;
	
	public GameScreen(AsteroidMiner game) {
		world = new World(new Vector2(0, 0), true);
		batch = game.getSpriteBatch();
	}
	
	@Override
	public void render(float delta) {
		Iterator<Body> bodies = world.getBodies();
		while (bodies.hasNext()) {
			Body body = bodies.next();
			Object o = body.getUserData();
			if (!(o instanceof Entity)) return;
			
			Entity e = (Entity) body.getUserData();
			if (e.isRemoved()) {
				bodies.remove();
			} else {
				if (e.getFriction() > 0f) {
					body.setLinearVelocity(body.getLinearVelocity().mul(1f - e.getFriction()));
				}
				
				e.render(batch);
			}
		}
		
		world.step(timeStep, velocityIterations, positionIterations);
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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
