package ch.kanti_wohlen.asteroidminer.spawner;

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

	private static final int START_SPAWNING_AMOUNT = 10;
	private static final int MAXIMUM_ENTITIES = 250;

	public TimeAttackAsteroidSpawner(World theWorld) {
		super(theWorld);
	}

	@Override
	public void start() {
		super.start();
		WorldBorder.addBorders(world);
		for (int i = 0; i < START_SPAWNING_AMOUNT; i++) {
			spawn();
		}
	}

	@Override
	public void tick() {
		if (MathUtils.random() > 0.1f) return;
		if (world.getBodyCount() < MAXIMUM_ENTITIES) spawn();
	}

	private void spawn() {
		final float width = GameScreen.WORLD_SIZE + Gdx.graphics.getWidth() * Entity.PIXEL_TO_BOX2D * 0.2f;
		final float height = GameScreen.WORLD_SIZE + Gdx.graphics.getHeight() * Entity.PIXEL_TO_BOX2D * 0.2f;
		final float angle = MathUtils.random(MathUtils.PI2);
		final float speed = MathUtils.random(5f, 15f);
		final float rotation = MathUtils.random(-0.5f, 0.5f);

		final BorderSide side = chooseSide(angle);
		final Vector2 spawningLocation = getSpawningPosition(side, width, height);
		final Vector2 movementVector = new Vector2(speed * -MathUtils.sin(angle), speed * MathUtils.cos(angle));

		spawnRandomAsteroid(spawningLocation, movementVector, rotation);
	}

	private void spawnRandomAsteroid(Vector2 location, Vector2 momentum, float rotation) {
		final float radius = MathUtils.random(1f, 4f);
		final float type = MathUtils.random(8f);
		if (!AsteroidAABB.checkAsteroidClipping(world, location, radius)) {
			if (type < 6f) {
				StoneAsteroid stoneAsteroid = new StoneAsteroid(world, location, radius, momentum);
				stoneAsteroid.getPhysicsBody().setAngularVelocity(rotation);
			} else {
				IceAsteroid iceAsteroid = new IceAsteroid(world, location, radius, momentum);
				iceAsteroid.getPhysicsBody().setAngularVelocity(rotation);
			}
		}
	}
}
