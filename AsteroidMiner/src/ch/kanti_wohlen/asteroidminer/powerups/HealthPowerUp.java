package ch.kanti_wohlen.asteroidminer.powerups;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer.SoundEffect;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class HealthPowerUp extends PowerUp {

	private static final int HEALING_AMOUNT = 120;

	public HealthPowerUp(World world, Vector2 position) {
		super(world, position);
	}

	@Override
	public void onPickUp(Player player) {
		super.onPickUp(player);

		player.getSpaceShip().heal(HEALING_AMOUNT);
		SoundPlayer.playSound(SoundEffect.POWER_UP_PICK_UP, 0.4f);
	}

	@Override
	public PowerUpType getPowerUpType() {
		return PowerUpType.HEALTH;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.LIFEPOWERUPBOX;
		positionSprite(s);
		s.draw(batch, alpha);
	}
}
