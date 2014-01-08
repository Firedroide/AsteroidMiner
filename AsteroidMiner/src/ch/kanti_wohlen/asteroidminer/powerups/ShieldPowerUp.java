package ch.kanti_wohlen.asteroidminer.powerups;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer.SoundEffect;
import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class ShieldPowerUp extends PowerUp {

	public ShieldPowerUp(World world, Vector2 position) {
		super(world, position);
	}

	@Override
	public void onPickUp(Player player) {
		super.onPickUp(player);

		player.getSpaceShip().setShield(SpaceShip.MAX_SHIELD);
		SoundPlayer.playSound(SoundEffect.POWER_UP_PICK_UP, 0.4f);
	}

	@Override
	public PowerUpType getPowerUpType() {
		return PowerUpType.SHIELD;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.SHILDPOWERUPBOX;
		positionSprite(s);
		s.draw(batch, alpha);
	}
}
