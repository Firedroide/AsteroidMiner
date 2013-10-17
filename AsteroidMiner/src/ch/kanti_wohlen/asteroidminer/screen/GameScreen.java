package ch.kanti_wohlen.asteroidminer.screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;
import ch.kanti_wohlen.asteroidminer.CollisionListener;
import ch.kanti_wohlen.asteroidminer.Input;
import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.entities.Asteroid;
import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen extends AbstractScreen {
	
	public static final float WORLD_SIZE = 200f;
	
	private static final float timeStep = 1 / 60f; // TODO: Allow different maximum frame rates?
	private static final int velocityIterations = 8;
	private static final int positionIterations = 3;
	
	private final OrthographicCamera foregroundCamera;
	private final Input input;
	private final World world;
	private final SpriteBatch batch;
	private final SpaceShip spaceShip;
	
	private float backgroundU2;
	private float backgroundV2;
	
	// Temp
	private int counter = 0;
	
	public GameScreen(AsteroidMiner game) {
		foregroundCamera = new OrthographicCamera();
		foregroundCamera.setToOrtho(false);
		foregroundCamera.position.set(0f, 0f, 0f);
		
		input = game.getInput();
		world = new World(new Vector2(0, 0), true);
		CollisionListener cl = new CollisionListener();
		world.setContactFilter(cl);
		batch = game.getSpriteBatch();
		
		spaceShip = new SpaceShip(world);
		new Asteroid(world, new Vector2(20, 20), Textures.ASTEROID.getHeight() * 0.05f);
		new Asteroid(world, new Vector2(50, 30), Textures.ASTEROID.getHeight() * 0.05f);
		new Asteroid(world, new Vector2(40, 60), Textures.ASTEROID.getHeight() * 0.05f);
	}
	
	@Override
	public void render(float delta) {
		moveCamera();
		renderGame();
		
		// Temp
		counter += 1;
		counter %= 60;
		if (counter == 0) {
			Gdx.app.log("World", (world.getBodyCount() - 2) + " LAZORS!");
		}
		
		// Process input
		input.onGameRunning(spaceShip);
		// Do physics
		world.step(timeStep, velocityIterations, positionIterations);
		applyGravity();
	}
	
	private void applyGravity() {
		final float G = 10f;
		
		// Update to nightly GDX builds to fix this issue?
		List<Body> bodies = new ArrayList<Body>();
		Iterator<Body> bodyIterator = world.getBodies();
		while (bodyIterator.hasNext()) {
			Body body = bodyIterator.next();
			if (body != null) {
				bodies.add(body);
			}
		}
		
		for (Body body : bodies) {
			if (body == null) continue;
			
			for (Body target : bodies) {
				if (target == null) continue;
				if (target.getGravityScale() == 0f) continue;
				
				Vector2 dir = body.getPosition().cpy().sub(target.getPosition());
				final float dist = dir.len2() + 1;
				dir.nor();
				final float w2 = body.getMass() * target.getMass();
				final float force = G * w2 / dist;
				
				target.applyForceToCenter(dir.mul(force).mul(target.getGravityScale()));
			}
		}
	}
	
	public void renderGame() {
		// Draw background
		renderBackground();
		
		Iterator<Body> bodies = world.getBodies();
		while (bodies.hasNext()) {
			Body body = bodies.next();
			if (body == null) continue;
			Entity e = (Entity) body.getUserData();
			
			if (e.isRemoved()) {
				bodies.remove();
				world.destroyBody(body);
				body.setUserData(null);
			} else {
				e.render(batch);
			}
		}
	}
	
	public void renderBackground() {
		// Make background move slower
		float fx = foregroundCamera.position.x * 0.2f;
		float fy = foregroundCamera.position.y * 0.2f;
		// And repeat background graphic
		fx = fx % Gdx.graphics.getWidth();
		fy = fy % Gdx.graphics.getHeight();
		if (fx < 0) fx += Gdx.graphics.getWidth();
		if (fy < 0) fy += Gdx.graphics.getHeight();
		fx -= Gdx.graphics.getWidth() / 2f;
		fy -= Gdx.graphics.getHeight() / 2f;
		// Then set position to be inside the camera's focus
		fx = foregroundCamera.position.x - fx;
		fy = foregroundCamera.position.y - fy;
		
		batch.draw(Textures.BACKGROUND.getTexture(), fx - width, fy - height, width, height, 0f, 0f, backgroundU2, backgroundV2);
		batch.draw(Textures.BACKGROUND.getTexture(), fx - width, fy, width, height, 0f, 0f, backgroundU2, backgroundV2);
		batch.draw(Textures.BACKGROUND.getTexture(), fx, fy - height, width, height, 0f, 0f, backgroundU2, backgroundV2);
		batch.draw(Textures.BACKGROUND.getTexture(), fx, fy, width, height, 0f, 0f, backgroundU2, backgroundV2);
	}
	
	private void moveCamera() {
		// Get the spaceship's current distance from the center of the screen
		final Vector2 movement = new Vector2(spaceShip.getPhysicsBody().getPosition().mul(10f));
		movement.sub(foregroundCamera.position.x, foregroundCamera.position.y);
		
		// Get the ship's distance from the border, keeping the direction
		final float xDir = movement.x < 0 ? -1 : 1;
		final float yDir = movement.y < 0 ? -1 : 1;
		movement.set(Math.abs(movement.x), Math.abs(movement.y));
		movement.sub(Gdx.graphics.getWidth() * 0.2f, Gdx.graphics.getHeight() * 0.15f);
		movement.set(Math.max(movement.x, 0f), Math.max(movement.y, 0f));
		movement.mul(xDir, yDir).mul(0.1f); // TODO: Use Box2DToPixel
		
		// Apply movement to foreground camera
		foregroundCamera.position.add(movement.x, movement.y, 0f);
		foregroundCamera.update(false);
		foregroundCamera.apply(Gdx.gl11);
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
	
	public SpaceShip getSpaceShip() {
		// Somewhat temporary.
		return spaceShip;
	}
}
