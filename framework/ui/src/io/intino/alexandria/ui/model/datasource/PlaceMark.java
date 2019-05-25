package io.intino.alexandria.ui.model.datasource;

import io.intino.alexandria.ui.model.datasource.locations.Location;

public class PlaceMark<O> {
	private O item;
	private Location location;

	public O item() {
		return item;
	}

	public PlaceMark item(O item) {
		this.item = item;
		return this;
	}

	public Location location() {
		return location;
	}

	public PlaceMark location(Location location) {
		this.location = location;
		return this;
	}
}
