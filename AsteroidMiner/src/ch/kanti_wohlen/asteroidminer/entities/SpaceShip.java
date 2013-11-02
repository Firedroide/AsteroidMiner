package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.entities.sub.HealthBar;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class SpaceShip extends Entity {

	public static final int MAX_HEALTH = 100;

	private final HealthBar healthBar;

	private int health;

	public SpaceShip(World world) {
		super(world, createBodyDef(), createFixture());
		healthBar = new HealthBar(MAX_HEALTH);
		health = MAX_HEALTH;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.SPACESHIP;
		positionSprite(s);
		s.draw(batch);
		getPhysicsBody().setGravityScale(5f);

		healthBar.render(batch, health,
				new Vector2(s.getX() - s.getWidth() * 0.05f, s.getY() + s.getHeight() * 1.15f));
	}

	@Override
	public boolean isRemoved() {
		return false;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int newHealth) {
		if (newHealth != health) {
			health = MathUtils.clamp(newHealth, 0, MAX_HEALTH);
			healthBar.resetAlpha();
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

	public Laser fireLaser() {
		return new Laser(getPhysicsBody().getWorld(), this);
	}

	private static BodyDef createBodyDef() {
		BodyDef bd = new BodyDef();
		bd.allowSleep = false;
		bd.type = BodyType.DynamicBody;
		bd.angularDamping = 10f;
		bd.linearDamping = 2.5f;
		bd.position.set(2f, 2f);

		return bd;
	}

	private static FixtureDef createFixture() {
		final FixtureDef fixture = new FixtureDef();
		fixture.density = 1f;
		final PolygonShape ps = new PolygonShape();
		ps.setAsBox(Textures.SPACESHIP.getWidth() / 2f * PIXEL_TO_BOX2D, Textures.SPACESHIP.getHeight() / 2f
				* PIXEL_TO_BOX2D);
		fixture.shape = ps;
		return fixture;
	}
}
