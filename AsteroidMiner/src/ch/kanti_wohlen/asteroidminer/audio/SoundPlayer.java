package ch.kanti_wohlen.asteroidminer.audio;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class SoundPlayer {

	private static Map<SoundEffect, Sound> sounds;

	private SoundPlayer() {}

	public static void loadSounds() {
		sounds = new HashMap<SoundPlayer.SoundEffect, Sound>();
		for (SoundEffect effect : SoundEffect.values()) {
			effect.load();
		}
	}

	public static void dispose() {
		for (Sound sound : sounds.values()) {
			sound.dispose();
		}
	}

	public static void playSound(SoundEffect soundEffect, float volume) {
		playSound(soundEffect, volume, 1f, 0f);
	}

	public static void playSound(SoundEffect soundEffect, float volume, float pitch, float pan) {
		Sound sound = sounds.get(soundEffect);
		sound.play(volume, pitch, pan);
	}

	public enum SoundEffect {
		LASER_SHOOT("LaserShoot.ogg"),
		POWER_UP_PICK_UP("PowerUpPickUp.ogg");

		private final String name;

		private SoundEffect(String fileName) {
			name = fileName;
		}

		private void load() {
			FileHandle fh = Gdx.files.internal("sounds/" + name);
			Sound sound = Gdx.audio.newSound(fh);
			sounds.put(this, sound);
		}
	}
}
