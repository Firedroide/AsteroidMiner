package ch.kanti_wohlen.asteroidminer.spawner;

import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.WorldBorder.BorderSide;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public abstract class AsteroidSpawner {

	protected final World world;

	public AsteroidSpawner(World theWorld) {
		world = theWorld;
	}

	public void start() {}

	public abstract void tick();

	public void stop() {
		Array<Body> bodies = new Array<Body>(world.getBodyCount());
		world.getBodies(bodies);
		for (Body body : bodies) {
			Entity e = (Entity) body.getUserData();
			world.destroyBody(body);
			e.remove();
		}
	}

	protected BorderSide chooseSide(float angle) {
		if (angle < MathUtils.PI * 0.5f) {
			if (MathUtils.randomBoolean()) {
				return BorderSide.BOTTOM;
			} else {
				return BorderSide.RIGHT;
			}
		} else if (angle < MathUtils.PI) {
			if (MathUtils.randomBoolean()) {
				return BorderSide.RIGHT;
			} else {
				return BorderSide.TOP;
			}
		} else if (angle < MathUtils.PI * 1.5f) {
			if (MathUtils.randomBoolean()) {
				return BorderSide.TOP;
			} else {
				return BorderSide.LEFT;
			}
		} else {
			if (MathUtils.randomBoolean()) {
				return BorderSide.LEFT;
			} else {
				return BorderSide.BOTTOM;
			}
		}
	}

	protected static Vector2 getSpawningPosition(BorderSide side, float distance) {
		return positionOnRectangle(createRectangle(side, distance, distance));
	}

	protected static Vector2 getSpawningPosition(BorderSide side, float xDistance, float yDistance) {
		return positionOnRectangle(createRectangle(side, xDistance, yDistance));
	}

	private static Rectangle createRectangle(BorderSide side, float xDistance, float yDistance) {
		switch (side) {
		case TOP:
			return new Rectangle(-xDistance, yDistance, 2f * xDistance, 0f);
		case BOTTOM:
			return new Rectangle(-xDistance, -yDistance, 2f * xDistance, 0f);
		case LEFT:
			return new Rectangle(-xDistance, -yDistance, 0f, 2f * yDistance);
		case RIGHT:
			return new Rectangle(xDistance, -yDistance, 0f, 2f * yDistance);
		default:
			return null;
		}
	}

	private static Vector2 positionOnRectangle(Rectangle rectangle) {
		final Vector2 pos = new Vector2(rectangle.x, rectangle.y);
		final float rnd = MathUtils.random();
		return pos.add(rectangle.width * rnd, rectangle.height * rnd);
	}
}
