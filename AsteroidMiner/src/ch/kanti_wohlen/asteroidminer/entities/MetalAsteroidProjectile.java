package ch.kanti_wohlen.asteroidminer.entities;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.fading.FadeOutHelper;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class MetalAsteroidProjectile extends Entity implements Damageable {

	public static final int COLLISION_DAMAGE = 15;

	private static final int MAX_HEALTH = 1;
	private static final int KILL_SCORE = 200;
	private static final float RENDER_SCALE = 0.6f;

	private final Player shooter;
	private final Rectangle boundingBox;
	private int health;

	public MetalAsteroidProjectile(World world, Vector2 position, Vector2 velocity) {
		this(world, position, velocity, null);
	}

	public MetalAsteroidProjectile(World world, Vector2 position, Vector2 velocity, Player cause) {
		super(world, createBodyDef(position, velocity), createFixture());
		shooter = cause;
		health = MAX_HEALTH;

		final float width = Textures.PROJECTILE.getWidth() * PIXEL_TO_BOX2D * RENDER_SCALE;
		final float height = Textures.PROJECTILE.getHeight() * PIXEL_TO_BOX2D * RENDER_SCALE;
		boundingBox = new Rectangle(0f, 0f, width, height);

		new FadeOutHelper(this, 0.8f, 0.2f, new Runnable() {

			@Override
			public void run() {
				remove();
			}
		});
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.PROJECTILE;
		positionSprite(s);
		s.setScale(RENDER_SCALE);
		s.draw(batch, alpha);
	}

	@Override
	public EntityType getType() {
		return EntityType.PROJECTILE;
	}

	@Override
	public Rectangle getBoundingBox() {
		final Rectangle rect = new Rectangle(boundingBox);
		rect.setCenter(body.getPosition());
		return rect;
	}

	@Override
	public boolean isRemoved() {
		return super.isRemoved() || health == 0;
	}

	public Player getCausingPlayer() {
		return shooter;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void setHealth(int newHealth) {
		health = MathUtils.clamp(newHealth, 0, MAX_HEALTH);
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
		final BodyDef bd = new BodyDef();
		bd.allowSleep = false;
		bd.type = BodyType.KinematicBody;
		bd.fixedRotation = true;
		bd.gravityScale = 0f;

		bd.position.set(position);
		bd.angularVelocity = MathUtils.random(-5f, 5f);
		bd.linearVelocity.set(velocity);
		bd.angle = MathUtils.random(MathUtils.PI2);

		return bd;
	}

	private static FixtureDef createFixture() {
		final FixtureDef fixture = new FixtureDef();
		fixture.density = 0f;
		fixture.isSensor = true;
		final PolygonShape ps = new PolygonShape();
		ps.setAsBox(Textures.PROJECTILE.getWidth() * RENDER_SCALE / 1.5f * PIXEL_TO_BOX2D,
				Textures.PROJECTILE.getHeight() * RENDER_SCALE / 1.5f * PIXEL_TO_BOX2D);
		fixture.shape = ps;
		return fixture;
	}
}
