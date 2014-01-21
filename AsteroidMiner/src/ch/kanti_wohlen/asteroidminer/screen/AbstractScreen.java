package ch.kanti_wohlen.asteroidminer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public abstract class AbstractScreen implements Screen {

	protected float width;
	protected float height;

	public AbstractScreen() {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
	}

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

	@Override
	public void pause() {
		// Irrelevant on desktop / web
	}

	@Override
	public void resume() {
		// Irrelevant on desktop / web
	}
}
