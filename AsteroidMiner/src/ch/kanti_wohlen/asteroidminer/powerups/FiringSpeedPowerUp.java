package ch.kanti_wohlen.asteroidminer.powerups;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.Textures;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class FiringSpeedPowerUp extends PowerUp {

	private static final float DROP_FREQUENCY = 1f;
	private static final double FIRING_AMOUNT = 0.2f;
	
	public FiringSpeedPowerUp(World world, Vector2 location) {
		super(world, location);
	}

	@Override
	public void onPickUp(Player player) {
		player.getSpaceShip().setFiring_delay(FIRING_AMOUNT);
	}

	@Override
	public float getDropFrequency() {
		return DROP_FREQUENCY;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.FIRINGSPEEDPOWERUPBOX;
		positionSprite(s);
		s.draw(batch);

	}

}
