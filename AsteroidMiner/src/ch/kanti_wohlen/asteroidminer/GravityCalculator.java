package ch.kanti_wohlen.asteroidminer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public final class GravityCalculator {

	private static final float G = 0.2f;
	private static final float MAX_RANGE = 1000;

	private GravityCalculator() {}

	public static void doGravityInWorld(World world) {
		Array<Body> bodies = new Array<Body>(world.getBodyCount());
		world.getBodies(bodies);

		for (int x = 0; x < bodies.size; ++x) {
			final Body source = bodies.get(x);

			for (int y = x + 1; y < bodies.size; ++y) {
				applyGravity(source, bodies.get(y));
			}
		}
	}

	private static void applyGravity(Body body1, Body body2) {
		// Non-null, dynamic bodies please.
		if (body1 == null || body2 == null) return;
		if (!(body1.getType() == BodyType.DynamicBody && body2.getType() == BodyType.DynamicBody)) return;

		final Vector2 dir = body1.getPosition().sub(body2.getPosition());
		float dist2 = dir.len2();
		if (dist2 > MAX_RANGE) return;

		dir.nor();

		dist2 += 1.0f; // Don't go faster than Box2D can handle, ever
		final float force = G * body1.getMass() * body2.getMass() / dist2;

		// Set length of direction vector to force
		dir.scl(force);

		// Apply forces
		body2.applyForceToCenter(dir.cpy().scl(body2.getGravityScale()), true);
		body1.applyForceToCenter(dir.cpy().scl(-body1.getGravityScale()), true);
	} 
}
