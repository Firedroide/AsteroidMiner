package ch.kanti_wohlen.asteroidminer.screen;

import java.util.ArrayList;
import java.util.List;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;
import ch.kanti_wohlen.asteroidminer.CollisionListener;
import ch.kanti_wohlen.asteroidminer.LocalPlayer;
import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.TaskScheduler;
import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.entities.*;
import ch.kanti_wohlen.asteroidminer.entities.asteroids.*;
import ch.kanti_wohlen.asteroidminer.powerups.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;

public class GameScreen {

	public static final float WORLD_SIZE = 160f;

	private static final float timeStep = 1 / 60f; // TODO: Allow different maximum frame rates?
	private static final int velocityIterations = 8;
	private static final int positionIterations = 3;

	private final AsteroidMiner main;
	private final OrthographicCamera camera;
	private final World world;
	private final SpriteBatch batch;
	private final TaskScheduler scheduler;
	private final List<Player> players;

	private LocalPlayer localPlayer;
	private boolean running;

	// Temp
	private int counter = 0;

	public GameScreen(AsteroidMiner asteroidMiner) {
		main = asteroidMiner;

		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		camera.position.set(0f, 0f, 0f);

		players = new ArrayList<Player>();
		world = new World(new Vector2(0, 0), true);
		CollisionListener cl = new CollisionListener();
		world.setContactListener(cl);
		batch = main.getSpriteBatch();
		scheduler = TaskScheduler.INSTANCE;

		WorldBorder.addBorders(world);

		new IceAsteroid(world, new Vector2(20, 20), Textures.ASTEROID.getHeight() * 0.05f, new Vector2(-2, -2));
		new StoneAsteroid(world, new Vector2(50, 30), Textures.ASTEROID.getHeight() * 0.05f, new Vector2(0, -2));
		new StoneAsteroid(world, new Vector2(40, 60), Textures.ASTEROID.getHeight() * 0.05f, new Vector2(-2, 0));

		new HealthPowerUp(world, new Vector2(10, 40));
	}

	public void startGame() {
		localPlayer = new LocalPlayer(main, world);
		players.add(localPlayer);
		Gdx.input.setInputProcessor(localPlayer.getInput());

		running = true;
	}

	public void stopGame() {
		running = false;
	}

	public void tick(float delta) {
		if (running) moveCamera();

		// Temp
		counter += 1;
		counter %= 60;
		if (counter == 0) {
			Gdx.app.log("World", (world.getBodyCount() - 2) + " LAZORS!");
			if (localPlayer != null) {
				Vector2 pos = localPlayer.getSpaceShip().getPhysicsBody().getPosition();
				Gdx.app.log("SpaceShip", "Location: " + pos.toString());
			}
		}

		// Process input
		for (Player p : players) {
			p.doInput();
		}

		// Do physics
		world.step(timeStep, velocityIterations, positionIterations);
		applyGravity();

		scheduler.onGameTick();
	}

	public void render() {
		// Draw background
		renderBackground();

		Array<Body> bodies = new Array<Body>(world.getBodyCount());
		world.getBodies(bodies);
		ArrayIterator<Body> i = new ArrayIterator<Body>(bodies, true);
		ArrayList<Entity> renderLater = new ArrayList<Entity>();

		batch.begin();
		camera.apply(Gdx.gl11);
		while (i.hasNext()) {
			Body body = i.next();
			if (body == null) continue;
			Entity e = (Entity) body.getUserData();

			if (e.isRemoved()) {
				Gdx.app.log("DEBUG", "Removed body " + body.getUserData().toString());
				i.remove();
				world.destroyBody(body);
				body.setUserData(null);
			} else {
				if (e.getType() == EntityType.WORLD_BORDER) {
					renderLater.add(e);
				} else {
					e.render(batch);
				}
			}
		}

		for (Entity e : renderLater) {
			e.render(batch);
		}
		batch.end();
	}

	private void renderBackground() {
		// Make background move slower
		float fx = camera.position.x * 0.2f;
		float fy = camera.position.y * 0.2f;
		// And repeat background graphic
		fx = fx % Gdx.graphics.getWidth();
		fy = fy % Gdx.graphics.getHeight();
		if (fx < 0) fx += Gdx.graphics.getWidth();
		if (fy < 0) fy += Gdx.graphics.getHeight();
		fx -= Gdx.graphics.getWidth() / 2f;
		fy -= Gdx.graphics.getHeight() / 2f;
		// Then set position to be inside the camera's focus
		fx = camera.position.x - fx;
		fy = camera.position.y - fy;

		final int width = Gdx.graphics.getWidth();
		final int height = Gdx.graphics.getHeight();
		final float u2 = width / Textures.BACKGROUND.getWidth();
		final float v2 = height / Textures.BACKGROUND.getHeight();

		batch.disableBlending();
		batch.begin();
		camera.update(false);
		camera.apply(Gdx.gl11);
		batch.draw(Textures.BACKGROUND.getTexture(), fx - width, fy - height, width, height, 0f, 0f, u2, v2);
		batch.draw(Textures.BACKGROUND.getTexture(), fx - width, fy, width, height, 0f, 0f, u2, v2);
		batch.draw(Textures.BACKGROUND.getTexture(), fx, fy - height, width, height, 0f, 0f, u2, v2);
		batch.draw(Textures.BACKGROUND.getTexture(), fx, fy, width, height, 0f, 0f, u2, v2);
		batch.end();
		batch.enableBlending();
	}

	private void moveCamera() {
		// Get the spaceship's current distance from the center of the screen
		final Vector2 movement = new Vector2(localPlayer.getSpaceShip().getPhysicsBody().getPosition().scl(10f));
		movement.sub(camera.position.x, camera.position.y);

		// Get the ship's distance from the border, keeping the direction
		final float xDir = movement.x < 0 ? -1 : 1;
		final float yDir = movement.y < 0 ? -1 : 1;
		movement.set(Math.abs(movement.x), Math.abs(movement.y));
		movement.sub(Gdx.graphics.getWidth() * 0.2f, Gdx.graphics.getHeight() * 0.15f);
		movement.set(Math.max(movement.x, 0f), Math.max(movement.y, 0f));
		movement.scl(xDir, yDir).scl(0.1f); // TODO: Use Box2DToPixel

		// Apply movement to foreground camera
		camera.position.add(movement.x, movement.y, 0f);
	}

	private void applyGravity() {
		final float G = 0.2f;

		// Update to nightly GDX builds to fix this issue?
		Array<Body> bodies = new Array<Body>(world.getBodyCount());
		world.getBodies(bodies);
		ArrayIterator<Body> outer = new ArrayIterator<Body>(bodies, false);

		for (Body body : outer) {
			if (body == null) continue;

			ArrayIterator<Body> inner = new ArrayIterator<Body>(bodies, false);
			for (Body target : inner) {
				if (target == null) continue;
				if (target.getGravityScale() == 0f) continue;

				Vector2 dir = body.getPosition().cpy().sub(target.getPosition());
				final float dist = dir.len2() + 1;
				dir.nor();
				final float w2 = body.getMass() * target.getMass();
				final float force = G * w2 / dist;

				target.applyForceToCenter(dir.scl(force).scl(target.getGravityScale()), true);
			}
		}
	}

	public Player[] getPlayers() {
		return players.toArray(new Player[players.size()]);
	}

	public Player getLocalPlayer() {
		return localPlayer;
	}

	public void dispose() {
		for (Player player : players) {
			player.dispose();
		}
		scheduler.dispose();
		world.dispose();
	}
}
