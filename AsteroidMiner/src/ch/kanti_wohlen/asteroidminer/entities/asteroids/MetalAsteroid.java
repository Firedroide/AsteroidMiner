package ch.kanti_wohlen.asteroidminer.entities.asteroids;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.TaskScheduler;
import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.entities.DamageableEntity;
import ch.kanti_wohlen.asteroidminer.entities.EntityType;
import ch.kanti_wohlen.asteroidminer.entities.MetalAsteroidProjectile;

public class MetalAsteroid extends DamageableEntity {

	public static final int HEALTH_PER_SIZE = 50;
	private static final float POWER_UP_CHANCE = 0.2f;
	private static final int SCORE_PER_SIZE = 100;

	private final float currentRadius;
	private final float renderScale;

	public MetalAsteroid(World world, Vector2 location, float radius) {
		this(world, location, radius, null);
	}

	public MetalAsteroid(World world, Vector2 location, float radius, Vector2 velocity) {
		super(world, createBodyDef(location, velocity), createCircle(radius), (int) (radius * HEALTH_PER_SIZE),
				(int) (radius * SCORE_PER_SIZE), POWER_UP_CHANCE);
		currentRadius = radius;
		renderScale = (radius * BOX2D_TO_PIXEL * 2f) / Textures.METALASTEROID.getRegionWidth();
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.METALASTEROID;
		positionSprite(s);
		s.setScale(renderScale);
		s.draw(batch, alpha);

		final float healthBarX = s.getX() + s.getWidth() * 0.17f;
		final float healthBarY = s.getY() + s.getHeight() * 0.6f + currentRadius * BOX2D_TO_PIXEL;
		healthBar.render(batch, health, new Vector2(healthBarX, healthBarY));
	}

	@Override
	public EntityType getType() {
		return EntityType.ASTEROID;
	}

	@Override
	public Rectangle getBoundingBox() {
		final float d = currentRadius * 2f;
		final Rectangle rect = new Rectangle(0f, 0f, d, d);
		rect.setCenter(body.getPosition());
		return rect;
	}

	@Override
	protected void onKill(Player player, float scoreMultiplier) {
		final int count = (int) (4.5f + currentRadius);
		TaskScheduler.INSTANCE.runTask(new MetalAsteroidProjectileSpawner(body.getWorld(), this, count, player));
	}

	private static BodyDef createBodyDef(Vector2 position, Vector2 velocity) {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.angle = MathUtils.random(2 * MathUtils.PI);
		bodyDef.angularDamping = 0.15f;
		if (velocity != null) {
			bodyDef.linearVelocity.set(velocity);
		}
		bodyDef.gravityScale = 0.1f;

		return bodyDef;
	}

	private static FixtureDef createCircle(float radius) {
		final FixtureDef fixture = new FixtureDef();
		fixture.density = 200f;
		fixture.restitution = 1.2f;
		final CircleShape cs = new CircleShape();
		cs.setRadius(radius);
		fixture.shape = cs;
		return fixture;
	}

	private class MetalAsteroidProjectileSpawner implements Runnable {

		private static final float VELOCITY = 40f;

		private final World world;
		private final Vector2 pos;
		private final float rot;
		private final int count;
		private final Player cause;

		private MetalAsteroidProjectileSpawner(World theWorld, MetalAsteroid source, int projectileCount, Player player) {
			world = theWorld;
			pos = source.getPhysicsBody().getPosition().cpy();
			rot = source.getPhysicsBody().getAngle();
			count = projectileCount;
			cause = player;
		}

		@Override
		public void run() {
			final Vector2 vel = new Vector2();
			for (int i = 0; i < count; ++i) {
				final float angle = (((float) i / count) * MathUtils.PI2 + rot) % MathUtils.PI2;
				vel.set(MathUtils.sin(angle) * VELOCITY, MathUtils.cos(angle) * VELOCITY);

				new MetalAsteroidProjectile(world, pos.cpy().add(vel.cpy().nor()), vel.cpy(), cause);
			}
		}
	}
}
