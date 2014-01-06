package ch.kanti_wohlen.asteroidminer;

/*
 * Simple pair class because AbstractMap.SimpleEntry and AbstractMap.SimpleImmutableEntry
 * seem to fail to compile in GWT.
 */
public class Pair<K, V> {

	private final K k;
	private final V v;

	public Pair(K key, V value) {
		k = key;
		v = value;
	}

	public K getKey() {
		return k;
	}

	public V getValue() {
		return v;
	}
}
