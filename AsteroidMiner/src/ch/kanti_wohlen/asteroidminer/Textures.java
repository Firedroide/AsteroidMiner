package ch.kanti_wohlen.asteroidminer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

public final class Textures {

	public static Sprite BACKGROUND;
	public static Sprite SPACESHIP;
	public static Sprite ASTEROID;
	public static Sprite METALASTEROID;
	public static Sprite ICEASTREOID;
	public static Sprite EXPLOSIVEASTEROID;
	public static Sprite LASER;
	public static Sprite LIFEPOWERUPBOX;
	public static Sprite SHILDPOWERUPBOX;
	public static Sprite FIRINGSPEEDPOWERUPBOX;
	public static Sprite SPEEDPOWERUPBOX;
	public static Sprite FIRINGFORCEPOWERUPBOX;
	public static Sprite POWERUPBOXMETAL;
	public static Sprite BOMBPOWERUPBOX;
	public static Sprite POWERUPBOXALUMINIUM;
	public static Sprite EXPLOSION;
	public static Sprite PROJECTILE;
	public static TiledDrawable BORDER;
	public static TiledDrawable BORDER_LINE;

	public static Sprite HEALTH_HIGH;
	public static Sprite HEALTH_LOW;
	public static Sprite SHIELD_HIGH;
	public static Sprite SHIELD_LOW;

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

		METALASTEROID = new Sprite(sprites, 493, 214, 123, 123);
		METALASTEROID.setOrigin(METALASTEROID.getRegionWidth() / 2f, METALASTEROID.getRegionHeight() / 2f);
		METALASTEROID.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		ICEASTREOID = new Sprite(sprites, 906, 220, 93, 93);
		ICEASTREOID.setOrigin(ICEASTREOID.getRegionWidth() / 2, ICEASTREOID.getRegionHeight() / 2);
		ICEASTREOID.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		EXPLOSIVEASTEROID = new Sprite(sprites, 845, 336, 114, 114);
		EXPLOSIVEASTEROID.setOrigin(EXPLOSIVEASTEROID.getRegionWidth() / 2, EXPLOSIVEASTEROID.getRegionHeight() / 2);
		EXPLOSIVEASTEROID.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		LASER = new Sprite(sprites, 364, 177, 7, 12);
		LASER.setOrigin(LASER.getRegionWidth() / 2, LASER.getRegionHeight() / 2);
		LASER.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		LIFEPOWERUPBOX = new Sprite(sprites, 256, 154, 38, 38);
		LIFEPOWERUPBOX.setOrigin(LIFEPOWERUPBOX.getRegionWidth() / 2, LIFEPOWERUPBOX.getRegionHeight() / 2);
		LIFEPOWERUPBOX.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		SHILDPOWERUPBOX = new Sprite(sprites, 772, 151, 38, 39);
		SHILDPOWERUPBOX.setOrigin(SHILDPOWERUPBOX.getRegionWidth() / 2, SHILDPOWERUPBOX.getRegionHeight() / 2);
		SHILDPOWERUPBOX.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		FIRINGSPEEDPOWERUPBOX = new Sprite(sprites, 825, 149, 38, 41);
		FIRINGSPEEDPOWERUPBOX.setOrigin(FIRINGSPEEDPOWERUPBOX.getRegionWidth() / 2, FIRINGSPEEDPOWERUPBOX.getRegionHeight() / 2);
		FIRINGSPEEDPOWERUPBOX.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		SPEEDPOWERUPBOX = new Sprite(sprites, 879, 148, 36, 43);
		SPEEDPOWERUPBOX.setOrigin(SPEEDPOWERUPBOX.getRegionWidth() / 2, SPEEDPOWERUPBOX.getRegionHeight() / 2);
		SPEEDPOWERUPBOX.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		BOMBPOWERUPBOX = new Sprite(sprites, 932, 152, 40, 37);
		BOMBPOWERUPBOX.setOrigin(BOMBPOWERUPBOX.getRegionWidth() / 2, BOMBPOWERUPBOX.getRegionHeight() / 2);
		BOMBPOWERUPBOX.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		FIRINGFORCEPOWERUPBOX = new Sprite(sprites, 739, 206, 43, 39);
		FIRINGFORCEPOWERUPBOX.setOrigin(FIRINGFORCEPOWERUPBOX.getRegionWidth() / 2, FIRINGFORCEPOWERUPBOX.getRegionHeight() / 2);
		FIRINGFORCEPOWERUPBOX.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		POWERUPBOXMETAL = new Sprite(sprites, 57, 154, 38, 38);
		POWERUPBOXMETAL.setOrigin(POWERUPBOXMETAL.getRegionWidth() / 2, POWERUPBOXMETAL.getRegionHeight() / 2);
		POWERUPBOXMETAL.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		POWERUPBOXALUMINIUM = new Sprite(sprites, 166, 154, 38, 38);
		POWERUPBOXALUMINIUM.setOrigin(POWERUPBOXALUMINIUM.getRegionWidth() / 2, POWERUPBOXALUMINIUM.getRegionHeight() / 2);
		POWERUPBOXALUMINIUM.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		HEALTH_HIGH = new Sprite(sprites, 20, 350, 80, 10);
		HEALTH_HIGH.setOrigin(HEALTH_HIGH.getRegionWidth() / 2, 0);
		HEALTH_HIGH.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		HEALTH_LOW = new Sprite(sprites, 20, 360, 80, 10);
		HEALTH_LOW.setOrigin(HEALTH_LOW.getRegionWidth() / 2, 0);
		HEALTH_LOW.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		SHIELD_HIGH = new Sprite(sprites, 20, 375, 80, 10);
		SHIELD_HIGH.setOrigin(SHIELD_HIGH.getRegionWidth() / 2, 0);
		SHIELD_HIGH.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		SHIELD_LOW = new Sprite(sprites, 20, 385, 80, 10);
		SHIELD_LOW.setOrigin(SHIELD_LOW.getRegionWidth() / 2, 0);
		SHIELD_LOW.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		EXPLOSION = new Sprite(sprites, 268, 360, 472, 496);
		EXPLOSION.setOrigin(490, 623);
		EXPLOSION.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		PROJECTILE = new Sprite(sprites, 20, 400, 30, 39);
		PROJECTILE.setOrigin(PROJECTILE.getRegionWidth() / 2f, PROJECTILE.getRegionHeight() / 2f);
		PROJECTILE.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureRegion borderRegion = new TextureRegion(sprites, 10, 950, 64, 64);
		BORDER = new TiledDrawable(borderRegion);

		TextureRegion borderLine = new TextureRegion(sprites, 10, 1020, 1, 1);
		BORDER_LINE = new TiledDrawable(borderLine);
	}
}
