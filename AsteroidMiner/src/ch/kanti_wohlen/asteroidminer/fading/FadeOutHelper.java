package ch.kanti_wohlen.asteroidminer.fading;

import ch.kanti_wohlen.asteroidminer.TaskScheduler;
import ch.kanti_wohlen.asteroidminer.TaskScheduler.Task;

public class FadeOutHelper implements Runnable {

	private final Fadeable source;
	private final float time;
	private final Task task;
	private float currentTime;
	private final Runnable doneListener;

	public FadeOutHelper(Fadeable fadeable, float startDelay, float fadeOutTime) {
		this(fadeable, startDelay, fadeOutTime, null);
	}

	public FadeOutHelper(Fadeable fadeable, float startDelay, float fadeOutTime, Runnable completionListener) {
		source = fadeable;
		time = fadeOutTime;
		task = TaskScheduler.INSTANCE.runTaskRepeated(this, startDelay, 0, time);
		doneListener = completionListener;
	}

	@Override
	public void run() {
		currentTime += 1 * TaskScheduler.TICK_TIME;
		source.setAlpha(1f - currentTime / time);
		if (time - currentTime < TaskScheduler.TICK_TIME) {
			if (doneListener != null) doneListener.run();
		}
	}

	public void cancel() {
		task.cancel();
	}
}
