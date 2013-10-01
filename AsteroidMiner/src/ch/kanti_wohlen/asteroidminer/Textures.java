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
	
	private Textures() {};
	
	/**
	 * Loads the textures into memory.
	 * Needs to be called before drawing any textures except the background
	 */
	public static void load() {
		Texture bg = new Texture(Gdx.files.internal("graphics/background.png"));
		bg.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		BACKGROUND = new Sprite(bg, bg.getWidth(), bg.getHeight());
		
		Texture sprites = new Texture(Gdx.files.internal("graphics/sprites.png"));
		SPACESHIP = new Sprite(sprites, 64, 0, 64, 64);
		SPACESHIP.setOrigin(32, 32);
		SPACESHIP.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		ASTEROID = new Sprite(sprites, 192, 0, 64, 64);
		ASTEROID.setOrigin(32, 32);
		ASTEROID.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
}
