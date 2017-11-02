package io.intino.konos.alexandria.framework.box.model.catalog.events;

public class OpenDialog extends Open {
	private int width = 100;
	private int height = 100;
	private Path path;

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

	public String path(String item) {
		return path != null ? path.path(item) : "";
	}

	public OpenDialog path(Path path) {
		this.path = path;
		return this;
	}

	public interface Path {
		String path(String item);
	}
}
