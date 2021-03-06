package ch.kanti_wohlen.asteroidminer.powerups;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.TaskScheduler;
import ch.kanti_wohlen.asteroidminer.Textures;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer;
import ch.kanti_wohlen.asteroidminer.audio.SoundPlayer.SoundEffect;
import ch.kanti_wohlen.asteroidminer.entities.SpaceShip;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class SpeedPowerUp extends PowerUp {

	private static final float SPEED_INCREASE = 0.5f;
	private static final float POWER_UP_DURATION = 10f;

	public SpeedPowerUp(World world, Vector2 position) {
		super(world, position);
	}

	@Override
	public void onPickUp(Player player) {
		super.onPickUp(player);

		player.getSpaceShip().setSpeed(SpaceShip.DEFAULT_SPEED + SPEED_INCREASE);
		TaskScheduler.INSTANCE.runTaskLater(new PowerUpRemover(player), POWER_UP_DURATION);
		SoundPlayer.playSound(SoundEffect.POWER_UP_PICK_UP, 0.4f);
	}

	@Override
	public PowerUpType getPowerUpType() {
		return PowerUpType.MOVEMENT_SPEED;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.SPEEDPOWERUPBOX;
		positionSprite(s);
		s.draw(batch, alpha);
	}

	private class PowerUpRemover implements Runnable {

		private final Player player;

		private PowerUpRemover(Player player) {
			this.player = player;
		}

		@Override
		public void run() {
			if (player == null || player.getSpaceShip() == null) return;
			player.getSpaceShip().setSpeed(SpaceShip.DEFAULT_SPEED);
		}
	}
}
