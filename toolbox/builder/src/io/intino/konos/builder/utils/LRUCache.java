package io.intino.konos.builder.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
	protected int maxCacheSize;

	public LRUCache() {
		this(0, 10000, 0.75F, true);
	}

	public LRUCache(int maximumCacheSize) {
		this(0, maximumCacheSize, 0.75F, true);
	}

	public LRUCache(int initialCapacity, int maximumCacheSize, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
		this.maxCacheSize = 10000;
		this.maxCacheSize = maximumCacheSize;
	}

	public int getMaxCacheSize() {
		return this.maxCacheSize;
	}

	public void setMaxCacheSize(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
	}

	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		if (this.size() > this.maxCacheSize) {
			this.onCacheEviction(eldest);
			return true;
		} else {
			return false;
		}
	}

	protected void onCacheEviction(Map.Entry<K, V> eldest) {
	}
}

