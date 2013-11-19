package ch.kanti_wohlen.asteroidminer.powerups;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.Textures;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class SpeedPowerUp extends PowerUp {

	private static final float DROP_FREQUENCY = 1f;
	private static final float SPEED_AMOUNT = 1.5f;

	public SpeedPowerUp(World world, Vector2 position) {
		super(world, position);
	}

	@Override
	public void onPickUp(Player player) {
		player.getSpaceShip().setLinear_damping(SPEED_AMOUNT);
	}

	@Override
	public float getDropFrequency() {
		return DROP_FREQUENCY;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.SPEEDPOWERUPBOX;
		positionSprite(s);
		s.draw(batch);
	}

}
