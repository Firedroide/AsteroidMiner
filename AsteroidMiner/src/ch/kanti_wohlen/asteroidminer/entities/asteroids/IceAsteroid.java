package ch.kanti_wohlen.asteroidminer.entities.asteroids;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.TaskScheduler;
import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.entities.Damageable;
import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.EntityType;
import ch.kanti_wohlen.asteroidminer.entities.bars.HealthBar;
import ch.kanti_wohlen.asteroidminer.powerups.PowerUpLauncher;

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

public class IceAsteroid extends Entity implements Damageable {

	public static final int HEALTH_PER_SIZE = 15;
	public static final float MIN_RADIUS = 0.5f;
	private static final float POWER_UP_SPAWN_CHANCE = 0.1f;
	private static final int KILL_SCORE = 300;

	private final HealthBar healthBar;
	private final float firstRadius;
	private final int maxHealth;

	private float currentRadius;
	private float renderScale;
	private int health;

	public IceAsteroid(World world, Vector2 location, float radius) {
		this(world, location, radius, null);
	}

	public IceAsteroid(World world, Vector2 location, float radius, Vector2 velocity) {
		super(world, createBodyDef(location, velocity), createCircle(radius));
		maxHealth = (int) (radius * HEALTH_PER_SIZE);
		health = maxHealth;
		healthBar = new HealthBar(maxHealth);
		firstRadius = radius;
		currentRadius = radius;
		renderScale = (radius * BOX2D_TO_PIXEL * 2f) / Textures.ASTEROID.getRegionWidth();
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.ICEASTREOID;
		positionSprite(s);
		s.setScale(renderScale);
		s.draw(batch);

		final float healthBarX = s.getX() + s.getWidth() * 0.025f;
		final float healthBarY = s.getY() + s.getHeight() * 0.75f + currentRadius * BOX2D_TO_PIXEL * 0.8f;
		healthBar.render(batch, health, new Vector2(healthBarX, healthBarY));
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
		final float d = currentRadius * 2f;
		final Rectangle rect = new Rectangle(0f, 0f, d, d);
		rect.setCenter(body.getPosition());
		return rect;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int newHealth) {
		if (newHealth != health) {
			health = MathUtils.clamp(newHealth, 0, maxHealth);
			healthBar.resetAlpha();

			if (health == 0) {
				if (MathUtils.random() > POWER_UP_SPAWN_CHANCE) return;
				final World world = body.getWorld();
				final Vector2 loc = body.getPosition();
				PowerUpLauncher pul = new PowerUpLauncher(world, loc);
				TaskScheduler.INSTANCE.runTask(pul);
			} else {
				currentRadius = MIN_RADIUS + ((float) health / maxHealth) * (firstRadius - MIN_RADIUS);
				renderScale = (currentRadius * BOX2D_TO_PIXEL * 2f) / Textures.ASTEROID.getRegionWidth();
				fixture.getShape().setRadius(currentRadius);
				body.resetMassData();
			}
		}
	}

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

	public void kill() {
		setHealth(0);
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
		fixture.density = 100f;
		fixture.restitution = 1.05f;
		final CircleShape cs = new CircleShape();
		cs.setRadius(radius);
		fixture.shape = cs;
		return fixture;
	}
}
