package ch.kanti_wohlen.asteroidminer.animations;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public final class Animations {

	private static final List<Animation> animations = new LinkedList<Animation>();

	private Animations() {}

	static void addAnimation(Animation a) {
		animations.add(a);
	}

	public static void renderAll(SpriteBatch batch) {
		for (Animation a : animations) {
			a.render(batch);
		}
	}

	public static void tickAll(float deltaTime) {
		for (Animation a : animations) {
			a.tick(deltaTime);
		}

		Iterator<Animation> iter = animations.iterator();
		while (iter.hasNext()) {
			Animation a = iter.next();
			if (a.isRemoved()) {
				iter.remove();
				a.dispose();
			}
		}
	}
}
