package io.intino.konos.server.activity.displays.elements.model.toolbar;

public class OpenDialog extends Operation {
	private int width = 100;
	private int height = 100;
	private String path;

	public int width() {
		return width;
	}

	public OpenDialog width(int width) {
		this.width = width;
		return this;
	}

	public int height() {
		return height;
	}

	public OpenDialog height(int height) {
		this.height = height;
		return this;
	}

	public String path() {
		return path;
	}

	public OpenDialog path(String path) {
		this.path = path;
		return this;
	}
}