package ch.kanti_wohlen.asteroidminer.animations;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.entities.Damageable;
import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.EntityType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;

public class Explosion implements Animation {

	private static final ShapeRenderer renderer = new ShapeRenderer();
	private static final float EXPLOSION_SCORE_MULTIPLIER = 0.2f;

	private final World world;
	private final Vector2 center;
	private final float r;
	private final int maxDmg;
	private final boolean dmgPlayer;
	private final Player player;
	private final LinkedList<AbstractMap.SimpleImmutableEntry<Entity, Float>> entities;

	private int currentRadius;
	private boolean removed;

	public Explosion(World theWorld, Vector2 explosionCenter, float radius, int maxDamage, boolean hurtPlayer, Player owner) {
		world = theWorld;
		center = explosionCenter;
		r = radius;
		maxDmg = maxDamage;
		dmgPlayer = hurtPlayer;
		currentRadius = 1;
		player = owner;

		entities = new LinkedList<AbstractMap.SimpleImmutableEntry<Entity, Float>>();
		final Vector2 upperLeft = explosionCenter.cpy().sub(r, r);
		final Vector2 lowerRight = explosionCenter.cpy().add(r, r);
		world.QueryAABB(new QueryCallback() {

			@Override
			public boolean reportFixture(Fixture fixture) {
				final Entity entity = (Entity) fixture.getBody().getUserData();
				if (entity.getType() == EntityType.ASTEROID || (entity.getType() == EntityType.SPACESHIP) && dmgPlayer) {
					final float r2 = fixture.getBody().getPosition().dst2(center);

					if (r2 <= r * r) {
						entities.add(new AbstractMap.SimpleImmutableEntry<Entity, Float>(entity, r2));
					}
				}
				return true;
			}
		}, upperLeft.x, upperLeft.y, lowerRight.x, lowerRight.y);

		Collections.sort(entities, new Comparator<Map.Entry<Entity, Float>>() {

			@Override
			public int compare(Map.Entry<Entity, Float> o1, Map.Entry<Entity, Float> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});

		center.scl(Entity.BOX2D_TO_PIXEL); // Scale center for rendering.
		Animations.addAnimation(this);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.end();
		renderer.begin(ShapeType.Filled);
		renderer.setTransformMatrix(batch.getTransformMatrix());
		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.setColor(Color.RED);
		renderer.circle(center.x, center.y, currentRadius * Entity.BOX2D_TO_PIXEL);
		renderer.end();
		batch.begin();
	}

	public void tick(float deltaTime) {
		++currentRadius;
		if (currentRadius > r) {
			removed = true;
			return;
		}
		if (entities.isEmpty()) return;

		final float radiusMax = currentRadius * currentRadius;

		for (Map.Entry<Entity, Float> entry = entities.getFirst(); entry.getValue() < radiusMax; entry = entities.getFirst()) {
			final int damage = (int) (maxDmg * Math.min(1f, 1.25f * (r - currentRadius) / r));
			final Damageable damageable = (Damageable) entry.getKey();
			damageable.damage(damage, player, EXPLOSION_SCORE_MULTIPLIER);
			entities.removeFirst();
			if (entities.isEmpty()) break;
		}
	}

	@Override
	public boolean isRemoved() {
		return removed;
	}

	@Override
	public void dispose() {
		entities.clear();
	}
}
