package io.intino.alexandria.ui.model.datasource;

import io.intino.alexandria.ui.model.datasource.locations.Location;

public class PlaceMark<O> {
	private O item;
	private Location position;

	public O item() {
		return item;
	}

	public PlaceMark item(O item) {
		this.item = item;
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
