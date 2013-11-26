package ch.kanti_wohlen.asteroidminer.spawner;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;

public final class AsteroidAABB {

	private AsteroidAABB() {}

	public static boolean checkAsteroidClipping(World world, Vector2 center, float radius) {
		final Rectangle rect = new Rectangle(center.x - radius, center.y - radius, 2f * radius, 2f * radius);
		final AABBCallback callback = new AABBCallback();
		world.QueryAABB(callback, rect.x, rect.y, rect.x + rect.width, rect.y + rect.width);
		return callback.didCollide;
	}

	private static final class AABBCallback implements QueryCallback {

		private boolean didCollide = false;

		@Override
		public boolean reportFixture(Fixture fixture) {
			didCollide = true;
			return false;
		}
	}
}
