package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.fading.FadeOutHelper;
import ch.kanti_wohlen.asteroidminer.fading.Fadeable;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Entity implements Fadeable {

	public static final float BOX2D_TO_PIXEL = 10f;
	public static final float PIXEL_TO_BOX2D = 0.1f;

	protected final Body body;
	protected final Fixture fixture;
	protected float alpha;
	private boolean removed;
	private FadeOutHelper fadeOut;

	public Entity(World world, BodyDef bodyDef, FixtureDef fixtureDef) {
		alpha = 1f;
		body = world.createBody(bodyDef);
		body.setUserData(this);
		if (fixtureDef != null) {
			fixture = body.createFixture(fixtureDef);
			fixtureDef.shape.dispose();
		} else {
			fixture = null;
		}
	}

	public Body getPhysicsBody() {
		return body;
	}

	public boolean remove() {
		if (isRemoved()) return false;
		removed = true;
		if (fadeOut != null) {
			fadeOut.cancel();
		}
		return true;
	}

	public boolean isRemoved() {
		return removed || WorldBorder.isOutside(this);
	}

	protected boolean isRemovedByUser() {
		return removed;
	}

	public void fadeOut(float fadeOutTime) {
		fadeOut(0f, fadeOutTime);
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float newAlpha) {
		alpha = MathUtils.clamp(newAlpha, 0f, 1f);
	}

	public void fadeOut(float startDelay, float fadeOutTime) {
		if (fadeOut != null) return; // If already fading out, ignore
		fadeOut = new FadeOutHelper(this, startDelay, fadeOutTime, new Runnable() {

			@Override
			public void run() {
				remove();
			}
		});
	}

	protected void positionSprite(Sprite sprite) {
		Vector2 loc = new Vector2(body.getPosition());
		loc.scl(Entity.BOX2D_TO_PIXEL);

		sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		sprite.setPosition(loc.x - sprite.getWidth() / 2f, loc.y - sprite.getHeight() / 2f);
	}

	public abstract void render(SpriteBatch batch);

	public abstract EntityType getType();

	public abstract Rectangle getBoundingBox();
}
