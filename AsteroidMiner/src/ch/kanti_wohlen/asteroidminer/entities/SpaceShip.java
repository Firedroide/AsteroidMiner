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
	public static final double FIRING_DELAY = 0.3f;
	public static final float LINEAR_DAMPING = 2.5f;
	

	private final HealthBar healthBar;
	private final Player player;

	private int health;
	private int shield;
	private double firing_delay;
	private static float linear_damping;
	private boolean canShoot;

	public SpaceShip(World world, Player owningPlayer) {
		super(world, createBodyDef(), createFixture());
		player = owningPlayer;
		healthBar = new HealthBar(MAX_HEALTH);
		health = MAX_HEALTH;
		canShoot = true;
		shield = 0;
		firing_delay = FIRING_DELAY;
		linear_damping = LINEAR_DAMPING;
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

	public double getFiring_delay() {
		return firing_delay;
	}

	public void setFiring_delay(double delay) {
		if (firing_delay == 0.3f) {
			firing_delay = delay;
		}
	}

	public double getLinear_damping() {
		return linear_damping;
	}

	public void setLinear_damping(float damping) {
		SpaceShip.linear_damping = damping;
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
			}, firing_delay);
		}
	}

	private static BodyDef createBodyDef() {
		BodyDef bd = new BodyDef();
		bd.allowSleep = false;
		bd.type = BodyType.DynamicBody;
		bd.angularDamping = 10f;
		bd.linearDamping = linear_damping;
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
