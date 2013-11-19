package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.TaskScheduler;
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

public class SpaceShip extends Entity implements Damageable {

	public static final int MAX_HEALTH = 100;
	public static final int MAX_SHIELD = 100;
	public static final double FIRING_DELAY_DEFAULT = 0.3f;
	public static final double FIRING_DELAY_DECREASED = 0.2f;
	public static final float SPEED_DEFAULT = 1f;
	public static final float SPEED_INCREASED = 1.5f;

	private final HealthBar healthBar;
	private final Player player;

	private int health;
	private int shield;
	private double firingDelay;
	private float speed;
	private boolean canShoot;

	public SpaceShip(World world, Player owningPlayer) {
		super(world, createBodyDef(), createFixture());
		player = owningPlayer;
		healthBar = new HealthBar(MAX_HEALTH);
		health = MAX_HEALTH;
		canShoot = true;
		shield = 0;
		firingDelay = FIRING_DELAY_DEFAULT;
		speed = SPEED_DEFAULT;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.SPACESHIP;
		positionSprite(s);
		s.draw(batch);
		getPhysicsBody().setGravityScale(5f);

		healthBar.render(batch, health, new Vector2(s.getX() - s.getWidth() * 0.05f, s.getY() + s.getHeight() * 1.15f));
	}

	@Override
	public boolean isRemoved() {
		return false;
	}

	@Override
	public EntityType getType() {
		return EntityType.SPACESHIP;
	}

	public Player getPlayer() {
		return player;
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

	public int getShield() {
		return shield;
	}

	public void setShield(int newShield) {
		if (shield != MAX_SHIELD) {
			shield = MathUtils.clamp(newShield, 0, MAX_SHIELD);
		}
	}

	public double getFiringDelay() {
		return firingDelay;
	}

	public void setFiringDelay(double delay) {
		if (firingDelay == 0.3f) {
			firingDelay = delay;
		}
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float newSpeed) {
		speed = newSpeed;
	}

	public void damage(int damageAmount) {
		if (shield == 0) {
			setHealth(health - damageAmount);
		} else {
			setShield(shield - damageAmount);
		}
	}

	public void kill() {
		setHealth(0);
	}

	public void fireLaser() {
		if (canShoot) {
			canShoot = false;
			new Laser(getPhysicsBody().getWorld(), this);
			TaskScheduler.INSTANCE.runTaskLater(new Runnable() {

				@Override
				public void run() {
					canShoot = true;
				}
			}, firingDelay);
		}
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
		ps.setAsBox(Textures.SPACESHIP.getWidth() / 2f * PIXEL_TO_BOX2D, Textures.SPACESHIP.getHeight() / 2f * PIXEL_TO_BOX2D);
		fixture.shape = ps;
		fixture.filter.categoryBits = 2;
		return fixture;
	}

}
