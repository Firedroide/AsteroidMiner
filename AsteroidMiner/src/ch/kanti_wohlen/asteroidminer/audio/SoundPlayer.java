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
		playSound(soundEffect, volume, pitch, pan, false);
	}

	public static void playSound(SoundEffect soundEffect, float volume, float pitch, float pan, boolean looping) {
		Sound sound = sounds.get(soundEffect);
		final long id = sound.play(volume, pitch, pan);
		sound.setLooping(id, looping);
		soundEffect.setSound(sound, id);
	}

	public static void stopSound(SoundEffect soundEffect) {
		if (soundEffect.currentSound != null) {
			soundEffect.currentSound.stop();
		}
	}

	public enum SoundEffect {
		LASER_SHOOT("LaserShoot.ogg"),
		POWER_UP_PICK_UP("PowerUpPickUp.ogg"),
		THRUSTER("Thrusters.ogg");

		private final String name;
		private long currentID;
		private Sound currentSound;

		private SoundEffect(String fileName) {
			name = fileName;
		}

		private void load() {
			FileHandle fh = Gdx.files.internal("sounds/" + name);
			Sound sound = Gdx.audio.newSound(fh);
			sounds.put(this, sound);
		}

		private void setSound(Sound sound, long id) {
			currentSound = sound;
			currentID = id;
		}

		public void setVolume(float volume) {
			if (currentSound != null) {
				currentSound.setVolume(currentID, volume);
			}
		}
	}
}
