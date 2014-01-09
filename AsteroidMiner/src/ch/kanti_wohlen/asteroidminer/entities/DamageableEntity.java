package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.TaskScheduler;
import ch.kanti_wohlen.asteroidminer.entities.bars.HealthBar;
import ch.kanti_wohlen.asteroidminer.powerups.PowerUpLauncher;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class DamageableEntity extends Entity implements Damageable {

	protected final int maxHealth;
	protected final int killScore;
	protected final float powerUpChance;
	protected final HealthBar healthBar;

	protected boolean invulnerable;
	protected int health;

	public DamageableEntity(World world, BodyDef bodyDef, FixtureDef fixtureDef, int maxHealth, int killScore,
			float powerUpChance) {
		super(world, bodyDef, fixtureDef);
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.killScore = killScore;
		this.powerUpChance = powerUpChance;
		this.healthBar = new HealthBar(maxHealth);
	}

	public int getMaximumHealth() {
		return maxHealth;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void setHealth(int newHealth) {
		setHealth(newHealth, null, 0f);
	}

	public void setHealth(int newHealth, Player player, float scoreMultiplier) {
		if (invulnerable) return;
		final int clampedHealth = MathUtils.clamp(newHealth, 0, maxHealth);
		if (health == 0 || health == clampedHealth) return;

		health = clampedHealth;
		healthBar.resetAlpha();

		if (health == 0) {
			if (powerUpChance > 0 && MathUtils.random() <= powerUpChance) {
				TaskScheduler.INSTANCE.runTask(new PowerUpLauncher(body.getWorld(), body.getPosition()));
			}
			if (player != null) {
				player.addScore((int) (killScore * scoreMultiplier));
			}
			onKill(player, scoreMultiplier);
		}
	}

	protected void onKill(Player player, float scoreMultiplier) {
		// Can be overridden by subclasses
	}

	@Override
	public void heal(int healingAmoung) {
		setHealth(health + healingAmoung);
	}

	public void heal(int healingAmoung, Player player, float scoreMultiplier) {
		setHealth(health + healingAmoung, player, scoreMultiplier);
	}

	public void damage(int damageAmount) {
		setHealth(health - damageAmount);
	}

	@Override
	public void damage(int damageAmount, Player player, float scoreMultiplier) {
		setHealth(health - damageAmount, player, scoreMultiplier);
	}

	@Override
	public void kill() {
		setHealth(0);
	}

	public void kill(Player player, float scoreMultiplier) {
		setHealth(0, player, scoreMultiplier);
	}

	public boolean isAlive() {
		return health != 0;
	}

	@Override
	public boolean isRemoved() {
		return super.isRemoved() || health == 0;
	}
}
