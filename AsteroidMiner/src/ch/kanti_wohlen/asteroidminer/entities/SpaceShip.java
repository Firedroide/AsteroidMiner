package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.AsteroidMiner;
import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.TaskScheduler;
import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer.SoundEffect;
import ch.kanti_wohlen.asteroidminer.entities.bars.HealthBar;
import ch.kanti_wohlen.asteroidminer.entities.bars.ShieldBar;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class SpaceShip extends Entity implements Damageable {

	public static final int MAX_HEALTH = 200;
	public static final int MAX_SHIELD = 200;
	public static final float DEFAULT_FIRING_DELAY = 0.27f;
	public static final float DEFAULT_SPEED = 1f;
	public static final float LINEAR_DAMPING = 2.5f;

	private final Player player;
	private final HealthBar healthBar;
	private final ShieldBar shieldBar;
	private final Rectangle boundingBox;

	private int health;
	private int shield;
	private int laserDamage;
	private float firingDelay;
	private float speed;
	private boolean canShoot;
	private boolean invulnerable;

	public SpaceShip(World world, Player owningPlayer) {
		this(world, owningPlayer, null, null);
	}

	public SpaceShip(World world, Player owningPlayer, Vector2 position, Vector2 velocity) {
		super(world, createBodyDef(position, velocity), createFixture());
		player = owningPlayer;
		healthBar = new HealthBar(MAX_HEALTH);
		shieldBar = new ShieldBar(MAX_SHIELD);

		health = 0;
		shield = 0;
		firingDelay = DEFAULT_FIRING_DELAY;
		speed = DEFAULT_SPEED;
		laserDamage = Laser.DEFAULT_DAMAGE;
		canShoot = false;
		invulnerable = true;
		TaskScheduler.INSTANCE.runTaskLater(new Runnable() {

			@Override
			public void run() {
				invulnerable = false;
				canShoot = true;
				health = MAX_HEALTH;
			}
		}, 0.8f);

		final float width = Textures.SPACESHIP.getWidth() * PIXEL_TO_BOX2D;
		final float height = Textures.SPACESHIP.getHeight() * PIXEL_TO_BOX2D;
		boundingBox = new Rectangle(0f, 0f, width, height);
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.SPACESHIP;
		positionSprite(s);
		s.draw(batch, alpha);

		final Vector2 barLoc = new Vector2(s.getX() - s.getWidth() * 0.05f, s.getY() + s.getHeight() * 1.15f);
		if (shield > 0) barLoc.y += Textures.HEALTH_HIGH.getRegionHeight() / 2f;

		healthBar.render(batch, health, barLoc);
		if (shield > 0) {
			shieldBar.render(batch, shield, barLoc.sub(0f, Textures.HEALTH_HIGH.getRegionHeight()), healthBar.getAlpha());
		}
	}

	@Override
	public boolean isRemoved() {
		return isRemovedByUser();
	}

	@Override
	public Rectangle getBoundingBox() {
		final Rectangle rect = new Rectangle(boundingBox);
		rect.setCenter(body.getPosition());
		return rect;
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
		if (invulnerable) return;
		if (health == 0) return;

		if (newHealth != health) {
			health = MathUtils.clamp(newHealth, 0, MAX_HEALTH);
			healthBar.resetAlpha();

			if (health == 0) {
				AsteroidMiner.INSTANCE.getGameScreen().stopGame();
			}
		}
	}

	public void heal(int healingAmoung) {
		setHealth(health + healingAmoung);
	}

	public int getShield() {
		return shield;
	}

	public void setShield(int newShield) {
		if (shield != newShield) {
			shield = MathUtils.clamp(newShield, 0, MAX_SHIELD);
			healthBar.resetAlpha();
		}
	}

	public float getFiringDelay() {
		return firingDelay;
	}

	public void setFiringDelay(float newFiringDelay) {
		if (firingDelay != newFiringDelay) {
			firingDelay = newFiringDelay;
		}
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float newSpeed) {
		speed = newSpeed;
	}

	public int getLaserDamage() {
		return laserDamage;
	}

	public void setLaserDamage(int newDamage) {
		laserDamage = newDamage;
	}

	public void damage(int damageAmount, Player player, float scoreMultiplier) {
		if (shield >= damageAmount) {
			setShield(shield - damageAmount);
		} else {
			final int hullDamage = damageAmount - shield;
			if (shield != 0) setShield(0);
			setHealth(health - hullDamage);
		}
	}

	public void kill() {
		setHealth(0);
	}

	public void fireLaser() {
		if (canShoot) {
			canShoot = false;
			new Laser(body.getWorld(), this, laserDamage);
			TaskScheduler.INSTANCE.runTaskLater(new Runnable() {

				@Override
				public void run() {
					canShoot = true;
				}
			}, firingDelay);
			SoundPlayer.playSound(SoundEffect.LASER_SHOOT, 0.25f, MathUtils.random(0.975f, 1.025f), 0f);
		}
	}

	private static BodyDef createBodyDef(Vector2 position, Vector2 velocity) {
		BodyDef bd = new BodyDef();
		bd.allowSleep = false;
		bd.type = BodyType.DynamicBody;
		bd.angularDamping = 10f;
		bd.linearDamping = LINEAR_DAMPING;
		bd.position.set(2f, 2f);
		bd.gravityScale = 5f;

		if (position != null) bd.position.set(position);
		if (velocity != null) bd.linearVelocity.set(velocity);

		return bd;
	}

	private static FixtureDef createFixture() {
		final FixtureDef fixture = new FixtureDef();
		fixture.density = 1f;
		final PolygonShape ps = new PolygonShape();
		ps.setAsBox(Textures.SPACESHIP.getWidth() / 2f * PIXEL_TO_BOX2D, Textures.SPACESHIP.getHeight() / 2f
				* PIXEL_TO_BOX2D);
		fixture.shape = ps;
		fixture.filter.categoryBits = 2;
		return fixture;
	}
}
