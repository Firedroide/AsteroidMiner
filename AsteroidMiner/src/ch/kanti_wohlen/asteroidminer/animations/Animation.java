package ch.kanti_wohlen.asteroidminer.animations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Animation {

	public void render(SpriteBatch batch);

	public void tick(float deltaTime);

	public boolean isRemoved();

	public void dispose();
}
