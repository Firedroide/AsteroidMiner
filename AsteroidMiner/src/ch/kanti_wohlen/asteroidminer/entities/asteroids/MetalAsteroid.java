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

import ch.kanti_wohlen.asteroidminer.TaskScheduler;
import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.entities.Damageable;
import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.EntityType;
import ch.kanti_wohlen.asteroidminer.entities.bars.HealthBar;
import ch.kanti_wohlen.asteroidminer.powerups.PowerUpLauncher;

public class MetalAsteroid extends Entity implements Damageable {

	public static final int MAX_HEALTH = 120;
	public static final float MIN_RADIUS = 0.75f;
	private static final float POWER_UP_SPAWN_CHANCE = 0.2f;

	private final HealthBar healthBar;
	private final float firstRadius;

	private float currentRadius;
	private float renderScale;
	private int health;

	public MetalAsteroid(World world, Vector2 location, float radius) {
		this(world, location, radius, null);
	}

	public MetalAsteroid(World world, Vector2 location, float radius, Vector2 velocity) {
		super(world, createBodyDef(location, velocity), createCircle(radius));
		healthBar = new HealthBar(MAX_HEALTH);
		firstRadius = radius;
		currentRadius = radius;
		renderScale = (radius * BOX2D_TO_PIXEL * 2f) / Textures.ASTEROID.getRegionWidth();
		health = MAX_HEALTH;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.METALASTEROID;
		positionSprite(s);
		s.setScale(renderScale);
		s.draw(batch);

		final float healthBarX = s.getX() + s.getWidth() * 0.025f;
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
			} else {
				currentRadius = MIN_RADIUS + ((float) health / MAX_HEALTH) * (firstRadius - MIN_RADIUS);
				renderScale = (currentRadius * BOX2D_TO_PIXEL * 2f) / Textures.ASTEROID.getRegionWidth();
				fixture.getShape().setRadius(currentRadius);
				body.resetMassData();
			}
		}
	}

	@Override
	public void heal(int healingAmoung) {
		setHealth(health + healingAmoung);
	}

	@Override
	public void damage(int damageAmount) {
		setHealth(health - damageAmount);
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
