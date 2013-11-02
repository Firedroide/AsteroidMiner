package ch.kanti_wohlen.asteroidminer.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Entity {

	protected static final float BOX2D_TO_PIXEL = 10f;
	protected static final float PIXEL_TO_BOX2D = 0.1f;

	private final Body body;
	private boolean removed;

	public Entity(World world, BodyDef bodyDef, FixtureDef... fixtures) {
		body = world.createBody(bodyDef);
		body.setUserData(this);
		if (fixtures == null) return;
		for (FixtureDef fixture : fixtures) {
			body.createFixture(fixture);
			fixture.shape.dispose();
		}
	}

	public Body getPhysicsBody() {
		return body;
	}

	public boolean remove() {
		if (isRemoved()) return false;
		removed = true;
		return true;
	}

	public boolean isRemoved() {
		return removed;
	}

	protected void positionSprite(Sprite sprite) {
		Vector2 loc = new Vector2(body.getPosition());
		loc.mul(Entity.BOX2D_TO_PIXEL);

		sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		sprite.setPosition(loc.x - sprite.getWidth() / 2f, loc.y - sprite.getHeight() / 2f);
	}

	public abstract void render(SpriteBatch batch);
}
