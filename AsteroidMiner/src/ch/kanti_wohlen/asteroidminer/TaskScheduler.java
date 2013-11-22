package ch.kanti_wohlen.asteroidminer;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TaskScheduler {

	/**
	 * The current and only instance of TaskScheduler.<br>
	 * Is never <code>null</code>.
	 */
	public static final TaskScheduler INSTANCE = new TaskScheduler();

	private Map<Long, Runnable[]> tasks;

	private TaskScheduler() {
		tasks = new TreeMap<Long, Runnable[]>();
	}

	public void dispose() {
		tasks.clear();
	}

	/**
	 * Run task after rendering and after simulating the world.<br>
	 * This way the Runnable can be executed safely.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. Won't add to queue if <code>null</code>
	 */
	public void runTask(Runnable r) {
		if (r == null) return;
		addTask(0l, r);
	}

	/**
	 * Runs a task after a delay in seconds.
	 * 
	 * @param r
	 *            the {@link Runnable} you want to run. Won't add to queue if <code>null</code>
	 * @param delay
	 *            the time in seconds until the method should be called
	 */
	public void runTaskLater(Runnable r, double delay) {
		if (r == null) return;
		long executionTime = System.currentTimeMillis() + (long) (1000 * Math.max(0d, delay));
		addTask(executionTime, r);
	}

	private void addTask(long when, Runnable what) {
		if (tasks.containsKey(when)) {
			Runnable[] oldArr = tasks.get(when);
			Runnable[] newArr = new Runnable[oldArr.length + 1];
			System.arraycopy(oldArr, 0, newArr, 0, oldArr.length);
			newArr[oldArr.length] = what;
			tasks.put(when, newArr);
		} else {
			tasks.put(when, new Runnable[] {what});
		}
	}

	/**
	 * Method to actually execute the stored Runnables.<br>
	 * Should only ever be called once in GameScreen.
	 */
	public void onGameTick() {
		Iterator<Entry<Long, Runnable[]>> i = tasks.entrySet().iterator();

		while (i.hasNext()) {
			Entry<Long, Runnable[]> entry = i.next();

			if (entry.getKey() <= System.currentTimeMillis()) {
				for (Runnable task : entry.getValue()) {
					task.run();
				}
				i.remove();
			} else {
				break;
			}
		}
	}
}
