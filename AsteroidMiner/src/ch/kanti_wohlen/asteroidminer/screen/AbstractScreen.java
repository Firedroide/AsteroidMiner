package ch.kanti_wohlen.asteroidminer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public abstract class AbstractScreen implements Screen {
	
	protected float height;
	protected float width;
	
	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void show() {
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
	}
}
