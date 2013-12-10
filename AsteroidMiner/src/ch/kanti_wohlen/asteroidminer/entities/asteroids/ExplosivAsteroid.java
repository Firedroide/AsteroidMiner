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
import ch.kanti_wohlen.asteroidminer.entities.Damageable;
import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.EntityType;
import ch.kanti_wohlen.asteroidminer.entities.bars.HealthBar;
import ch.kanti_wohlen.asteroidminer.powerups.PowerUpLauncher;

public class ExplosivAsteroid extends Entity implements Damageable {

	public static final int MAX_HEALTH = 10;
	public static final float MIN_RADIUS = 0.5f;
	private static final float POWER_UP_SPAWN_CHANCE = 0.1f;
	private static final int KILL_SCORE = 100;

	private final HealthBar healthBar;
	private float radius;
	private float renderScale;
	private int health;

	public ExplosivAsteroid(World world, Vector2 location, float radius) {
		this(world, location, radius, null);
	}

	public ExplosivAsteroid(World world, Vector2 location, float radius, Vector2 velocity) {
		super(world, createBodyDef(location, velocity), createCircle(radius));
		healthBar = new HealthBar(MAX_HEALTH);
		this.radius = radius;
		renderScale = (radius * BOX2D_TO_PIXEL * 2f) / Textures.ASTEROID.getRegionWidth();
		health = MAX_HEALTH;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.ASTEROID;
		positionSprite(s);
		s.setScale(renderScale);
		s.draw(batch);
	}

	@Override
	public boolean isRemoved() {
		return super.isRemoved() || health == 0;
	}

	@Override
	public EntityType getType() {
		return EntityType.ASTEROID;
	}

	@Override
	public Rectangle getBoundingBox() {
		final float d = radius * 2f;
		final Rectangle rect = new Rectangle(0f, 0f, d, d);
		rect.setCenter(body.getPosition());
		return rect;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void setHealth(int newHealth) {
		if (newHealth != health) {
			health = MathUtils.clamp(newHealth, 0, MAX_HEALTH);
			healthBar.resetAlpha();

			if (health == 0) {
				if (MathUtils.random() > POWER_UP_SPAWN_CHANCE)
					return;
				final World world = body.getWorld();
				final Vector2 loc = body.getPosition();
				PowerUpLauncher pul = new PowerUpLauncher(world, loc);
				TaskScheduler.INSTANCE.runTask(pul);
			}
		}
	}

	@Override
	public void heal(int healingAmoung) {
		setHealth(health + healingAmoung);
	}

	@Override
	public void damage(int damageAmount, Player player, float scoreMultiplier) {
		setHealth(health - damageAmount);
		if (health == 0 && player != null) {
			player.addScore((int) (KILL_SCORE * scoreMultiplier));
		}
	}

	@Override
	public void kill() {
		setHealth(0);
	}

	private static BodyDef createBodyDef(Vector2 position, Vector2 velocity) {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.angle = MathUtils.random(2 * MathUtils.PI);
		if (velocity != null) {
			bodyDef.linearVelocity.set(velocity);
		}
		bodyDef.gravityScale = 0.1f;

		return bodyDef;
	}

	private static FixtureDef createCircle(float radius) {
		final FixtureDef fixture = new FixtureDef();
		fixture.density = 100f;
		fixture.restitution = 0.9f;
		final CircleShape cs = new CircleShape();
		cs.setRadius(radius);
		fixture.shape = cs;
		return fixture;
	}

}
