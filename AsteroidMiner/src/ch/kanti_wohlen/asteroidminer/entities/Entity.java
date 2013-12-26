package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.TaskScheduler;
import ch.kanti_wohlen.asteroidminer.TaskScheduler.Task;

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

public abstract class Entity {

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

	public void fadeOut(float startDelay, float fadeOutTime) {
		if (fadeOut != null) return; // If already fading out, ignore
		fadeOut = new FadeOutHelper(startDelay, fadeOutTime);
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

	private class FadeOutHelper implements Runnable {

		private final float time;
		private final Task task;
		private float currentTime;

		private FadeOutHelper(float startDelay, float fadeOutTime) {
			time = fadeOutTime;
			task = TaskScheduler.INSTANCE.runTaskRepeated(this, startDelay, 0, time);
		}

		@Override
		public void run() {
			if (isRemoved()) {
				task.cancel();
				return;
			}
			currentTime += 1 * TaskScheduler.TICK_TIME;
			alpha = 1f - currentTime / time;
			if (time - currentTime < TaskScheduler.TICK_TIME) {
				remove();
			}
		}
	}
}
