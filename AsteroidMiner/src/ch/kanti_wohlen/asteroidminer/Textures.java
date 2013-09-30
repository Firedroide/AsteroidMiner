package ch.kanti_wohlen.asteroidminer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;

public final class Textures {
	
	public static Sprite BACKGROUND;
	
	private Textures() {};
	
	/**
	 * Loads the textures into memory.
	 * Needs to be called before drawing any textures except the background
	 */
	public static void load() {
		Texture bg = new Texture(Gdx.files.internal("graphics/background.png"));
		bg.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		BACKGROUND = new Sprite(bg, bg.getWidth(), bg.getHeight());
	}
}
