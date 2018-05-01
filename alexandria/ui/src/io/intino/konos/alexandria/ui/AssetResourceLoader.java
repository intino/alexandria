package io.intino.konos.alexandria.ui;

import io.intino.konos.alexandria.Box;

public class AssetResourceLoader {
	private final Box box;

	public AssetResourceLoader(Box box) {
		this.box = box;
	}

	public java.net.URL load(String name) {
		return AssetResourceLoader.class.getResource(name);
	}

}
