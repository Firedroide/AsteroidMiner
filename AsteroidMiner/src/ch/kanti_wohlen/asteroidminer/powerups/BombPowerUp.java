package ch.kanti_wohlen.asteroidminer.powerups;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.entities.Entity;
import ch.kanti_wohlen.asteroidminer.entities.EntityType;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class BombPowerUp extends PowerUp {

	private final World world;

	public BombPowerUp(World world, Vector2 position) {
		super(world, position);
		this.world = world;
	}

	@Override
	public void onPickUp(Player player) {
		Array<Body> bodies = new Array<Body>(world.getBodyCount());
		world.getBodies(bodies);

		for (Body b : bodies) {
			if (b == null) continue;
			Entity e = (Entity) b.getUserData();

			if (e.getType() == EntityType.ASTEROID) {
				e.remove();
			}
		}
	}

	@Override
	public PowerUpType getPowerUpType() {
		return PowerUpType.BOMB;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.BOMBPOWERUPBOX;
		positionSprite(s);
		s.draw(batch);
	}
}
