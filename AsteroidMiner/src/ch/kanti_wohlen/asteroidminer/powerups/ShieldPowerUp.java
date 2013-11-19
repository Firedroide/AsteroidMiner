package ch.kanti_wohlen.asteroidminer.powerups;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.Textures;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class ShieldPowerUp extends PowerUp {

	private static final float DROP_FREQUENCY = 1f;
	private static final int SHIELD_AMOUNT = 100;
	
	public ShieldPowerUp(World world, Vector2 position) {
		super(world, position);
	}

	@Override
	public void onPickUp(Player player) {
		player.getSpaceShip().setShield(SHIELD_AMOUNT);
	}

	@Override
	public float getDropFrequency() {
		return DROP_FREQUENCY;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.SHILDPOWERUPBOX;
		positionSprite(s);
		s.draw(batch);
	}
}
