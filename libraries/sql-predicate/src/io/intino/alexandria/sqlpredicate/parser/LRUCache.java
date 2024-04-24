package io.intino.alexandria.sqlpredicate.parser;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
	protected int maxCacheSize = 10000;

	public LRUCache() {
		this(0, 10000, 0.75f, true);
	}

	public LRUCache(int maximumCacheSize) {
		this(0, maximumCacheSize, 0.75f, true);
	}

	public LRUCache(int initialCapacity, int maximumCacheSize, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
		this.maxCacheSize = maximumCacheSize;
	}

	public int getMaxCacheSize() {
		return maxCacheSize;
	}

	public void setMaxCacheSize(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
	}

	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		if (size() > maxCacheSize) {
			onCacheEviction(eldest);
			return true;
		}
		return false;
	}

	protected void onCacheEviction(Map.Entry<K, V> eldest) {
	}

}
