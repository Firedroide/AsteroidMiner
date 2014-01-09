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
import ch.kanti_wohlen.asteroidminer.animations.Explosion;
import ch.kanti_wohlen.asteroidminer.entities.DamageableEntity;
import ch.kanti_wohlen.asteroidminer.entities.EntityType;

public class ExplosiveAsteroid extends DamageableEntity {

	public static final int MAX_HEALTH = 10;
	public static final float MIN_RADIUS = 0.5f;
	private static final float POWER_UP_CHANCE = 0.1f;
	private static final int KILL_SCORE = 100;

	private final float currentRadius;
	private final float renderScale;

	public ExplosiveAsteroid(World world, Vector2 location, float radius) {
		this(world, location, radius, null);
	}

	public ExplosiveAsteroid(World world, Vector2 location, float radius, Vector2 velocity) {
		super(world, createBodyDef(location, velocity), createCircle(radius), MAX_HEALTH, KILL_SCORE, POWER_UP_CHANCE);
		currentRadius = radius;
		renderScale = (radius * BOX2D_TO_PIXEL * 2f) / Textures.EXPLOSIVEASTEROID.getRegionWidth();
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.EXPLOSIVEASTEROID;
		positionSprite(s);
		s.setScale(renderScale);
		s.draw(batch, alpha);

		// Do not render the health bar.
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
		final Vector2 pos = body.getPosition().cpy();
		final Player damager = player;
		final float radius = currentRadius * 8f;
		final int damage = (int) (25f * currentRadius);

		TaskScheduler.INSTANCE.runTask(new Runnable() {

			@Override
			public void run() {
				new Explosion(body.getWorld(), pos, radius, damage, true, damager);
			}
		});
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
		fixture.density = 150f;
		fixture.restitution = 0.5f;
		final CircleShape cs = new CircleShape();
		cs.setRadius(radius);
		fixture.shape = cs;
		return fixture;
	}
}
