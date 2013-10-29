package ch.kanti_wohlen.asteroidminer.powerups;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.Textures;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class ShieldPowerUP extends PowerUp {

	private static final float DROP_FREQUENCY = 1f;

	public ShieldPowerUP(World world, BodyDef bodyDef, Shape collisionBox) {
		super(world, bodyDef, collisionBox);
	}

	@Override
	public void onPickUp(Player player) {
		if (player.getSpaceShip().getShieldEnabled()) {
			//TODO If Shield already enabled, add a second shield to the existing one after the time expired. 
		} else {
			player.getSpaceShip().setShieldEnabled(true);
		}
	}

	@Override
	public float getDropFrequency() {
		return DROP_FREQUENCY;
	}

	@Override
	public void render(SpriteBatch batch) {
		Sprite s = Textures.LIFEPOWERUPBOX;
		positionSprite(s);
		s.draw(batch);
	}

}
