package ch.kanti_wohlen.asteroidminer.spawner;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;
import ch.kanti_wohlen.asteroidminer.TaskScheduler;
import ch.kanti_wohlen.asteroidminer.TaskScheduler.Task;
import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.WorldBorder;
import ch.kanti_wohlen.asteroidminer.entities.WorldBorder.BorderSide;
import ch.kanti_wohlen.asteroidminer.entities.asteroids.*;
import ch.kanti_wohlen.asteroidminer.screen.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class TimeAttackAsteroidSpawner extends AsteroidSpawner {

	private static final int START_SPAWNING_AMOUNT = 40;

	private final float gameTime;
	private final float maxTime;
	private Task gameEndTask;
	private float time;

	public TimeAttackAsteroidSpawner(World theWorld, float time) {
		super(theWorld);
		gameTime = time;
		maxTime = time * 0.8f;
	}

	@Override
	public void start() {
		super.start();
		WorldBorder.addBorders(world);
		for (int i = 0; i < START_SPAWNING_AMOUNT; i++) {
			spawn(0f);
		}

		gameEndTask = TaskScheduler.INSTANCE.runTaskLater(new Runnable() {
			@Override
			public void run() {
				AsteroidMiner.INSTANCE.getGameScreen().stopGame();
			}
		}, gameTime);
	}

	@Override
	public void stop() {
		super.stop();
		gameEndTask.cancel();
	}

	@Override
	public void tick() {
		time += TaskScheduler.INSTANCE.TICK_TIME;
		final float difficulty = Math.min(1f, time / maxTime);
		if (MathUtils.random() > tickTimeModifier * (0.05f + 0.1f * difficulty)) return;
		if (world.getBodyCount() < MAXIMUM_ENTITIES) spawn(difficulty);
	}

	public float getCurrentTime() {
		return time;
	}

	public float getTimeLeft() {
		return Math.max(gameTime - time, 0f);
	}

	private void spawn(float difficulty) {
		final float width = GameScreen.WORLD_SIZE + Gdx.graphics.getWidth() * Entity.PIXEL_TO_BOX2D * 0.2f;
		final float height = GameScreen.WORLD_SIZE + Gdx.graphics.getHeight() * Entity.PIXEL_TO_BOX2D * 0.2f;
		final float angle = MathUtils.random(MathUtils.PI2);
		final float speed = MathUtils.random(12f, 20f) + difficulty * 6f;
		final float rotation = MathUtils.random(-0.5f, 0.5f);

		final BorderSide side = chooseSide(angle);
		final Vector2 spawningLocation = getSpawningPosition(side, width, height);
		final Vector2 movementVector = new Vector2(speed * -MathUtils.sin(angle), speed * MathUtils.cos(angle));

		spawnRandomAsteroid(spawningLocation, movementVector, rotation, difficulty);
	}

	private void spawnRandomAsteroid(Vector2 location, Vector2 momentum, float rotation, float difficulty) {
		final float radius = MathUtils.random(1f + difficulty, 3f + 2f * difficulty);
		final float type = MathUtils.random(5f + 8f * difficulty);
		if (!AsteroidAABB.checkAsteroidClipping(world, location, radius)) {
			if (type < 6f) {
				StoneAsteroid stoneAsteroid = new StoneAsteroid(world, location, radius, momentum);
				stoneAsteroid.getPhysicsBody().setAngularVelocity(rotation);
			} else if (type < 9f) {
				IceAsteroid iceAsteroid = new IceAsteroid(world, location, radius, momentum);
				iceAsteroid.getPhysicsBody().setAngularVelocity(rotation);
			} else if (type < 11f) {
				MetalAsteroid metalAsteroid = new MetalAsteroid(world, location, radius, momentum);
				metalAsteroid.getPhysicsBody().setAngularVelocity(rotation);
			} else {
				ExplosiveAsteroid explosiveAsteroid = new ExplosiveAsteroid(world, location, radius, momentum.scl(0.75f));
				explosiveAsteroid.getPhysicsBody().setAngularVelocity(rotation);
			}
		}
	}
}
