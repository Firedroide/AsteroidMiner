package ch.kanti_wohlen.asteroidminer.entities.asteroids;

import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.sub.HealthBar;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class StoneAsteroid extends Entity {

	public static final int MAX_HEALTH = 100;
	public static final float STONE_ASTEROID_MIN_SIZE = 0.5f;

	private final HealthBar healthBar;
	private final float currentRadius;
	private final float renderScale;

	private int health;

	public StoneAsteroid(World world, Vector2 location, float radius) {
		this(world, location, radius, null);
	}

	public StoneAsteroid(World world, Vector2 location, float radius, Vector2 velocity) {
		super(world, createBodyDef(location, velocity), createCircle(radius));
		healthBar = new HealthBar(MAX_HEALTH);
		currentRadius = radius;
		renderScale = (radius * BOX2D_TO_PIXEL * 2f) / Textures.ASTEROID.getRegionWidth();
		health = MAX_HEALTH;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.ASTEROID;
		positionSprite(s);
		s.setScale(renderScale);
		s.draw(batch);

		healthBar.render(batch, health, new Vector2(s.getX() + s.getWidth() * 0.025f, s.getY() + s.getHeight() * 1.15f));
	}

	@Override
	public boolean isRemoved() {
		return health == 0;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int newHealth) {
		if (newHealth != health) {
			health = MathUtils.clamp(newHealth, 0, MAX_HEALTH);
			healthBar.resetAlpha();

			if (health == 0f) {
				final World w = getPhysicsBody().getWorld();
				final Body body = getPhysicsBody();
				final float nextRadius = currentRadius / 2f;

				if (nextRadius <= STONE_ASTEROID_MIN_SIZE) {
					// TODO: Spawn PowerUps? Use separate PowerUpSpawner class
					return;
				}

				Vector2 locOff = new Vector2(body.getLinearVelocity().nor());
				Vector2 loc1 = body.getPosition().cpy().add(locOff.rotate(90f));
				Vector2 loc2 = body.getPosition().cpy().add(locOff.rotate(180f));
				StoneAsteroid a1 = new StoneAsteroid(w, loc1, nextRadius, body.getLinearVelocity());
				StoneAsteroid a2 = new StoneAsteroid(w, loc2, nextRadius, body.getLinearVelocity());

				Vector2 impulse = new Vector2(body.getLinearVelocity());
				impulse.scl(body.getMass()).scl(0.25f);

				a1.getPhysicsBody().applyLinearImpulse(impulse.rotate(90f), body.getWorldCenter(), true);
				a2.getPhysicsBody().applyLinearImpulse(impulse.rotate(180f), body.getWorldCenter(), true);
			}
		}
	}

	public void heal(int healingAmoung) {
		setHealth(health + healingAmoung);
	}

	public void damage(int damageAmount) {
		setHealth(health - damageAmount);
	}

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