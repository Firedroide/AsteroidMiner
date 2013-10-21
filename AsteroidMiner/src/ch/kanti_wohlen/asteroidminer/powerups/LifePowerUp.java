package ch.kanti_wohlen.asteroidminer.powerups;

import ch.kanti_wohlen.asteroidminer.Player;
import ch.kanti_wohlen.asteroidminer.Textures;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class LifePowerUp extends PowerUp {

	private static final float DROP_FREQUENCY = 1f;
	private static final int HEALING_AMOUNT = 40;

	public LifePowerUp(World world, BodyDef bodyDef, Shape collisionBox) {
		super(world, bodyDef, collisionBox);
	}

	@Override
	public void onPickUp(Player player) {
		player.getSpaceShip().heal(HEALING_AMOUNT);
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
