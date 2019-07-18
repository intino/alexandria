package io.intino.alexandria.ui.model.datasource;

import io.intino.alexandria.ui.model.datasource.locations.Location;

import java.net.URL;

public class PlaceMark<O> {
	private O item;
	private String label;
	private URL icon = null;
	private Location position;

	public O item() {
		return item;
	}

	public PlaceMark item(O item) {
		this.item = item;
		return this;
	}

	public String label() {
		return label;
	}

	public PlaceMark label(String label) {
		this.label = label;
		return this;
	}

	public URL icon() {
		return icon;
	}

	public PlaceMark icon(URL icon) {
		this.icon = icon;
		return this;
	}

	public Location location() {
		return position;
	}

	public PlaceMark location(Location location) {
		this.position = location;
		return this;
	}

}
