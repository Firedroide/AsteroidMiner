package ch.kanti_wohlen.asteroidminer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;

public final class Textures {

	public static Sprite BACKGROUND;
	public static Sprite SPACESHIP;
	public static Sprite ASTEROID;
	public static Sprite LASER;
	public static Sprite LIFEPOWERUPBOX;

	public static Sprite HEALTH_HIGH;
	public static Sprite HEALTH_LOW;

	private Textures() {};

	/**
	 * Loads the textures into memory.
	 * Needs to be called before drawing any textures.
	 */
	public static void load() {
		Texture bg = new Texture(Gdx.files.internal("graphics/background.png"));
		bg.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		BACKGROUND = new Sprite(bg, bg.getWidth(), bg.getHeight());

		Texture sprites = new Texture(Gdx.files.internal("graphics/sprites.png"));
		SPACESHIP = new Sprite(sprites, 629, 34, 70, 81);
		SPACESHIP.setOrigin(SPACESHIP.getRegionWidth() / 2, SPACESHIP.getRegionHeight() / 2);
		SPACESHIP.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		ASTEROID = new Sprite(sprites, 164, 237, 84, 83);
		ASTEROID.setOrigin(ASTEROID.getRegionWidth() / 2, ASTEROID.getRegionHeight() / 2);
		ASTEROID.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		LASER = new Sprite(sprites, 364, 177, 7, 12);
		LASER.setOrigin(LASER.getRegionWidth() / 2, LASER.getRegionHeight() / 2);
		LASER.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		LIFEPOWERUPBOX = new Sprite(sprites, 256, 154, 38, 38);
		LIFEPOWERUPBOX.setOrigin(LIFEPOWERUPBOX.getRegionWidth() / 2, LIFEPOWERUPBOX.getRegionHeight() / 2);
		LIFEPOWERUPBOX.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		HEALTH_HIGH = new Sprite(sprites, 20, 350, 80, 10);
		HEALTH_HIGH.setOrigin(HEALTH_HIGH.getRegionWidth() / 2, 0);
		HEALTH_HIGH.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		HEALTH_LOW = new Sprite(sprites, 20, 360, 80, 10);
		HEALTH_LOW.setOrigin(HEALTH_LOW.getRegionWidth() / 2, 0);
		HEALTH_LOW.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
}
