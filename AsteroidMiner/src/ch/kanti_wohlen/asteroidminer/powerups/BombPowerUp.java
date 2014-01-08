package ch.kanti_wohlen.asteroidminer.powerups;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.animations.Explosion;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class BombPowerUp extends PowerUp {

	private final World world;

	public BombPowerUp(World world, Vector2 position) {
		super(world, position);
		this.world = world;
	}

	@Override
	public void onPickUp(Player player) {
		super.onPickUp(player);
		new Explosion(world, body.getPosition().cpy(), 32f, 100, false, player);
	}

	@Override
	public PowerUpType getPowerUpType() {
		return PowerUpType.BOMB;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.BOMBPOWERUPBOX;
		positionSprite(s);
		s.draw(batch, alpha);
	}
}
