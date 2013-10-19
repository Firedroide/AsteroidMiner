package ch.kanti_wohlen.asteroidminer.powerups;

import ch.kanti_wohlen.asteroidminer.Player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class LifePowerUp extends PowerUp {

	private static float Drop_Frequency;

	public LifePowerUp(World world, BodyDef bodyDef, Shape collisionBox) {
		super(world, bodyDef, collisionBox);
	}

	@Override
	public void onPickUp(Player p) {
		p.addLife(1);
	}

	@Override
	public float getDropFrequency() {
		return Drop_Frequency;
	}

	@Override
	public void render(SpriteBatch batch) {

	}

}
