package ch.kanti_wohlen.asteroidminer.spawner;

import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.WorldBorder.BorderSide;
import ch.kanti_wohlen.asteroidminer.entities.asteroids.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class IdleSpawner extends AsteroidSpawner {

	private static final int START_SPAWNING_AMOUNT = 10;

	public IdleSpawner(World theWorld) {
		super(theWorld);
	}

	@Override
	public void start() {
		super.start();
		for (int i = 0; i < START_SPAWNING_AMOUNT; i++) {
			spawn();
		}
	}

	@Override
	public void tick() {
		if (MathUtils.random() > 0.05f) return;
		System.out.println("Entity count: " + world.getBodyCount());
		spawn();
	}

	private void spawn() {
		final float width = Gdx.graphics.getWidth() * Entity.PIXEL_TO_BOX2D;
		final float height = Gdx.graphics.getHeight() * Entity.PIXEL_TO_BOX2D;
		final float angle = MathUtils.random(MathUtils.PI2);
		final float speed = MathUtils.random(3f, 10f);
		final float rotation = MathUtils.random(-0.5f, 0.5f);

		final BorderSide side = chooseSide(angle);
		final Vector2 spawningLocation = getSpawningPosition(side, width, height);
		final Vector2 movementVector = new Vector2(speed * -MathUtils.sin(angle), speed * MathUtils.cos(angle));

		spawnRandomAsteroid(spawningLocation, movementVector, rotation);
	}

	// TODO: Random asteroid
	private void spawnRandomAsteroid(Vector2 location, Vector2 momentum, float rotation) {
		final float radius = MathUtils.random(1f, 4f);
		if (!AsteroidAABB.checkAsteroidClipping(world, location, radius)) {
			StoneAsteroid stoneAsteroid = new StoneAsteroid(world, location, radius, momentum);
			stoneAsteroid.getPhysicsBody().setAngularVelocity(rotation);
		}
	}
}
