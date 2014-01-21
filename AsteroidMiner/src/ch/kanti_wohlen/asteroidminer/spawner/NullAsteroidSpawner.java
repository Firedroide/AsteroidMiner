package ch.kanti_wohlen.asteroidminer.spawner;

import com.badlogic.gdx.physics.box2d.World;

/*
 * AsteroidSpanwer to be used during the TutorialScreen.
 * Doesn't spawn any asteroids.
 */
public class NullAsteroidSpawner extends AsteroidSpawner {

	public NullAsteroidSpawner(World theWorld) {
		super(theWorld);
	}

	@Override
	public void tick() {
		// Do nothing
	}
}
