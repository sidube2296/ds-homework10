package edu.uwm.cs351.util;

/**
 * An entry in a Map.  A default implementation.
 * @see {@link java.util.Map.Entry}
 */
public class DefaultEntry<K,V> extends AbstractEntry<K,V> {

	protected K key;
	protected V value;
	
	public DefaultEntry(K k, V v) { key = k; value = v; }
	
	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public V setValue(V v) {
		V old = value;
		value = v;
		return old;
	}
}